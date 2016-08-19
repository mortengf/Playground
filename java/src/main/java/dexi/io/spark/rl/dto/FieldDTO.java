package dexi.io.spark.rl.dto;

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
