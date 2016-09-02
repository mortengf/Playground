package playground.spark.rl;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import playground.spark.rl.dto.FieldDTO;
import playground.spark.rl.dto.KeyDTO;
import playground.spark.rl.dto.KeyFieldDTO;
import playground.spark.rl.dto.ValueDTO;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.List;

/**
 Perform Record Linkage (RL) (using statically-defined (key/value) config) that matches:
 <ul>
 <li>"Cool" "Sam Harris" with "uncool" "Sam Harris" even though their age is different, ie. use the
 "selected comparison methods" and weights defined on the KeyFieldDTOs.</li>
 <li>Multiple "Elon Musk"s within the same data set.</li>
 </ul>

 TODO: support the "selected comparison methods" and weights. Which Spark data structure and transformation
 supports such a JOIN with custom logic? Can we avoid a cartesian product (using "rdd.join(otherRDD)")?
        - Use RDD.cogroup()?
        - Call data comparison/cleaning methods from equals() of DTO classes? If we do that, we won't be able to
 implement something like "how many percent"-ish values match, because equals() returns boolean
*/
public class SparkRecordLinkageFun {
    private static SparkSession session;
    private static SQLContext sqlContext;

    private static RLConfig rlConfig;
    private static int numberOfRLConfigFields;

    static {
        SparkConf conf = new SparkConf().setAppName("Spark RL Fun").setMaster("local");

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

    private static void initRLConfig() {
        rlConfig = new RLConfig();

        // TODO: this must be possible to define dynamically
        rlConfig.addKeyField(new KeyFieldDTO("firstName", DataTypes.StringType));
        rlConfig.addKeyField(new KeyFieldDTO("lastName", DataTypes.StringType));

        rlConfig.addValueField(new FieldDTO("age", DataTypes.LongType));
        rlConfig.addValueField(new FieldDTO("sex", DataTypes.StringType));

        numberOfRLConfigFields = rlConfig.getKeyFields().size() + rlConfig.getValueFields().size();
    }

    private static PairFunction<Row, KeyDTO, ValueDTO> peoplePairFunction = new PairFunction<Row, KeyDTO, ValueDTO>() {
        public Tuple2<KeyDTO, ValueDTO> call(Row row) throws Exception {
            StructType schema = row.schema();
            Seq<StructField> seq = schema.seq();
            StructField[] fields = ((StructType) seq).fields();

            if (fields.length != numberOfRLConfigFields) {
                throw new IllegalArgumentException("#Fields of row does not match #fields RL Config");
            }

            KeyDTO keyDTO = new KeyDTO();
            ValueDTO valueDTO = new ValueDTO();
            for (StructField field : fields) {
                String name = field.name();
                DataType dataType = field.dataType();
                Object fieldValue = row.getAs(name); // TODO: this will throw IllegalArgumentException if field
                // with name does not exist => set value after checking if field exists in RL config - BUT this
                // requires to do the "is key/value?" check again?

                boolean isKeyField = rlConfig.containsKeyField(name, dataType);
                boolean isValueField = rlConfig.containsValueField(name, dataType);

                if (isKeyField) {
                    KeyFieldDTO keyField = new KeyFieldDTO(name, dataType);
                    keyField.setValue(fieldValue);
                    keyDTO.addKeyField(keyField);
                } else if (isValueField) {
                    FieldDTO valueField = new FieldDTO(name, dataType);
                    valueField.setValue(fieldValue);
                    valueDTO.addValueField(valueField);
                } else {
                    System.err.println("ERROR: field " + name + " is not defined in RL config as neither key " +
                            "or value field");
                    // TODO: stop execution?
                    continue;
                }
            }

            Tuple2<KeyDTO, ValueDTO> tuple = new Tuple2<KeyDTO, ValueDTO>(keyDTO, valueDTO);
            return tuple;
        }
    };

    private static void doRLWithPairRDDs(JavaRDD<Row> coolPeopleRDD, JavaRDD<Row> uncoolPeopleRDD) {
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
        System.out.println("RLed/JOINed people - with PairRDDs:");
        for (Tuple2<KeyDTO, Tuple2<Iterable<ValueDTO>, Iterable<ValueDTO>>> tuple : joinedAndCollectedPeople) {
            System.out.println(tuple);
        }

    }

    private static void doRLWithDatasetsAndUDF(Dataset<Row> coolPeopleDataset, Dataset<Row> uncoolPeopleDataset) {
        sqlContext.udf().register("personComparatorUDF", new UDF2<String, String, Boolean>() {
            public Boolean call(String str1, String str2) {
                return str1.equals(str2); // TODO: do actual comparison - of what exactly?
            }
        }, DataTypes.BooleanType);

        Column[] keyColumns = { new Column("cool.firstName"), new Column("uncool.firstName") };
        Column joinExpression = functions.callUDF("personComparatorUDF", keyColumns);

        Dataset<Row> joinedPeople = coolPeopleDataset.as("cool").join(uncoolPeopleDataset.as("uncool"),
                joinExpression, "inner");

        List<Row> joinedAndCollectedPeople = joinedPeople.collectAsList();
        System.out.println("RLed/JOINed people - with Datasets + UDF:");
        for (Row row : joinedAndCollectedPeople) {
            System.out.println(row);
        }
    }

    public static void main(String[] args) {
        // JSON -> Datasets
        Dataset<Row> coolPeopleDataset = session.read().json("src/main/resources/spark/rl/cool_people.json");
        Dataset<Row> uncoolPeopleDataset = session.read().json("src/main/resources/spark/rl/uncool_people.json");

        // Datasets -> RDDs
        JavaRDD<Row> coolPeopleRDD = coolPeopleDataset.toJavaRDD();
        List<Row> coolPeopleCollected = coolPeopleRDD.collect();
        System.out.println("Cool, collected people:");
        printPeople(coolPeopleCollected);

        JavaRDD<Row> uncoolPeopleRDD = uncoolPeopleDataset.toJavaRDD();
        List<Row> uncoolPeopleCollected = uncoolPeopleRDD.collect();
        System.out.println("Uncool, collected people:");
        printPeople(uncoolPeopleCollected);

        doCartesianProduct(coolPeopleDataset, uncoolPeopleDataset);

        initRLConfig();

        doRLWithPairRDDs(coolPeopleRDD, uncoolPeopleRDD);

        doRLWithDatasetsAndUDF(coolPeopleDataset, uncoolPeopleDataset);

    }

}
