package mortengf.playground.basic;

public class FormatterTest {
    public static void main(String[] args) {
        String logMessage = String.format("Expiration date %s for id %s passed. Failing it.", "2018-07-01", 123132);
        System.out.println(logMessage);
    }
}
