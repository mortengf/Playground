package dexi.io.spark.rl.dto;

import java.util.List;

public class ValueFieldDTO extends FieldDTO {
    // TODO: not needed?
    private List<FieldDTO> fields;

    public ValueFieldDTO(String name, String dataType, Object value) {
        super(name, dataType, value);
    }
}
