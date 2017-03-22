package mortengf.playground.spark.join.comparison.method;

import java.io.Serializable;
import java.util.*;

public class ComparisonMethodFactory implements Serializable {
    private static Map<String, Set<ComparisonMethod>> comparisonMethodMap;

    static {
        comparisonMethodMap = new HashMap<String, Set<ComparisonMethod>>();
        Set<ComparisonMethod> numberComparisonMethods = new HashSet<ComparisonMethod>();
        numberComparisonMethods.add(new ExactComparisonMethod());
        numberComparisonMethods.add(new RangeComparisonMethod());

        comparisonMethodMap.put("Number", numberComparisonMethods);
        // TODO: add comparison methods for other data types
    }

    public static Map<String, Set<ComparisonMethod>> getComparisonMethods() {
        return comparisonMethodMap;
    }

}
