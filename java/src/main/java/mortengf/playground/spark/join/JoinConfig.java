package mortengf.playground.spark.join;

import mortengf.playground.spark.join.dto.FieldDTO;
import mortengf.playground.spark.join.dto.KeyFieldDTO;
import org.apache.spark.sql.types.DataType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// TODO: use class scala.Tuple2 instead of this class?
public class JoinConfig implements Serializable {
    private Set<KeyFieldDTO> keyFields;
    private Set<FieldDTO> valueFields;

    public JoinConfig() {
        this.keyFields = new HashSet<KeyFieldDTO>();
        this.valueFields = new HashSet<FieldDTO>();
    }

    public void addKeyField(KeyFieldDTO keyField) {
        this.keyFields.add(keyField);
    }

    public void addValueField(FieldDTO valueField) {
        this.valueFields.add(valueField);
    }

    public boolean containsKeyField(String name, DataType dataType) {
        boolean result = false;
        for (KeyFieldDTO keyField : keyFields) {
            if (keyField.getName().equals(name) && keyField.getDataType().equals(dataType)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public boolean containsValueField(String name, DataType dataType) {
        boolean result = false;
        for (FieldDTO valueField : valueFields) {
            if (valueField.getName().equals(name) && valueField.getDataType().equals(dataType)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public Set<KeyFieldDTO> getKeyFields() {
        return keyFields;
    }

    public Set<FieldDTO> getValueFields() {
        return valueFields;
    }

}
