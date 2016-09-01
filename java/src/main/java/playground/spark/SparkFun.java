package playground.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Code from http://spark.apache.org/docs/latest/programming-guide.html
 *
 */
public class SparkFun {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Spark Fun").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distNumbers = sc.parallelize(numbers);

        Integer reduced = distNumbers.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        });

        System.out.println("Reduced: " + reduced);

        JavaRDD<String> distFileLines = sc.textFile("src/main/resources/spark/randomdata.txt");
        JavaRDD<Object> mappedData = distFileLines.map(new Function<String, Object>() {
            public Object call(String s) throws Exception {
                return "Line: " + s;
            }
        });

        List<Object> collectedData = mappedData.collect();
        for (Object o : collectedData) {
            System.out.println("Collected: " + o);
        }

        /*
        JavaPairRDD<String, Integer> pairs = distFileLines.compare(new PairFunction<String, String, Integer>() {
            public Tuple2 call(DataSetEntryDTO a, DataSetEntryDTO b) throws Exception {
                if (RecordLinkageMagic.isSame(a, b)) {
                    return RecordLinkageMagic.merge(a, b);
                }

                return null;
            }
        });
        */

        JavaPairRDD<String, Integer> pairs = distFileLines.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2 call(String s) throws Exception {
                return new Tuple2(s, 1);
            }
        });

        JavaPairRDD<String, Integer> counts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer value1, Integer value2) throws Exception {
                return value1 + value2;
            }
        });

        pairs.persist(StorageLevel.MEMORY_ONLY_SER());

        counts.sortByKey();

        List<Tuple2<String, Integer>> firstXCounts = counts.take(10); // Instead of collect()ing all elements

        for (Tuple2<String, Integer> count : firstXCounts) {
            System.out.println("Count: " + count);
        }

        Broadcast<int[]> broadcastVar = sc.broadcast(new int[] {1, 2, 3});

        System.out.println("Broadcasted value length: " + broadcastVar.value().length);

        final LongAccumulator accum = sc.sc().longAccumulator();

        sc.parallelize(Arrays.asList(1, 2, 3, 4)).foreach(new VoidFunction<Integer>() {
            public void call(Integer i) throws Exception {
                accum.add(i + 100);
            }
        });

        System.out.println("Accumulated value: " + accum.value());

    }
}
