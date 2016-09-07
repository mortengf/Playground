package playground.spark.external.comparison_method;

import java.io.Serializable;

public interface ComparisonMethod extends Serializable {
    Double compare(Object v1, Object v2);
}
