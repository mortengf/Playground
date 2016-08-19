package dexi.io.spark.rl;

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
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

public class SparkRecordLinkageFun {

    /*
    private static List<Tuple2<KeyFieldDTO, ValueFieldDTO>> defineKeyAndValues(Dataset<Row> people) {
        List<Tuple2<KeyFieldDTO, ValueFieldDTO>> tuples = new ArrayList<Tuple2<KeyFieldDTO, ValueFieldDTO>>();

        if (people != null) {
            for (Row person : people) {

            }
        }

        return tuples;
    }
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

        JavaRDD<Row> coolPeopleRDD = coolPeopleDataset.toJavaRDD();
        JavaRDD<Row> uncoolPeopleRDD = uncoolPeopleDataset.toJavaRDD();

        /* TODO:

            Perform record linkage that matches "cool" Sam Harris with "uncool" Sam Harris even though their age
            is different, ie. use the "selected comparison methods" and weights defined on the KeyFieldDTOs.

                1. Should the DTO classes contain only the schema or also the actual values?
                    A: it is probably easier to store them together for "processing" (e.g. JOIN and data comparison)
                    by Spark
                2. Which Spark data type to use: RDD or Dataset? Does a partition in an RDD represent a "group"/"block"
                with common keys - or is a partition purely an internal thing for controlling the number of tasks to
                parallelize operations in?
                    A: seems to be the ladder based on e.g. http://spark.apache.org/docs/latest/programming-guide.html#parallelized-collections
                3. How do we "load" the Dataset objects into the DTO classes (RL conf) such that we can define which
             keys to JOIN on and which comparison methods to use?
                4. Which Spark data structure and transformation supports such a JOIN with custom logic? Can we avoid a
             cartesian product (using "ds.join(otherDS)")?

         */

        /*
        MapFunction<Row, Tuple2<KeyFieldDTO, ValueFieldDTO>> defineKeyAndValues = new MapFunction<Row, Tuple2<KeyFieldDTO, ValueFieldDTO>>() {
            public Tuple2<KeyFieldDTO, ValueFieldDTO> call(Row person) throws Exception {
                Tuple2<KeyFieldDTO, ValueFieldDTO> result = null;
                // If person.<attr> == any KeyFieldDTO.name then add person.<attr> and person.<attr>.<value> to KeyFieldDTO
                // Else add  person.<attr> and person.<attr>.<value> to ValueFieldDTO

                StructType schema = person.schema();
                if (schema != null) {
                    //schema.
                }

                return result;
            }
        };
        */

        /*
        KeyValueGroupedDataset<Tuple2<KeyFieldDTO, ValueFieldDTO>, Row> coolPeopleKeyValueGroupedDataset =
                coolPeopleDataset.groupByKey(defineKeyAndValues, null);// TODO: define Encoder how?
        */

        StructType schema = coolPeopleDataset.schema();
        coolPeopleDataset.printSchema();

        // Cartesian product
        System.out.println("Cartesian product...");
        Dataset<Row> cartesianProduct = coolPeopleDataset.join(uncoolPeopleDataset);
        JavaRDD<Row> cartesianProductRDD = cartesianProduct.toJavaRDD();
        cartesianProductRDD.foreach(new VoidFunction<Row>() {
            public void call(Row row) throws Exception {
                System.out.println(row);
            }
        });

        // JOIN - Pair
        System.out.println("JOIN - Pair...");
        PairFunction<Row, Object, Object> peoplePairFunction = new PairFunction<Row, Object, Object>() {
            public Tuple2<Object, Object> call(Row row) throws Exception {
                return new Tuple2<Object, Object>(row.get(1), row.get(2));
            }
        };

        JavaPairRDD<Object, Object> coolPeoplePairRDD = coolPeopleRDD.mapToPair(peoplePairFunction);
        JavaPairRDD<Object, Object> uncoolPeoplePairRDD = uncoolPeopleRDD.mapToPair(peoplePairFunction);

        JavaPairRDD<Object, Tuple2<Object, Object>> joined = coolPeoplePairRDD.join(uncoolPeoplePairRDD);
        joined.foreach(new VoidFunction<Tuple2<Object, Tuple2<Object, Object>>>() {
            public void call(Tuple2<Object, Tuple2<Object, Object>> tuple) throws Exception {
                System.out.println(tuple);
            }
        });


        /*
        Object cartesianProductCollected = cartesianProduct.collect();

        Dataset<Row> sumSet = coolPeopleDataset.groupBy("firstName").sum("age");
        Object collect = sumSet.collect();
        */

        // -----------------

        //RecordDTO recordDTO = new RecordDTO();

        //FieldDTO key = new FieldDTO("firstName", "String", "Elon");
        //FieldDTO lastName = new FieldDTO("lastName", "String", "Musk");

        //recordDTO.addField(lastName);
        //recordDTO.addField(age);
        //recordDTO.addField(sex);

        //MapType PersonRecordType = DataTypes.createMapType(recordDTO.getKeys(), recordDTO);

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
