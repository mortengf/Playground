package mortengf.playground.basic;

public class UTF8Test {
    public static void main(String args[]) {
        // Korean
        String string1 = "[상설공연]요행복한 국악나무";
        String string2 = "[상설공연]요행복한 국악나무";

        System.out.println(string1.equals(string2));
    }
}
