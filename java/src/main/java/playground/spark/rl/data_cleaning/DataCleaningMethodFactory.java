package playground.spark.rl.data_cleaning;

import java.util.*;

public class DataCleaningMethodFactory {
    private static Map<String, Set<DataCleaningMethod>> dataCleaningMethodMap;

    static {
        // TODO: should data cleaning take place BEFORE actual record linkage? If so, remove ALL data
        // cleaning-related code
        dataCleaningMethodMap = new HashMap<String, Set<DataCleaningMethod>>();

        Set<DataCleaningMethod> numberDataCleaningMethods = new HashSet<DataCleaningMethod>();
        numberDataCleaningMethods.add(new RemoveSeparators());

        dataCleaningMethodMap.put("Number", numberDataCleaningMethods);
        // TODO: add data cleaning methods for other data types
    }

    public static Map<String, Set<DataCleaningMethod>> getDataCleaningMethods() {
        return dataCleaningMethodMap;
    }

}
