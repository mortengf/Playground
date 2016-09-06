package playground.spark.row;

import org.apache.spark.sql.types.DataType;

import java.util.HashMap;
import java.util.Map;


public class FieldConfig {
    private boolean isKey;
    private ComparisonMethod comparisonMethod;
    private Double weight;

    public FieldConfig() {
        // TODO: which comparison methods to instantiate - this should of course be dynamic
        comparisonMethod = new RangeComparisonMethod();
    }

    public boolean isKey() {
        return isKey;
    }

    public ComparisonMethod getComparisonMethod() {
        return comparisonMethod;
    }
}
