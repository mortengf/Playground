package dexi.io.spark.rl;

import dexi.io.spark.rl.dto.*;
import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import scala.collection.Seq;

import java.util.List;

public class SparkRecordLinkageFun {

    /*
    private static List<Tuple2<KeyDTO, ValueDTO>> defineKeyAndValues(Dataset<Row> people) {
        List<Tuple2<KeyDTO, ValueDTO>> tuples = new ArrayList<Tuple2<KeyDTO, ValueDTO>>();

        if (people != null) {
            for (Row person : people) {

            }
        }

        return tuples;
    }
    */

    /*
    private static MapFunction<Row, Tuple2<KeyDTO, ValueDTO>> defineKeyAndValues = new MapFunction<Row,
            Tuple2<KeyDTO, ValueDTO>>() {
        public Tuple2<KeyDTO, ValueDTO> call(Row person) throws Exception {
            Tuple2<KeyDTO, ValueDTO> result = null;
            // If person.<attr> == any KeyDTO.name then add person.<attr> and person.<attr>.<value> to KeyDTO
            // Else add  person.<attr> and person.<attr>.<value> to ValueDTO

            StructType schema = person.schema();
            if (schema != null) {
                //schema.
            }

            return result;
        }
    };
    */

    private enum Sex {
        MALE, FEMALE
    }

    private static class Person extends SpecificRecordBase implements SpecificRecord {
        private String firstName;
        private String lastName;
        private Integer age;
        private Sex sex;

        private Person(String firstName, String lastName, Integer age, Sex sex) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public Schema getSchema() {
            return null;
        }

        @Override
        public Object get(int i) {
            return null;
        }

        @Override
        public void put(int i, Object o) {

        }
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark RL Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        SparkSession session = SparkSession.builder().getOrCreate();
        Dataset<Row> coolPeopleDataset = session.read().json("src/main/resources/spark/rl/cool_people.json");
        Dataset<Row> uncoolPeopleDataset = session.read().json("src/main/resources/spark/rl/uncool_people.json");
        /*
        Dataset<Person> coolPeopleDataset = session.read().json("src/main/resources/spark/rl/cool_people.json").as
                (Encoders.bean(Person.class));
        Dataset<Person> uncoolPeopleDataset = session.read().json("src/main/resources/spark/rl/uncool_people.json").as
                (Encoders.bean(Person.class));
        */

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

        /* TODO:

            Perform record linkage that matches "cool" Sam Harris with "uncool" Sam Harris even though their age
            is different, ie. use the "selected comparison methods" and weights defined on the KeyFieldDTOs.

                1. Should the DTO classes contain only the schema or also the actual values?
                    A: it is probably easier to store them together for "processing" (e.g. JOIN and data comparison)
                    by Spark
                2. Which Spark data type to use: RDD or Dataset? Does a partition in an RDD represent a "group"/"block"
                with common keys - or is a partition purely an internal thing for controlling the number of tasks to
                parallelize operations in?
                    A: seems to be the latter based on e.g. http://spark.apache.org/docs/latest/programming-guide
                    .html#parallelized-collections
                3. How do we "load" the Dataset objects into the DTO classes (RL conf) such that we can define which
             keys to JOIN on and which comparison methods to use?
                4. Which Spark data structure and transformation supports such a JOIN with custom logic? Can we avoid a
             cartesian product (using "ds.join(otherDS)")?

         */

        /*
        KeyValueGroupedDataset<Tuple2<KeyDTO, ValueDTO>, Row> coolPeopleKeyValueGroupedDataset =
                coolPeopleDataset.groupByKey(defineKeyAndValues, null);// TODO: define Encoder how?
        */

        // TODO: can we use Spark's internal schema for our RL config?
        //StructType schema = coolPeopleDataset.schema();
        //coolPeopleDataset.printSchema();

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

        JavaPairRDD<KeyDTO, ValueDTO> coolPeoplePairRDD = coolPeopleRDD.mapToPair(peoplePairFunction);
        JavaPairRDD<KeyDTO, ValueDTO> uncoolPeoplePairRDD = uncoolPeopleRDD.mapToPair(peoplePairFunction);

        // TODO: use coolPeoplePairRDD#reduceByKey/aggregateByKey/etc. to perform pairing?
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> coolPeoplePairGroupedRDD = coolPeoplePairRDD.groupByKey();
        JavaPairRDD<KeyDTO, Iterable<ValueDTO>> uncoolPeoplePairGroupedRDD = uncoolPeoplePairRDD.groupByKey();

        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> coolPeoplePairGroupedCollected = coolPeoplePairGroupedRDD.collect();
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : coolPeoplePairGroupedCollected) {
            System.out.println("Cool, collected person: " + tuple);
        }
        List<Tuple2<KeyDTO, Iterable<ValueDTO>>> uncoolPeoplePairsCollected = uncoolPeoplePairGroupedRDD.collect();
        for (Tuple2<KeyDTO, Iterable<ValueDTO>> tuple : uncoolPeoplePairsCollected) {
            System.out.println("Uncool, collected person: " + tuple);
        }

        System.out.println("Key-Valuing (pairing) done");

        System.out.println("Performing RL...");
        JavaPairRDD<KeyDTO, Tuple2<ValueDTO, ValueDTO>> joined = coolPeoplePairRDD.join(uncoolPeoplePairRDD);
        joined.foreach(new VoidFunction<Tuple2<KeyDTO, Tuple2<ValueDTO, ValueDTO>>>() {
            public void call(Tuple2<KeyDTO, Tuple2<ValueDTO, ValueDTO>> tuple) throws Exception {
                System.out.println(tuple);
            }
        });
        System.out.println("RL done");


        /*
        Object cartesianProductCollected = cartesianProduct.collect();

        Dataset<Row> sumSet = coolPeopleDataset.groupBy("firstName").sum("age");
        Object collect = sumSet.collect();
        */

        // -----------------

        //RLConfig recordDTO = new RLConfig();

        //FieldDTO key = new FieldDTO("firstName", "String", "Elon");
        //FieldDTO lastName = new FieldDTO("lastName", "String", "Musk");

        //recordDTO.addField(lastName);
        //recordDTO.addField(age);
        //recordDTO.addField(sex);

        //MapType PersonRecordType = DataTypes.createMapType(recordDTO.getKeyFields(), recordDTO);

        // Because we are in Java, not Scala, we have to create the UDF in a bit annoying way, c.f.
        // c.f. http://stackoverflow.com/questions/36248206/creating-a-sparksql-udf-in-java-outside-of-sqlcontext
        // TODO: write the UDF in Scala and call it from Java?
        /*
        UserDefinedFunction personComparatorUDF = functions.udf(personComparator, typeTagRT, typeTagA1, typeTagA2, ...);
        sqlContext.udf().register("personComparatorUDF",
                new UDF4<Object, Object, Object, Object, Object>() {
                    public Object call(Object o, Object o2, Object o3, Object o4) throws Exception {
                        return null;
                    }
                }, PersonRecordType);
        */

        //scala.collection.Seq<Column> keyColumns = new scala.collection.GenSeq();

        //Column joinExpression = functions.callUDF("personComparatorUDF", keyColumns);
        //Dataset<Row> linkedPeople = coolPeopleDataset.join(uncoolPeopleDataset, joinExpression, "outer");
        //linkedPeople.show();

    }

}
