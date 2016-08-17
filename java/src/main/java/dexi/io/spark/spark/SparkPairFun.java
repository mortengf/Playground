package dexi.io.spark.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class SparkPairFun {

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

        List<Person> uncoolPeople = new ArrayList<Person>();
        Person donaldTrump = new Person("Donald", "Trump", 70, Sex.MALE);
        Person piaKjaersgaard = new Person("Pia", "Kjærsgaard", 69, Sex.FEMALE);
        Person uncoolSamHarris = new Person("Sam", "Harris", 50, Sex.MALE);

        uncoolPeople.add(donaldTrump);
        uncoolPeople.add(piaKjaersgaard);
        uncoolPeople.add(uncoolSamHarris);

        JavaRDD<Person> coolPeopleRDD = sc.parallelize(coolPeople);

    }

}
