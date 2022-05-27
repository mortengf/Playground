package mortengf.playground.basic;

interface Fieldable {
    int i = 100;
}

public class FieldTester implements Fieldable {

    public static void main(String[] args) {
        System.out.println("i: " + i);
    }
}