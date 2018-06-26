package mortengf.playground.basic;

import java.math.BigDecimal;

public class DecimalTest {
    public static void main(String[] args) {
        System.out.println(String.format("%.2f", 1.235999));
        System.out.println(String.format("%.2f", new BigDecimal((double) 237823277 / 3600)));

    }
}
