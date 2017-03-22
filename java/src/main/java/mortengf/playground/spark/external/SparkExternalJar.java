package mortengf.playground.spark.external;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.ArrayList;
import java.util.List;

public class SparkExternalJar {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark External Jar Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<String> testData = new ArrayList<String>();
        testData.add("Peter");
        testData.add("James");
        testData.add("John");

        JavaRDD<String> testDataRDD = sc.parallelize(testData);

        JavaRDD<String> mappedTestDataRDD = testDataRDD.map(new Function<String, String>() {
            public String call(String v1) throws Exception {
                return StringUtils.upperCase(v1);
            }
        });

        List<String> collected = mappedTestDataRDD.collect();
        for (String s : collected) {
            System.out.println(s);
        }


    }
}
