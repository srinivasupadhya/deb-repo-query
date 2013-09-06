package com.tw.pkg.version;

import junit.framework.TestCase;

import java.text.Collator;
import java.util.*;

public class StringsTest extends TestCase {
    public void testCompareNatural() {
        assertEquals(0, c("", ""));
        assertEquals(1, c("1-02", "1-2"));
        assertEquals(-1, c("1-2", "1-02"));
        assertEquals(-1, c("catch 22", "catch 022"));
        assertEquals(0, c("a", "a"));
        assertEquals(-1, c("2a", "2a2"));
        assertEquals(1, c("b", "a"));
        assertEquals(-1, c("a", "b"));
        assertEquals(-1, c("002", "11"));
        assertEquals(-1, c("2", "11"));
        assertEquals(1, c("22", "11"));
        assertEquals(1, c("222", "99"));
        assertEquals(-1, c("a 2", "a 11"));
        assertEquals(-1, c("c23", "c111"));
        assertEquals(-1, c("a2", "aa2"));
        assertEquals(1, c("a 22", "a 2"));
        assertEquals(1, c("a", "A"));
        assertEquals(-1, c("a 2 h", "a 2 h 2"));
        assertEquals(-1, c("abcd 234 huj", "abcd 234 huj 2"));
        assertEquals(0, c("abcd 234 huj", "abcd 234 huj"));
        assertEquals(1, c("abcd 234 huj 33", "abcd 234 huj 9"));
        assertEquals(-1, c("1.9.2-r9abc", "1.10.1-r9abc"));
        assertEquals(1, c("1.9.2-r10abc", "1.9.2-r9abc"));
    }

    public void testNaturalCompareWhitespace() {
        String[] strings = {"p4", "p  3"};
        List<String> sortedStrings = Arrays.asList(strings);
        List<String> testStrings = new ArrayList<String>(sortedStrings);
        for (int i = 0; i < 10; ++i) {
            Collections.shuffle(testStrings);
            Collections.sort(testStrings, Strings.getNaturalComparator());
            assertEquals(sortedStrings, testStrings);
        }
    }


    public void testCompareNaturalIgnoreCase() {
        assertEquals(0, ci("a", "a"));
        assertEquals(1, ci("b", "a"));
        assertEquals(0, ci("A", "a"));
        assertEquals(0, ci("A12", "a12"));
        assertEquals(1, ci("A12 11", "a12 9"));
        assertEquals(-1, ci("catch 22", "cAtCh 022"));
        assertEquals(1, ci("pic 5", "pic 4 else"));
        assertEquals(1, ci("p 5 s", "p 5"));
        assertEquals(-1, ci("p 5", "p 5 s"));
    }

    public void testCompareNaturalCollator() {
        Collator c = Collator.getInstance(Locale.GERMANY);
        c.setStrength(Collator.SECONDARY);
        assertEquals(0, c(c, "a", "a"));
        assertEquals(0, c(c, "a", "A"));
        assertEquals(1, c(c, "ä", "a"));
        assertEquals(1, c(c, "B", "a"));
    }

    private int c(Collator c, String a, String b) {
        int result = Strings.compareNatural(c, a, b);
        result = result < 0 ? -1 : result > 0 ? 1 : 0;
        return result;
    }

    private int c(String a, String b) {
        int result = Strings.compareNaturalAscii(a, b);
        result = result < 0 ? -1 : !(result <= 0) ? 1 : 0;
        return result;
    }

    private int ci(String a, String b) {
        int result = Strings.compareNaturalIgnoreCaseAscii(a, b);
        result = result < 0 ? -1 : !(result <= 0) ? 1 : 0;
        return result;
    }

    public static void testListSort() {
        String[] strings = new String[]{"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic   7", "pic 4 else",
                "pic 5", "pic 5", "pic 5 something", "pic 6", "pic01", "pic2", "pic02",
                "pic02a", "pic3", "pic4", "pic05", "pic100", "pic100a", "pic120", "pic121",
                "pic02000", "tom", "x2-g8", "x2-y7", "x2-y08", "x8-y8"};

        List<String> expectedSorted = new ArrayList<String>(Arrays.asList(strings));
        List<String> actualSorted = new ArrayList<String>(Arrays.asList(strings));

        for (int i = 0; i < 1000; ++i) {
            Collections.shuffle(actualSorted);
            Collections.sort(actualSorted, Strings.getNaturalComparatorIgnoreCaseAscii());

            assertEquals(expectedSorted, actualSorted);
        }
    }
}
