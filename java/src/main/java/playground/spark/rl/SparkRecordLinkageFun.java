package playground.spark.rl;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
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

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark RL Fun").setMaster("local");

        // A (Java)SparkContext must be instantiated even though the object is never used - otherwise the program
        // will throw a "SparkException: A master URL must be set in your configuration"
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        SparkSession session = SparkSession.builder().getOrCreate();
        Dataset<Row> coolPeopleDataset = session.read().json("src/main/resources/spark/rl/cool_people.json");
        Dataset<Row> uncoolPeopleDataset = session.read().json("src/main/resources/spark/rl/uncool_people.json");

        VoidFunction<Row> printPeopleRow = new VoidFunction<Row>() {
            public void call(Row row) throws Exception {
                System.out.println(row);
            }
        };

        JavaRDD<Row> coolPeopleRDD = coolPeopleDataset.toJavaRDD();
        System.out.println("Cool people...");
        coolPeopleRDD.foreach(printPeopleRow);
        JavaRDD<Row> uncoolPeopleRDD = uncoolPeopleDataset.toJavaRDD();
        System.out.println("Uncool people...");
        uncoolPeopleRDD.foreach(printPeopleRow);

        // Cartesian product
        System.out.println("Performing Cartesian product...");
        Dataset<Row> cartesianProduct = coolPeopleDataset.join(uncoolPeopleDataset);
        JavaRDD<Row> cartesianProductRDD = cartesianProduct.toJavaRDD();
        cartesianProductRDD.foreach(printPeopleRow);
        System.out.println("Cartesian product done");

        // RL: JOIN with pairs

        // Statically define RL config
        // TODO: this must be possible to define dynamically
        final RLConfig rlConfig = new RLConfig();

        rlConfig.addKeyField(new KeyFieldDTO("firstName", DataTypes.StringType));
        rlConfig.addKeyField(new KeyFieldDTO("lastName", DataTypes.StringType));

        rlConfig.addValueField(new FieldDTO("age", DataTypes.LongType));
        rlConfig.addValueField(new FieldDTO("sex", DataTypes.StringType));

        final int numberOfRLConfigFields = rlConfig.getKeyFields().size() + rlConfig.getValueFields().size();

        System.out.println("Key-Valuing (pairing) each data set...");
        PairFunction<Row, KeyDTO, ValueDTO> peoplePairFunction = new PairFunction<Row, KeyDTO, ValueDTO>() {
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

        // Define what a "people pair" is, i.e. the key and value
        JavaPairRDD<KeyDTO, ValueDTO> coolPeoplePairRDD = coolPeopleRDD.mapToPair(peoplePairFunction);
        JavaPairRDD<KeyDTO, ValueDTO> uncoolPeoplePairRDD = uncoolPeopleRDD.mapToPair(peoplePairFunction);

        // Do the actual pairing/grouping
        // TODO: use coolPeoplePairRDD#aggregateByKey/reduceByKey/etc. to perform grouping?
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> coolPeoplePairGroupedRDD = coolPeoplePairRDD.groupByKey();
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> uncoolPeoplePairGroupedRDD = uncoolPeoplePairRDD.groupByKey();

        // Collect the results before printing to ensure that all nodes have finished processing
        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> coolPeoplePairGroupedCollected = coolPeoplePairGroupedRDD.collect();
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : coolPeoplePairGroupedCollected) {
            System.out.println("Cool, collected person: " + tuple);
        }
        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> uncoolPeoplePairsCollected = uncoolPeoplePairGroupedRDD.collect();
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : uncoolPeoplePairsCollected) {
            System.out.println("Uncool, collected person: " + tuple);
        }

        System.out.println("Key-Valuing (pairing) done");

        System.out.println("Performing RL with PairRDDs...");
        JavaPairRDD<KeyDTO, Tuple2<ValueDTO, ValueDTO>> joined = coolPeoplePairRDD.join(uncoolPeoplePairRDD);
        joined.foreach(new VoidFunction<Tuple2<KeyDTO, Tuple2<ValueDTO, ValueDTO>>>() {
            public void call(Tuple2<KeyDTO, Tuple2<ValueDTO, ValueDTO>> tuple) throws Exception {
                System.out.println(tuple);
            }
        });
        System.out.println("RL with PairRDDs done");

        System.out.println("Performing RL with Datasets + UDF...");
        sqlContext.udf().register("personComparatorUDF", new UDF2<String, String, Boolean>() {
            public Boolean call(String str1, String str2) {
                return str1.equals(str2); // TODO: do actual comparison - of what exactly?
            }
        }, DataTypes.BooleanType);

        Column[] keyColumns = { new Column("cool.firstName"), new Column("uncool.firstName") };
        Column joinExpression = functions.callUDF("personComparatorUDF", keyColumns);

        Dataset<Row> linkedPeople = coolPeopleDataset.as("cool").join(uncoolPeopleDataset.as("uncool"),
                joinExpression, "inner");

        List<Row> linkedAndCollectedPeople = linkedPeople.collectAsList();
        for (Row row : linkedAndCollectedPeople) {
            System.out.println(row);
        }
        System.out.println("RL with Datasets + UDF done");

    }

}
