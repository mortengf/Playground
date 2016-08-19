package dexi.io.spark.rl.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class KeyDTO implements Serializable {
    private Set<KeyFieldDTO> keyFields;

    public KeyDTO() {
        this.keyFields = new HashSet<KeyFieldDTO>();
    }

    public void addKeyField(KeyFieldDTO keyField) {
        this.keyFields.add(keyField);
    }

    public Set<KeyFieldDTO> getKeyFields() {
        return keyFields;
    }

    public void setKeyFields(Set<KeyFieldDTO> keyFields) {
        this.keyFields = keyFields;
    }
}
