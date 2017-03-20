package basic;

public class EqualsTest {
    public static void main(String[] args) {
        Object o = new Object();
        boolean equals = o.equals(null);
        System.out.println(equals);

        double a = 0.2;
        double b = 0.7;
        double c = a + b;
        System.out.println("c " + c);
    }
}
