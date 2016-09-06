package playground.spark.row;

import org.apache.spark.sql.Row;

import java.util.HashMap;
import java.util.Map;

public class DataRow {
    private Row rddRow;
    private Map<String, FieldConfig> fieldNameConfig;
    private Map<String, Double> fieldNameScores;
    private Double totalScore;

    public DataRow() {
        fieldNameConfig = new HashMap<String, FieldConfig>();
        fieldNameScores = new HashMap<String, Double>();
    }

    public DataRow(Row rddRow) {
        super();
        this.rddRow = rddRow;
    }

    public FieldConfig getFieldConfig(String fieldName) {
        return fieldNameConfig.get(fieldName);
    }

    public Map<String, Double> getFieldNameScores() {
        return fieldNameScores;
    }

    public void setSimilarityScore(String fieldName, Double similarityScore) {
        fieldNameScores.put(fieldName, similarityScore);
    }

    public Row getRddRow() {
        return rddRow;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }
}
