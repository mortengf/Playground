package basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NpeTest {
    private static final Logger log = LogManager.getLogger(NpeTest.class);

    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (Exception e) {
            if (e.getMessage() != null &&
                    e.getMessage().contains("duplicate entry")) {
                System.out.println("continue; //Ignore duplicates");
            }
            log.error("Caught NullPointerException: ", e);
        }

        String formatted = String.format("%s", null);
        System.out.println(formatted);

        Boolean b = null;
        if (b != false) {
            System.out.println("Should not get here due to NPE");
        }
        //throw new NullPointerException();
    }

}
