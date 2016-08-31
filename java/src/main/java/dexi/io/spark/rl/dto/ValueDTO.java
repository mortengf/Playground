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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueDTO valueDTO = (ValueDTO) o;

        return valueFields.equals(valueDTO.valueFields);
    }

    @Override
    public int hashCode() {
        return valueFields.hashCode();
    }

    @Override
    public String toString() {
        return "ValueDTO{" +
                "valueFields=" + valueFields +
                '}';
    }
}
