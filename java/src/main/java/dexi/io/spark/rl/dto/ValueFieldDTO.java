package dexi.io.spark.rl.dto;

import org.apache.spark.sql.types.DataType;

public class ValueFieldDTO extends FieldDTO {

    public ValueFieldDTO(String name, String dataType, Object value) {
        super(name, dataType, value);
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
