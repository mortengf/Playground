package playground.spark.join.data_cleaning;

import java.io.Serializable;

public class DataCleaningOption implements Serializable {
    private String options;

    public DataCleaningOption(String options) {
        this.options = options;
    }

}
