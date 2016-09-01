package playground.spark.rl.dto;

import org.apache.spark.sql.types.DataType;

import java.io.Serializable;

public class FieldDTO implements Serializable {
    protected String name;
    protected DataType dataType;
    protected Object value;

    public FieldDTO(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDTO fieldDTO = (FieldDTO) o;

        if (!name.equals(fieldDTO.name)) return false;
        if (!dataType.equals(fieldDTO.dataType)) return false;
        return value != null ? value.equals(fieldDTO.value) : fieldDTO.value == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FieldDTO{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
