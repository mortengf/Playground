package dexi.io.spark.rl;

import dexi.io.spark.rl.dto.FieldDTO;
import dexi.io.spark.rl.dto.KeyFieldDTO;
import dexi.io.spark.rl.dto.RecordDTO;
import dexi.io.spark.rl.dto.ValueFieldDTO;
import org.apache.avro.ipc.specific.Person;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.api.java.UDF3;
import org.apache.spark.sql.api.java.UDF4;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.MapType;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import scala.collection.IndexedSeq;
import scala.collection.Seq;
import scala.reflect.ClassTag;

import java.util.ArrayList;
import java.util.List;

public class SparkRecordLinkageFun {

    private static List<Tuple2<KeyFieldDTO, ValueFieldDTO>> defineKeyAndValues(Dataset<Row> people) {
        List<Tuple2<KeyFieldDTO, ValueFieldDTO>> tuples = new ArrayList<Tuple2<KeyFieldDTO, ValueFieldDTO>>();

        if (people != null) {
            for (Row person : people) {

            }
        }

        return tuples;
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark RL Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        SparkSession session = SparkSession.builder().getOrCreate();
        Dataset<Row> coolPeopleDataset = session.read().json("src/main/resources/spark/rl/cool_people.json");
        Dataset<Row> uncoolPeopleDataset = session.read().json("src/main/resources/spark/rl/uncool_people.json");

        /* TODO:

            Perform record linkage that matches "cool" Sam Harris with "uncool" Sam Harris even though their age
            is different, ie. use the "selected comparison methods" and weights defined on the KeyFieldDTOs.

             1. How do we "load" the Dataset objects into the DTO classes (RL conf) such that we can define which
             keys to JOIN on and which comparison methods to use?
             2. Which Spark data structure and transformation supports such a JOIN with custom logic? Can we avoid a
             cartesian product (using "ds.join(otherDS)")?

         */

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

        KeyValueGroupedDataset<Tuple2<KeyFieldDTO, ValueFieldDTO>, Row> coolPeopleKeyValueGroupedDataset = coolPeopleDataset.groupByKey(defineKeyAndValues, null);// TODO: define Encoder how?

        Dataset<Row> cartesianProduct = coolPeopleDataset.join(uncoolPeopleDataset);
        Object cartesianProductCollected = cartesianProduct.collect();

        Dataset<Row> sumSet = coolPeopleDataset.groupBy("firstName").sum("age");
        Object collect = sumSet.collect();

        // -----------------

        RecordDTO recordDTO = new RecordDTO();

        FieldDTO key = new FieldDTO("firstName", "String", "Elon");
        FieldDTO lastName = new FieldDTO("lastName", "String", "Musk");

        recordDTO.addField(lastName);
        recordDTO.addField(age);
        recordDTO.addField(sex);

        MapType PersonRecordType = DataTypes.createMapType(recordDTO.getKeys(), recordDTO);

        // Because we are in Java, not Scala, we have to create the UDF in a bit annoying way, c.f.
        // c.f. http://stackoverflow.com/questions/36248206/creating-a-sparksql-udf-in-java-outside-of-sqlcontext
        // TODO: write the UDF in Scala and call it from Java?
        //UserDefinedFunction personComparatorUDF = functions.udf(personComparator, typeTagRT, typeTagA1, typeTagA2, ...);
        sqlContext.udf().register("personComparatorUDF",
                new UDF4<Object, Object, Object, Object, Object>() {
                    public Object call(Object o, Object o2, Object o3, Object o4) throws Exception {
                        return null;
                    }
                }, PersonRecordType);

        scala.collection.Seq<Column> keyColumns = new IndexedSeq<Column>() {
        };

        Column joinExpression = functions.callUDF("personComparatorUDF", keyColumns);

        coolPeopleDataset.join(uncoolPeopleDataset, joinExpression);

    }

}
