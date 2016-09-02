package playground.spark.join;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.api.java.UDF8;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import playground.spark.join.dto.FieldDTO;
import playground.spark.join.dto.KeyDTO;
import playground.spark.join.dto.KeyFieldDTO;
import playground.spark.join.dto.ValueDTO;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiments with JOINs in Apache Spark.
 *
 * End goal is to JOIN two data sets using custom criteria, e.g. fuzzy logic, e.g. range/interval for numbers or dates
 * and various "distance methods", e.g. Levenshtein, for strings.
 *
 * For the provided test data sets, match:
 * <ul>
 * <li>"Cool" "Sam Harris" with "uncool" "Sam Harris" even though their age is different, ie. use the
 * "selected comparison methods" and weights defined on the KeyFieldDTOs.</li>
 * <li>Multiple "Elon Musk"s within the same data set.</li>
 * </ul>
 *
 * Corresponding SQL would be something like:
 *
 * <pre>
 *  SELECT  *
 *  FROM    cool c
 *  JOIN    uncool uc
 *  ON      c.firstName = uc.firstName AND
 *          c.lastName = uc.lastName AND
 *          diff(c.age, uc.age) < 3 AND
 *          c.sex = uc.sex;
 *
 * </pre>
 *
 * Use a statically-defined config.
*/
public class SparkCustomJoinFun {
    private static SparkSession session;
    private static SQLContext sqlContext;

    private static JoinConfig joinConfig;
    private static int numberOfJoinConfigFields;

    static {
        SparkConf conf = new SparkConf().setAppName("Spark Custom JOIN Fun").setMaster("local");

        // A (Java)SparkContext must be instantiated even though the object is never used - otherwise the program
        // will throw a "SparkException: A master URL must be set in your configuration"
        JavaSparkContext sc = new JavaSparkContext(conf);
        sqlContext = new SQLContext(sc);
        session = SparkSession.builder().getOrCreate();
    }

    private static void printPeople(List<Row> people) {
        for (Row person : people) {
            System.out.println(person);
        }
    };

    private static void doCartesianProduct(Dataset<Row> dataset1, Dataset<Row> dataset2) {
        Dataset<Row> cartesianProduct = dataset1.join(dataset2);
        JavaRDD<Row> cartesianProductRDD = cartesianProduct.toJavaRDD();
        List<Row> cartesianProductCollected = cartesianProductRDD.collect();
        System.out.println("Cartesian product:");
        for (Row row : cartesianProductCollected) {
            System.out.println(row);
        }
    }

    private static void initJoinConfig() {
        joinConfig = new JoinConfig();

        // TODO: this must be possible to define dynamically
        joinConfig.addKeyField(new KeyFieldDTO("firstName", DataTypes.StringType));
        joinConfig.addKeyField(new KeyFieldDTO("lastName", DataTypes.StringType));

        joinConfig.addValueField(new FieldDTO("age", DataTypes.LongType));
        joinConfig.addValueField(new FieldDTO("sex", DataTypes.StringType));

        numberOfJoinConfigFields = joinConfig.getKeyFields().size() + joinConfig.getValueFields().size();
    }

    private static PairFunction<Row, KeyDTO, ValueDTO> peoplePairFunction = new PairFunction<Row, KeyDTO, ValueDTO>() {
        public Tuple2<KeyDTO, ValueDTO> call(Row row) throws Exception {
            StructType schema = row.schema();
            Seq<StructField> seq = schema.seq();
            StructField[] fields = ((StructType) seq).fields();

            if (fields.length != numberOfJoinConfigFields) {
                throw new IllegalArgumentException("#Fields of row does not match #fields Join Config");
            }

            KeyDTO keyDTO = new KeyDTO();
            ValueDTO valueDTO = new ValueDTO();
            for (StructField field : fields) {
                String name = field.name();
                DataType dataType = field.dataType();
                Object fieldValue = row.getAs(name); // TODO: this will throw IllegalArgumentException if field
                // with name does not exist => set value after checking if field exists in Join config - BUT this
                // requires to do the "is key/value?" check again?

                boolean isKeyField = joinConfig.containsKeyField(name, dataType);
                boolean isValueField = joinConfig.containsValueField(name, dataType);

                if (isKeyField) {
                    KeyFieldDTO keyField = new KeyFieldDTO(name, dataType);
                    keyField.setValue(fieldValue);
                    keyDTO.addKeyField(keyField);
                } else if (isValueField) {
                    FieldDTO valueField = new FieldDTO(name, dataType);
                    valueField.setValue(fieldValue);
                    valueDTO.addValueField(valueField);
                } else {
                    System.err.println("ERROR: field " + name + " is not defined in Join config as neither key " +
                            "or value field");
                    // TODO: stop execution?
                    continue;
                }
            }

            Tuple2<KeyDTO, ValueDTO> tuple = new Tuple2<KeyDTO, ValueDTO>(keyDTO, valueDTO);
            return tuple;
        }
    };

