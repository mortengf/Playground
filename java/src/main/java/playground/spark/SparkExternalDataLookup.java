package playground.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import playground.spark.row.ComparisonMethod;
import playground.spark.row.DataRow;
import playground.spark.row.FieldConfig;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.*;

public class SparkExternalDataLookup {
    private static SparkSession session;
    private static SQLContext sqlContext;

    static {
        SparkConf conf = new SparkConf().setAppName("Spark External Data Lookup Fun\"").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sqlContext = new SQLContext(sc);
        session = SparkSession.builder().getOrCreate();
    }

    private static StructField[] getFields(Row dataRow) {
        StructType schema = dataRow.schema();
        Seq<StructField> seq = schema.seq();
        return ((StructType) seq).fields();
    }

    private static Double calculateSimilarityScore(Object sourceValue, Object targetValue, ComparisonMethod comparisonMethod) {
        return comparisonMethod.compare(sourceValue, targetValue);
    }

    private static Double calculateTotalScore(Map<String, Double> fieldNameScores) {
        Double totalScore = 0.0;
        // TODO: implement
        return totalScore;
    }

    private static void enrichTargetRowWithSimilarityScores(DataRow source, DataRow target) {
        Row sourceDataRowRdd = source.getRddRow();
        StructField[] sourceFields = getFields(sourceDataRowRdd);

        Row targetDataRowRdd = target.getRddRow();
        for (StructField sourceField : sourceFields) {
            String sourceFieldName = sourceField.name();
            Object sourceValue = sourceDataRowRdd.getAs(sourceFieldName);

            // TODO: we use the source field name to get the target field config so currently source and target names
            // must match: at least implement a check for this
            FieldConfig targetFieldConfig = target.getFieldConfig(sourceFieldName);
            if (targetFieldConfig.isKey()) {
                Object targetValue = targetDataRowRdd.getAs(sourceFieldName);
                Double similarityScore = calculateSimilarityScore(sourceValue, targetValue, targetFieldConfig.getComparisonMethod());
                target.setSimilarityScore(sourceField.name(), similarityScore);
            }
        }

        Double totalScore = calculateTotalScore(target.getFieldNameScores());
        target.setTotalScore(totalScore);
    }

    /**
     * Lookup in uncool people: get top X matches (X will be low (< 100?)).
     *
     * @return top matches of cool and uncool people with similarity scores
     */
    private static Tuple2<DataRow, Set<DataRow>> getTopMatchesWithSimilarityScores(DataRow key, String matchDatasetName, Double
            threshold) {
        Set<DataRow> topMatchesAboveThresholdWithScores = new HashSet<DataRow>();

        // TODO: this should be lookup in Elasticsearch - use RDD from "elasticsearch-hadoop"?
        JavaRDD<Row> uncoolPeopleRDD = session.read().json("src/main/resources/spark/join/" + matchDatasetName
                + ".json").toJavaRDD();
        List<Row> topMatches = uncoolPeopleRDD.collect(); // This is OK because the number of rows is low

        for (Row topMatchRow : topMatches) {
            DataRow topMatch = new DataRow(topMatchRow);
            enrichTargetRowWithSimilarityScores(key, topMatch);
            if (topMatch.getTotalScore() >= threshold) {
                topMatchesAboveThresholdWithScores.add(topMatch);
            }
        }

        return new Tuple2<DataRow, Set<DataRow>>(key, topMatchesAboveThresholdWithScores);
    }

    public static void main(String[] args) {
        final Double threshold = 0.7;

        JavaRDD<Row> coolPeopleRDD = session.read().json("src/main/resources/spark/join/cool_people.json").toJavaRDD();

        // Loop over cool people - for each...
        JavaRDD<Tuple2<DataRow, Set<DataRow>>> coolAndUncoolPeopleMatchedAndEnrichedWithScores = coolPeopleRDD.map(new Function<Row, Tuple2<DataRow, Set<DataRow>>>() {
            public Tuple2<DataRow, Set<DataRow>> call(Row rddRow) throws Exception {

                DataRow coolDataRow = new DataRow(rddRow);
                Tuple2<DataRow, Set<DataRow>> topMatchesWithScores = getTopMatchesWithSimilarityScores(coolDataRow, "uncool_people", threshold);
                return topMatchesWithScores;
            }
        });

        // Collect results
        List<Tuple2<DataRow, Set<DataRow>>> coolAndUncoolPeopleWithScoresCollected = coolAndUncoolPeopleMatchedAndEnrichedWithScores.collect();
        for (Tuple2<DataRow, Set<DataRow>> coolPersonAndUncoolPeopleWithScores : coolAndUncoolPeopleWithScoresCollected) {
            System.out.println(coolPersonAndUncoolPeopleWithScores);
        }

    }
}
