package playground.spark.external.comparison_method;

public class RangeComparisonMethod implements ComparisonMethod {
    private int range;

    public RangeComparisonMethod(int range) {
        this.range = range;
    }

    public Double compare(Object v1, Object v2) {
        Double score = 0.0;

        Long v1Value = (Long) v1;
        Long v2Value = (Long) v2;
        Long diff = Math.abs(v1Value - v2Value);

        if (diff <= range) {
           score = 1.0;
        // TODO: how to implement a "gradually declining" score?
        } else if (diff < range * 1.3) {
            score = 0.7;
        } else if (diff < range * 1.5) {
            score = 0.5;
        } else if (diff < range * 1.7) {
            score = 0.3;
        } else {
            score = 0.1;
        }

        return score;
    }
}
