package dexi.io.spark.rl.comparison;

import java.util.ArrayList;

public class RangeComparison extends ComparisonMethod {
    public RangeComparison(double negative, double positive) {
        options = new ArrayList<ComparisonOption>();
        options.add(new ComparisonOption("-" + String.valueOf(negative) + ":" + "+" + String.valueOf(positive)));
    }

    @Override
    public boolean compare() {
        // TODO: loop over comparison options
        return false;
    }
}
