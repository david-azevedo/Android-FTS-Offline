package com.company.david.fts.Utils;

public final class PerformanceTime {
    // Start time
    private static long t1;
    // Time after date detection
    private static long t2;
    // Time after 4 gram
    private static long t3;
    // Time after Synonyms;
    private static long t4;
    // Time after sql
    private static long t5;
    // Time after Tf idf
    private static long t6;

    private static boolean foundDate = false;
    private static boolean foundSinom = false;

    public static void setFoundDate() {
        foundDate = true;
    }

    public static void setFoundSinom() {
        foundSinom = true;
    }

    public static void setT1(long t) {
        t1 = t;
    }

    public static void setT2(long t) {
        t2 = t;
    }

    public static void setT3(long t) {
        t3 = t;
    }

    public static void setT4(long t) {
        t4 = t;
    }

    public static void setT5(long t) {
        t5 = t;
    }

    public static void setT6(long t) {
        t6 = t;
    }

    public static String getToastMessage() {
        String res = "";

        if (foundDate) {
            res += "Date found!\n";
            foundDate = false;
        }

        if (foundSinom) {
            res += "Synonym found!\n";
            foundSinom = false;
        }

        res += "Date Detection ended " + (t2 - t1) + "ms.\n";
        res += "Synonym ended " + (t3 - t1) + "ms.\n";
        res += "4gram conversion ended " + (t4 - t1) + "ms.\n";
        res += "Sql ended " + (t5 - t1) + "ms.\n";
        res += "Tf idf ended " + (t6 - t1) + "ms.\n";

        return res;
    }
}
