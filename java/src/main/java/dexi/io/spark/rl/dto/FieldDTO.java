package dexi.io.spark.rl.dto;

public abstract class FieldDTO {
    protected String name;
    protected String dataType;
    protected Object value;

    public FieldDTO(String name, String dataType, Object value) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
    }
}
