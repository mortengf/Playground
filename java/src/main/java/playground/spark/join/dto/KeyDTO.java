package playground.spark.join.dto;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyDTO keyDTO = (KeyDTO) o;

        return keyFields.equals(keyDTO.keyFields);
    }

    @Override
    public int hashCode() {
        return keyFields.hashCode();
    }

    @Override
    public String toString() {
        return "KeyDTO{" +
                "keyFields=" + keyFields +
                '}';
    }
}
