package dexi.io.spark.spark.rl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldDTO {
    private static Map<String, List<DataCleaningMethod>> dataCleaningMethodMap;

    static {
        dataCleaningMethodMap = new HashMap<String, List<DataCleaningMethod>>();

        List<DataCleaningMethod> numberDataCleaningMethods = new ArrayList<DataCleaningMethod>();
        numberDataCleaningMethods.add(new DataCleaningMethod());

        dataCleaningMethodMap.put("Number", numberDataCleaningMethods);
    }

    private String name;
    private String dataType;
    private List<DataCleaningMethod> dataCleaningMethods;

    private FieldDTO(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
        this.dataCleaningMethods = dataCleaningMethodMap.get(dataType);
    }

}
