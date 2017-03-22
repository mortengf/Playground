package mortengf.playground.spark.join.dto;

import mortengf.playground.spark.join.comparison.method.ComparisonMethod;
import mortengf.playground.spark.join.comparison.method.ComparisonMethodFactory;
import mortengf.playground.spark.join.data_cleaning.DataCleaningMethod;
import mortengf.playground.spark.join.data_cleaning.DataCleaningMethodFactory;
import org.apache.spark.sql.types.DataType;

import java.util.Map;
import java.util.Set;

public class KeyFieldDTO extends FieldDTO {
    private Double weight;

    private Map<String, Set<ComparisonMethod>> availableComparisonMethods;
    private Map<String, Set<ComparisonMethod>> selectedComparisonMethods;

    private Map<String, Set<DataCleaningMethod>> availableDataCleaningMethods;
    private Map<String, Set<DataCleaningMethod>> selectedDataCleaningMethods;

    public KeyFieldDTO(String name, DataType dataType) {
        super(name, dataType);
        this.availableComparisonMethods = ComparisonMethodFactory.getComparisonMethods();
        this.availableDataCleaningMethods = DataCleaningMethodFactory.getDataCleaningMethods();
    }

    @Override
    public String toString() {
        // TODO: include key-specific fields when we start using them
        return super.toString();
    }
}
