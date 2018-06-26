package mortengf.playground.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConcurrencyTest {

    private final static Map<String, List<String>> map = new HashMap();

    public static void main(String[] args) {
        Random random = new Random();
        for (int i=0; i<10; i++) {
            TestRunner testRunner = new TestRunner(map, "key", random.nextBoolean());
            Thread thread = new Thread(testRunner);
            thread.start();
        }
    }
}

class TestRunner implements Runnable {

    private Map<String, List<String>> map;

    private String key;
    private boolean shouldAdd;

    public TestRunner(Map<String, List<String>> map, String key, boolean shouldAdd) {
        this.map = map;
        this.key = key;
        this.shouldAdd = shouldAdd;
    }

    public boolean canAddValueToListForKey() {
        final List<String> valuesForKey = map.get(key);
        if (valuesForKey == null) {
            return false;
        }

        synchronized (valuesForKey) {
            valuesForKey.add("value");
            return true;
        }
    }

    private boolean canRemoveValueForKey() {
        final List<String> valuesForKey = map.get(key);
        if (valuesForKey == null) {
            return false;
        }

        synchronized (valuesForKey) {
            valuesForKey.remove("value");
            return true;
        }
    }

    public void run() {
        while (true) {
            if (shouldAdd) {
                if (canAddValueToListForKey()) {
                    System.out.println("Could add value for key");
                }
            } else {
                if (canRemoveValueForKey()) {
                    System.out.println("Could remove value for key - or key was not present => OK to modify values");
                }
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
