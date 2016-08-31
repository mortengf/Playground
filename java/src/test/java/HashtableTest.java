import java.util.Hashtable;

/**
 * See http://stackoverflow.com/questions/26287852/java-hashtable-stores-multiple-items-with-same-hash-in-one-bucket
 *
 */
public class HashtableTest {

    private static class Tester {
        private Long value;

        public Tester(Long value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tester tester = (Tester) o;

            return value.equals(tester.value);

        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public String toString() {
            return "Tester{" +
                    "value=" + value +
                    '}';
        }
    }

    public static void main(String[] args) {
        Hashtable<Tester, String> testMap = new Hashtable<Tester, String>();

        Tester normalOne = new Tester(1L);
        Tester crazyOne = new Tester(2L);

        testMap.put(normalOne, "normal one");
        testMap.put(crazyOne, "crazy one");

        String result = testMap.get(crazyOne);
        System.out.println(result);
    }
}
