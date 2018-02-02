package mortengf.playground.basic;

import org.apache.commons.lang.StringUtils;

public class UTF8Test {
    public static void main(String args[]) {
        // Korean
        String string1 = "[상설공연]요행복한 국악나무";
        String string2 = "[상설공연]요행복한 국악나무";

        System.out.println("String.equals(): " + string1.equals(string2));

        int levenshteinDistance = StringUtils.getLevenshteinDistance(string1, string2);
        System.out.println("Levenshtein distance (via StringUtils): " + levenshteinDistance);
    }
}
