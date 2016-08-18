package dexi.io.spark.rl.dto;

import org.apache.spark.sql.types.DataType;

public class FieldDTO extends DataType {
    protected String name;
    protected String dataType;
    protected Object value;

    public FieldDTO(String name, String dataType, Object value) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
    }

    @Override
    public int defaultSize() {
        return 0;
    }

    @Override
    public DataType asNullable() {
        return null;
    }
}
