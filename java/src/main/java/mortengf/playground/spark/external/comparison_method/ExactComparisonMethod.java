package mortengf.playground.spark.external.comparison_method;

public class ExactComparisonMethod implements ComparisonMethod {

    public Double compare(Object v1, Object v2) {
        return v1.equals(v2) ? 1.0 : 0.0;
    }
}
