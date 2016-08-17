package dexi.io.spark.rl.comparison.method;

import dexi.io.spark.rl.comparison.option.ComparisonOption;

import java.util.HashSet;
import java.util.Set;

public abstract class ComparisonMethod {
    protected Set<ComparisonOption> options;

    public ComparisonMethod() {
        options = new HashSet<ComparisonOption>();
    }

    public abstract boolean compare();
}
