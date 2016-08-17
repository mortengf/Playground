package dexi.io.spark.rl;

import dexi.io.spark.rl.comparison.ComparisonMethod;
import dexi.io.spark.rl.comparison.ExactComparison;
import dexi.io.spark.rl.comparison.RangeComparison;
import dexi.io.spark.rl.data_cleaning.DataCleaningMethod;
import dexi.io.spark.rl.data_cleaning.RemoveSeparators;

import java.util.*;

public class FieldDTO {
    private static Map<String, List<ComparisonMethod>> comparisonMethodMap;
    private static Map<String, List<DataCleaningMethod>> dataCleaningMethodMap;

    // TODO: refactor to factory methods + implement setters for comparison & data cleaning methods
    static {
        comparisonMethodMap = new HashMap<String, List<ComparisonMethod>>();
        List<ComparisonMethod> numberComparisonMethods = new ArrayList<ComparisonMethod>();
        numberComparisonMethods.add(new ExactComparison());
        numberComparisonMethods.add(new RangeComparison(-100, 100));

        comparisonMethodMap.put("Number", numberComparisonMethods);
        // TODO: add comparison methods for other data types

        // TODO: should data cleaning take place BEFORE actual record linkage? If so, remove ALL data
        // cleaning-related code
        dataCleaningMethodMap = new HashMap<String, List<DataCleaningMethod>>();

        List<DataCleaningMethod> numberDataCleaningMethods = new ArrayList<DataCleaningMethod>();
        numberDataCleaningMethods.add(new RemoveSeparators());

        dataCleaningMethodMap.put("Number", numberDataCleaningMethods);
        // TODO: add data cleaning methods for other data types
    }

    private String name;
    private String dataType;
    private Double weight;

    private List<ComparisonMethod> availableComparisonMethods;
    private List<ComparisonMethod> selectedComparisonMethods;

    private List<DataCleaningMethod> availableDataCleaningMethods;
    private List<DataCleaningMethod> selectedDataCleaningMethods;

    private FieldDTO(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
        this.availableComparisonMethods = comparisonMethodMap.get(dataType);
        this.availableDataCleaningMethods = dataCleaningMethodMap.get(dataType);
    }


}
