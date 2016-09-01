package playground.spark.rl.comparison.method;

import playground.spark.rl.comparison.option.ComparisonOption;

public class RangeComparisonMethod extends ComparisonMethod {

    public RangeComparisonMethod() { }

    public RangeComparisonMethod(ComparisonOption option) {
        super();
        options.add(option);
    }

    public void addComparisonOption(ComparisonOption option) {
        options.add(option);
    }

    @Override
    public boolean compare() {
        // TODO: loop over comparison options
        return false;
    }
}
