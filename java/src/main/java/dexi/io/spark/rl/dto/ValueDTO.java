package dexi.io.spark.rl.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ValueDTO implements Serializable {
    private Set<FieldDTO> valueFields;

    public ValueDTO() {
        this.valueFields = new HashSet<FieldDTO>();
    }

    public Set<FieldDTO> getValueFields() {
        return valueFields;
    }

    public void addValueField(FieldDTO valueField) {
        this.valueFields.add(valueField);
    }

}
