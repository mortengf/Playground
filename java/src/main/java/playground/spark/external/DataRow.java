package playground.spark.external;

import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.*;

public class DataRow implements Serializable {

    private Row rddRow; // Currently used for the "source" data set ("cool people")
    private Map<String, Object> fieldNameValues; // Currently used for the "target" data set ("uncool people")
    private Map<String, Double> fieldNameScores;
    private Double totalScore;

    public DataRow() {
        fieldNameScores = new HashMap<String, Double>();
    }

    public DataRow(Row rddRow) {
        this();
        this.rddRow = rddRow;
    }

    public DataRow(Map<String, Object> fieldNameValues) {
        this();
        this.fieldNameValues = fieldNameValues;
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

    public Object getFieldValue(String fieldName) {
        return fieldNameValues.get(fieldName);
    }

    public Double getTotalScore() { return totalScore; }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        String json = "{\n";

        if (rddRow != null) {
            json += "\t\"rddRow\": { " + rddRow + " },\n";
        }

        if (fieldNameValues != null) {
            SortedMap fieldNameValuesSorted = new TreeMap(fieldNameValues);
            json += "\t\"fieldNameValues\": { " + fieldNameValuesSorted + " },\n";
        }

        if (fieldNameScores != null && fieldNameScores.size() > 0) {
            SortedMap fieldNameScoresSorted = new TreeMap(fieldNameScores);
            json += "\t\"fieldNameScores\": { " + fieldNameScoresSorted + " },\n";
        }

        if (totalScore != null) {
            json += "\t\"totalScore\": " + totalScore + " \n";
        }

        json += "}";
        return json;
    }
}
