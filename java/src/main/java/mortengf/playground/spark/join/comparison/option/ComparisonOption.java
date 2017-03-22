package mortengf.playground.spark.join.comparison.option;

import java.io.Serializable;

public abstract class ComparisonOption implements Serializable {
    protected String options;

    public ComparisonOption(String options) {
        this.options = options;
    }

}
