package mortengf.playground.basic;

import org.apache.commons.lang.math.NumberUtils;

public class NumberUtilsTest {
    public static void main(String[] args) {
        String number = "2.5L.";
        boolean isNumber = NumberUtils.isNumber(number);
        System.out.println(isNumber);
        if (isNumber) {
            NumberUtils.createNumber(number);
            System.out.println("Created number: " + number);
        }

    }
}
