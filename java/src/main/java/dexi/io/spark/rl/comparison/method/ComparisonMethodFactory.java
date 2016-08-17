package dexi.io.spark.rl.comparison.method;

import java.util.*;

public class ComparisonMethodFactory {
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
