package playground.spark.row;

import playground.spark.join.comparison.option.ComparisonOption;

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
    public Double compare(Object v1, Object v2) {
        return 0.0;
    }
}