    private static void doJoinWithPairRDDs(JavaRDD<Row> coolPeopleRDD, JavaRDD<Row> uncoolPeopleRDD) {
        JavaPairRDD<KeyDTO, ValueDTO> coolPeoplePairRDD = coolPeopleRDD.mapToPair(peoplePairFunction);
        // TODO: use coolPeoplePairRDD#aggregateByKey/reduceByKey/etc. to perform grouping?
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> coolPeoplePairGroupedRDD = coolPeoplePairRDD.groupByKey();
        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> coolPeoplePairGroupedCollected = coolPeoplePairGroupedRDD.collect();
        System.out.println("Cool, paired, grouped and collected people:");
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : coolPeoplePairGroupedCollected) {
            System.out.println(tuple);
        }

        JavaPairRDD<KeyDTO, ValueDTO> uncoolPeoplePairRDD = uncoolPeopleRDD.mapToPair(peoplePairFunction);
        // TODO: same as above
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> uncoolPeoplePairGroupedRDD = uncoolPeoplePairRDD.groupByKey();
        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> uncoolPeoplePairGroupedCollected = uncoolPeoplePairGroupedRDD
                .collect();
        System.out.println("Uncool, paired, grouped and collected people:");
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : uncoolPeoplePairGroupedCollected) {
            System.out.println(tuple);
        }

        JavaPairRDD<KeyDTO, Tuple2<Iterable<ValueDTO>, Iterable<ValueDTO>>> joinedPeople = coolPeoplePairGroupedRDD.join
                (uncoolPeoplePairGroupedRDD);
        List<Tuple2<KeyDTO, Tuple2<Iterable<ValueDTO>, Iterable<ValueDTO>>>> joinedAndCollectedPeople = joinedPeople.collect();
        System.out.println("JOINed people - with PairRDDs:");
        for (Tuple2<KeyDTO, Tuple2<Iterable<ValueDTO>, Iterable<ValueDTO>>> tuple : joinedAndCollectedPeople) {
            System.out.println(tuple);
        }

    }

    private static boolean fuzzyComparePeople(Row person1, Row person2) {
        // TODO: do some complicated, fuzzy matching logic, e.g. use Elasticsearch to calculate a Levenshtein distance
        boolean matchesUsingFuzzyLogic = false;
        return matchesUsingFuzzyLogic;
    }

    private static void doJoinWithRawDatasets(Dataset<Row> coolPeopleDataset, final Dataset<Row> uncoolPeopleDataset) {
        final List<Row> joinedPeopleDataset = new ArrayList<Row>();

        /*
            TODO: this is a BAD idea, right!?

            1. In a clustered/non-local environment, we cannot just make the inner ("uncool") and result data sets
            final: we have to send the inner data set as a broadcast variable to each node - but for big data sets
            this is not feasible, neither in terms of fitting it in memory or in terms of network traffic.

            2. Doing a "manual JOIN" this way does not allow Spark to use partitions, as it would with .join()?

            See e.g.:
            	- http://heather.miller.am/teaching/cs212/slides/week20.pdf, slide "Partitions"
	            - http://blog.cloudera.com/blog/2015/03/how-to-tune-your-apache-spark-jobs-part-1/

         */
        coolPeopleDataset.foreach(new ForeachFunction<Row>() {
            public void call(final Row coolPerson) throws Exception {
                // TODO: does not work: uncoolPeopleDataset is "Invalid tree; null:" (an NPE is thrown)!
                Dataset<Row> uncoolPeopleFiltered = uncoolPeopleDataset.filter(new FilterFunction<Row>() {
                    public boolean call(Row uncoolPerson) throws Exception {
                        return fuzzyComparePeople(coolPerson, uncoolPerson);
                    }
                });

                joinedPeopleDataset.addAll(uncoolPeopleFiltered.collectAsList());
            }
        });

        System.out.println("JOINed people - with raw Datasets:");
        for (Row row : joinedPeopleDataset) {
            System.out.println(row);
        }
    }

    private static void doJoinWithDatasetsAndSimpleUDF(Dataset<Row> coolPeopleDataset, Dataset<Row> uncoolPeopleDataset) {
        // TODO: how would this work with a dynamic number of attributes in the JOIN key? Use reflection to load the
        // appropriate UDF<X> class? Only up to 9 attributes are supported: is that ok?
        sqlContext.udf().register("simplePersonComparatorUDF", new UDF2<String, String, Boolean>() {
            public Boolean call(String str1, String str2) {
                return str1.equals(str2); // TODO: do actual comparison - of what exactly?
            }
        }, DataTypes.BooleanType);

        Column[] keyColumns = { new Column("cool.firstName"), new Column("uncool.firstName") };
        Column joinExpression = functions.callUDF("simplePersonComparatorUDF", keyColumns);

        // Use aliases to avoid "ambiguous column name" error
        Dataset<Row> joinedPeople = coolPeopleDataset.as("cool").join(uncoolPeopleDataset.as("uncool"),
                joinExpression, "inner");

        List<Row> joinedAndCollectedPeople = joinedPeople.collectAsList();
        System.out.println("JOINed people - with Datasets + simple UDF:");
        for (Row row : joinedAndCollectedPeople) {
            System.out.println(row);
        }
    }

    private static void doJoinWithDatasetsAndColumnCondition(Dataset<Row> coolPeopleDataset, final Dataset<Row> uncoolPeopleDataset) {
        sqlContext.udf().register("advancedPersonComparatorUDF", new UDF8<String, String, String, String, Long,
                Long, String, String, Boolean>() {
            public Boolean call(String coolFirstName, String uncoolFirstName, String coolLastName, String
                    uncoolLastName, Long coolAge, Long uncoolAge, String coolSex, String uncoolSex) throws Exception {
                boolean firstNamesMatch = coolFirstName.equals(uncoolFirstName);
                boolean lastNamesMatch = coolLastName.equals(uncoolLastName);
                boolean agesMatch = Math.abs(coolAge - uncoolAge) <= 3;
                boolean sexesMatch = coolSex.equals(uncoolSex);

                return firstNamesMatch && lastNamesMatch && agesMatch && sexesMatch;
            }
        }, DataTypes.BooleanType);

        Column[] keyColumns = {
                new Column("cool.firstName"), new Column("uncool.firstName"),
                new Column("cool.lastName"), new Column("uncool.lastName"),
                new Column("cool.age"), new Column("uncool.age"),
                new Column("cool.sex"), new Column("uncool.sex")
        };
        Column joinExpression = functions.callUDF("advancedPersonComparatorUDF", keyColumns);

        Dataset<Row> joinedPeople = coolPeopleDataset.as("cool").join(uncoolPeopleDataset.as("uncool"),
                joinExpression, "outer");
        List<Row> joinedAndCollectedPeople = joinedPeople.collectAsList();
        System.out.println("JOINed people - with Datasets + column condition:");
        for (Row row : joinedAndCollectedPeople) {
            System.out.println(row);
        }

    }

    public static void main(String[] args) {
        // JSON -> Datasets
        Dataset<Row> coolPeopleDataset = session.read().json("src/main/resources/spark/join/cool_people.json");
        Dataset<Row> uncoolPeopleDataset = session.read().json("src/main/resources/spark/join/uncool_people.json");

        // Datasets -> RDDs
        /*
        JavaRDD<Row> coolPeopleRDD = coolPeopleDataset.toJavaRDD();
        List<Row> coolPeopleCollected = coolPeopleRDD.collect();
        System.out.println("Cool, collected people:");
        printPeople(coolPeopleCollected);

        JavaRDD<Row> uncoolPeopleRDD = uncoolPeopleDataset.toJavaRDD();
        List<Row> uncoolPeopleCollected = uncoolPeopleRDD.collect();
        System.out.println("Uncool, collected people:");
        printPeople(uncoolPeopleCollected);
        */

        //doCartesianProduct(coolPeopleDataset, uncoolPeopleDataset);

        //initJoinConfig();

        //doJoinWithPairRDDs(coolPeopleRDD, uncoolPeopleRDD);

        //doJoinWithRawDatasets(coolPeopleDataset, uncoolPeopleDataset);

        //doJoinWithDatasetsAndSimpleUDF(coolPeopleDataset, uncoolPeopleDataset);

        doJoinWithDatasetsAndColumnCondition(coolPeopleDataset, uncoolPeopleDataset);

    }

}
