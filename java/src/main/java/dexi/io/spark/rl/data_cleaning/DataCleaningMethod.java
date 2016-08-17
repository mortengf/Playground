package dexi.io.spark.rl.data_cleaning;

import java.util.List;

public abstract class DataCleaningMethod {
    protected List<DataCleaningOption> options;

    public abstract void clean();
}

