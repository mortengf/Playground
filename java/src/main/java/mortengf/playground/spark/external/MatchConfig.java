package mortengf.playground.spark.external;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MatchConfig implements Serializable {
    private Map<String, FieldConfig> fieldNameConfig;

    public MatchConfig() {
        fieldNameConfig = new HashMap<String, FieldConfig>();
    }

    public void addFieldConfig(String fieldName, FieldConfig fieldConfig) {
        fieldNameConfig.put(fieldName, fieldConfig);
    }

    public FieldConfig getFieldConfig(String fieldName) {
        return fieldNameConfig.get(fieldName);
    }

}
