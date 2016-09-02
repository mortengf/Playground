package playground.spark.join.comparison.method;

import playground.spark.join.comparison.option.ComparisonOption;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class ComparisonMethod implements Serializable {
    protected Set<ComparisonOption> options;

    public ComparisonMethod() {
        options = new HashSet<ComparisonOption>();
    }

    public abstract boolean compare();
}
