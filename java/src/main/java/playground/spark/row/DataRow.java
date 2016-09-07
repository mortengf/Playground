package playground.spark.row;

import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataRow implements Serializable {
    private Row rddRow;
    private Map<String, Double> fieldNameScores;
    private Double totalScore;

    public DataRow() {
        fieldNameScores = new HashMap<String, Double>();
    }

    public DataRow(Row rddRow) {
        this();
        this.rddRow = rddRow;
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
