package mortengf.playground.spark.join.comparison.option;

public class RangeComparisonOption extends ComparisonOption {

    public RangeComparisonOption(String options) {
        super(options);
    }

    private String calculateOptionString(double negative, double positive) {
        return "-" + String.valueOf(negative) + ":" + "+" + String.valueOf(positive);
    }

}
