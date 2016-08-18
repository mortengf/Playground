package dexi.io.spark.rl.dto;

import org.apache.spark.sql.types.DataType;

import java.util.Set;

// TODO: use class scala.Tuple2 instead of this class?
public class RecordDTO extends DataType {
    private Set<KeyFieldDTO> keys;
    private Set<ValueFieldDTO> values;

    @Override
    public int defaultSize() {
        return 0;
    }

    @Override
    public DataType asNullable() {
        return null;
    }

    public Set<KeyFieldDTO> getKeys() {
        return keys;
    }

    public void setKeys(Set<KeyFieldDTO> keys) {
        this.keys = keys;
    }

    public Set<ValueFieldDTO> getValues() {
        return values;
    }

    public void setValues(Set<ValueFieldDTO> values) {
        this.values = values;
    }
}
