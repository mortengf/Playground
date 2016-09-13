package playground.spark.external;

import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import playground.spark.external.comparison_method.ComparisonMethod;
import playground.spark.external.comparison_method.ExactComparisonMethod;
import playground.spark.external.comparison_method.RangeComparisonMethod;
import scala.Tuple2;
import scala.collection.Seq;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SparkExternalDataLookup {
    private static SparkSession session;
    private static SQLContext sqlContext;

    static {
        SparkConf conf = new SparkConf().setAppName("Spark External Data Lookup Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sqlContext = new SQLContext(sc);
        session = SparkSession.builder().getOrCreate();
    }

    private static MatchConfig createMatchConfig() {
        MatchConfig matchConfig = new MatchConfig();

        // TODO: this must be possible to define dynamically
        matchConfig.addFieldConfig("firstName", new FieldConfig(true, new ExactComparisonMethod(), 0.2));
        matchConfig.addFieldConfig("lastName", new FieldConfig(true, new ExactComparisonMethod(), 0.3));
        matchConfig.addFieldConfig("age", new FieldConfig(true, new RangeComparisonMethod(20), 0.5));
        matchConfig.addFieldConfig("sex", new FieldConfig(false));

        // TODO: check that sum of weights = 1.0

        return matchConfig;
    }

    private static StructField[] getFields(Row dataRow) {
        StructType schema = dataRow.schema();
        Seq<StructField> seq = schema.seq();
        return ((StructType) seq).fields();
    }

    private static Double calculateSimilarityScore(Object sourceValue, Object targetValue, ComparisonMethod comparisonMethod) {
        return comparisonMethod.compare(sourceValue, targetValue);
    }

    private static Double calculateTotalScore(Map<String, Double> fieldNameScores, Map<String, Double>
            fieldNameWeights) {
        Double totalScore = 0.0;

        // TODO: same "source/target field name" comment as below
        for (String fieldName : fieldNameScores.keySet()) {
            Double score = fieldNameScores.get(fieldName);
            Double weight = fieldNameWeights.get(fieldName);
            totalScore += score * weight;
        }

        return totalScore;
    }

    private static void enrichTargetRowWithSimilarityScores(DataRow sourceRow, DataRow targetRow, MatchConfig matchConfig) {
        Row sourceRowRdd = sourceRow.getRddRow();
        StructField[] sourceFields = getFields(sourceRowRdd);

        Map<String, Double> fieldNameWeights = new HashMap<String, Double>();

        // TODO: we use the source field name to get the target field config so currently source and target names
        // must match: implement a <source field name, target field name> mapping or at least a check
        for (StructField sourceField : sourceFields) {
            String sourceFieldName = sourceField.name();
            Object sourceValue = sourceRowRdd.getAs(sourceFieldName);

            FieldConfig fieldConfig = matchConfig.getFieldConfig(sourceFieldName);
            if (fieldConfig.isKey()) {
                Object targetValue = targetRow.getFieldValue(sourceFieldName);
                Double similarityScore = calculateSimilarityScore(sourceValue, targetValue, fieldConfig.getComparisonMethod());
                targetRow.setSimilarityScore(sourceFieldName, similarityScore);

                Double weight = fieldConfig.getWeight();
                fieldNameWeights.put(sourceFieldName, weight);
            }
        }

        Double totalScore = calculateTotalScore(targetRow.getFieldNameScores(), fieldNameWeights);
        targetRow.setTotalScore(totalScore);
    }

    private static JSONArray getJSONRows(String jsonFilename) {
        JSONParser jsonParser = new JSONParser();
        Object rawJson = null;
        try {
            rawJson = jsonParser.parse(new FileReader("src/main/resources/spark/external/" + jsonFilename + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) rawJson;
        return (JSONArray) jsonObject.get("rows");
    }

    private static DataRow createDataRowFrom(JSONObject row) {
        Map<String, Object> fieldNameValues = new HashMap<String, Object>();
        for (Object key : row.keySet()) {
            String keyName = (String) key;
            Object value = row.get(key);
            fieldNameValues.put(keyName, value);
        }
        return new DataRow(fieldNameValues);
    }

    private static OrderedHashSet<DataRow> getTopMatches(String matchDatasetName, int top) {
        OrderedHashSet<DataRow> topMatchesOrdered = new OrderedHashSet<DataRow>();

        // TODO: get actual top matches, e.g. from Elasticsearch
        JSONArray rows = getJSONRows(matchDatasetName);
        for (int i=0; i<rows.size(); i++) {
            JSONObject row = (JSONObject) rows.get(i);
            DataRow topMatch = createDataRowFrom(row);
            topMatchesOrdered.add(topMatch);
            if (i > top) break;
        }

        return topMatchesOrdered;
    }

    /**
     * Lookup in uncool people: get top X matches (X will be low (< 100?)).
     *
     * @return top matches of cool and uncool people with similarity scores
     */
    private static Tuple2<DataRow, Set<DataRow>> getTopMatchesWithSimilarityScores(DataRow key, String
            matchDatasetName, MatchConfig matchConfig, int top, Double threshold) {
        if (top > 100) {
            throw new IllegalArgumentException("Input argument 'top' must be below 100");
        }
        Set<DataRow> topMatchesAboveThreshold = new HashSet<DataRow>();

        OrderedHashSet<DataRow> topMatches = getTopMatches(matchDatasetName, top);
        for (DataRow topMatch : topMatches) {
            enrichTargetRowWithSimilarityScores(key, topMatch, matchConfig);
            if (topMatch.getTotalScore() >= threshold) {
                topMatchesAboveThreshold.add(topMatch);
            }
        }

        return new Tuple2<DataRow, Set<DataRow>>(key, topMatchesAboveThreshold);
    }

    public static void main(String[] args) {
        // TODO: send these to worker nodes via broadcast variables?
        final Double threshold = 0.01;
        final int top = 15;
        final MatchConfig matchConfig = createMatchConfig();

        JavaRDD<Row> coolPeopleRDD = session.read().json("src/main/resources/spark/external/cool_people.json").toJavaRDD();

        // Loop over cool people - for each...
        //JavaRDD<Tuple2<DataRow, Set<DataRow>>> coolAndUncoolPeopleMatchedAndEnrichedWithScores =
        List<Tuple2<DataRow, Set<DataRow>>> coolAndUncoolPeopleWithScoresCollected = coolPeopleRDD.map(new Function<Row, Tuple2<DataRow, Set<DataRow>>>() {
            public Tuple2<DataRow, Set<DataRow>> call(Row rddRow) throws Exception {
                DataRow coolDataRow = new DataRow(rddRow);
                Tuple2<DataRow, Set<DataRow>> topMatchesWithScores = getTopMatchesWithSimilarityScores(coolDataRow,
                        "uncool_people", matchConfig, top, threshold);
                return topMatchesWithScores;
            }
        }).collect(); // The collect() call must be chained. If not, Spark will not wait for map() to finish.

        // Collect results
        System.out.println("Cool and uncool people with scores:");
        for (Tuple2<DataRow, Set<DataRow>> coolPersonAndUncoolPeopleWithScores : coolAndUncoolPeopleWithScoresCollected) {
            System.out.println(coolPersonAndUncoolPeopleWithScores);
        }

    }
}
