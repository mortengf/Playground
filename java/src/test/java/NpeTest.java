import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
    }

}
