package playground.spark.row;

import playground.spark.row.comparison_method.ComparisonMethod;

import java.io.Serializable;

public class FieldConfig implements Serializable {
    private boolean isKey;
    private ComparisonMethod comparisonMethod;
    private Double weight;

    public FieldConfig(boolean isKey, ComparisonMethod comparisonMethod, Double weight) {
        this.isKey = isKey;
        this.comparisonMethod = comparisonMethod;
        this.weight = weight;
    }

    public boolean isKey() {
        return isKey;
    }

    public ComparisonMethod getComparisonMethod() {
        return comparisonMethod;
    }

    public Double getWeight() {
        return weight;
    }
}
