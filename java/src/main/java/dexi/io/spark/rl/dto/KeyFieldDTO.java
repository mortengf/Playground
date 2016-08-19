package dexi.io.spark.rl.dto;

import dexi.io.spark.rl.comparison.method.ComparisonMethod;
import dexi.io.spark.rl.comparison.method.ComparisonMethodFactory;
import dexi.io.spark.rl.data_cleaning.DataCleaningMethod;
import dexi.io.spark.rl.data_cleaning.DataCleaningMethodFactory;
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

}
