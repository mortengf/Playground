package dexi.io.spark.rl;

import dexi.io.spark.rl.dto.KeyFieldDTO;
import dexi.io.spark.rl.dto.ValueFieldDTO;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class SparkRecordLinkageFun {

    private enum Sex {
        MALE, FEMALE
    }

    private static class Person {
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
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Person> coolPeople = new ArrayList<Person>();
        Person elonMusk = new Person("Elon", "Musk", 45, Sex.MALE);
        Person soerenUlrikThomsen = new Person("Søren", "Ulrik Thomsen", 60, Sex.MALE);
        Person coolSamHarris = new Person("Sam", "Harris", 49, Sex.MALE);

        coolPeople.add(elonMusk);
        coolPeople.add(soerenUlrikThomsen);
        coolPeople.add(coolSamHarris);

        // TODO: convert "coolPeople" collection into RecordDTO/Tuple2 how? Implement PairFunction,
        // c.f. https://www.safaribooksonline.com/library/view/learning-spark/9781449359034/ch04.html ?
        // UPDATE: we will not be able to use PairFunction for RL across data sets because it only works on a single
        // data set?
        PairFunction<KeyFieldDTO, KeyFieldDTO, Tuple2> keyData =
            new PairFunction<KeyFieldDTO, KeyFieldDTO, Tuple2>() {
                public Tuple2<String, String> call(String x) {
                    return new Tuple2(x.split(" ")[0], x);
                }
        };

        JavaRDD<Person> coolPeopleRDD = sc.parallelize(coolPeople);
        List<Tuple2<KeyFieldDTO, ValueFieldDTO>> coolPeopleTuples = coolPeopleRDD.mapToPair(keyData);
        JavaPairRDD<KeyFieldDTO, ValueFieldDTO> coolPeoplePairs = sc.parallelizePairs(coolPeopleTuples);

        // TODO: what does "coolPeoplePairs" contain?

        List<Person> uncoolPeople = new ArrayList<Person>();
        Person donaldTrump = new Person("Donald", "Trump", 70, Sex.MALE);
        Person piaKjaersgaard = new Person("Pia", "Kjærsgaard", 69, Sex.FEMALE);
        Person uncoolSamHarris = new Person("Sam", "Harris", 50, Sex.MALE);

        uncoolPeople.add(donaldTrump);
        uncoolPeople.add(piaKjaersgaard);
        uncoolPeople.add(uncoolSamHarris);

        // TODO: perform record linkage that matches "cool" Sam Harris with "uncool" Sam Harris even though their age
        // is different

    }

}
