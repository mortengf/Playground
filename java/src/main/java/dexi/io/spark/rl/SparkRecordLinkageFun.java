package dexi.io.spark.rl;

import dexi.io.spark.rl.dto.KeyFieldDTO;
import dexi.io.spark.rl.dto.ValueFieldDTO;
import org.apache.avro.ipc.specific.Person;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class SparkRecordLinkageFun {

    private static List<Tuple2<KeyFieldDTO, ValueFieldDTO>> defineKeyAndValues(List<Person> people) {
        List<Tuple2<KeyFieldDTO, ValueFieldDTO>> tuples = new ArrayList<Tuple2<KeyFieldDTO, ValueFieldDTO>>();

        return tuples;
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark RL Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession session = SparkSession.builder().getOrCreate();
        Dataset<Row> coolPeople = session.read().json("src/main/resources/spark/rl/cool_people.json");
        Dataset<Row> uncoolPeople = session.read().json("src/main/resources/spark/rl/uncool_people.json");

        /* TODO:

            Perform record linkage that matches "cool" Sam Harris with "uncool" Sam Harris even though their age
            is different, ie. use the "selected comparison methods" and weights defined on the KeyFieldDTOs.

             1. How do we "load" the Dataset objects into the DTO classes (RL conf) such that we can define which
             keys to JOIN on and which comparison methods to use?
             2. Which Spark data structure and transformation supports such a JOIN with custom logic? Can we avoid a
             cartesian product (using "ds.join(otherDS)")?

         */
        Dataset<Row> cartesianProduct = coolPeople.join(uncoolPeople);
        Object cartesianProductCollected = cartesianProduct.collect();

        MapFunction<KeyFieldDTO, ValueFieldDTO> keyPeople = new MapFunction<KeyFieldDTO, ValueFieldDTO>() {
            public ValueFieldDTO call(KeyFieldDTO value) throws Exception {
                return null;
            }
        };

        Dataset<Row> sumSet = coolPeople.groupBy("firstName").sum("age");
        Object collect = sumSet.collect();

        // TODO: convert "coolPeople" collection into RecordDTO/Tuple2 how? Implement PairFunction,
        // c.f. https://www.safaribooksonline.com/library/view/learning-spark/9781449359034/ch04.html ?
        // UPDATE: we will not be able to use PairFunction for RL across data sets because it only works on a single
        // data set?
        /*
        PairFunction<Person, KeyFieldDTO, ValueFieldDTO> keyData =
            new PairFunction<Person, KeyFieldDTO, ValueFieldDTO>() {
                public Tuple2<KeyFieldDTO, ValueFieldDTO> call(Person person) throws Exception {
                    return null;
                }

        };
        */

    }

}
