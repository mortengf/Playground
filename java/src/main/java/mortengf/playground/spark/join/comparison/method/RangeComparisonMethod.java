package mortengf.playground.spark.join.comparison.method;

import mortengf.playground.spark.join.comparison.option.ComparisonOption;

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
