package dexi.io.spark.rl.data_cleaning;

import java.io.Serializable;
import java.util.List;

public abstract class DataCleaningMethod implements Serializable {
    protected List<DataCleaningOption> options;

    public abstract void clean();
}

