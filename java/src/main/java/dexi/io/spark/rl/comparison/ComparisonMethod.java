package dexi.io.spark.rl.comparison;

import java.util.List;

public abstract class ComparisonMethod {
    protected List<ComparisonOption> options;

    public abstract boolean compare();
}
