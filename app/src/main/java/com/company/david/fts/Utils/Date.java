package com.company.david.fts.Utils;

import android.util.SparseIntArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/*
* Class to map months to standard format
*/
public class Date {

    public static final Pattern DATE_DD_MM_YYYY_PATTERN = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- \\/.](0?[1-9]|1[012])(?:[- \\/.]((?:19|20)?\\d\\d))?");
    public static final Pattern DATE_MM_DD_YYYY_PATTERN = Pattern.compile("(0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])(?:[- \\/.]((?:19|20)?\\d\\d))?");
    public static final Pattern DATE_YYYY_MM_DD_PATTERN = Pattern.compile("((?:19|20)?\\d\\d)[- \\/.](0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])");

    // TODO ver se vale a pena ou não introduzir horas
    //public static final Pattern HOURS_PATTERN = Pattern.compile("([0-9]|0[0-9]|1[0-9]|2[0-3])(?::|h)([0-5][0-9]|[1-9])? *(?i)(A.?M.?|P.?M.?)?");
    //public static final Pattern HOURS_IN_FULL_PATTERN = Pattern.compile("(?!00)(\\d\\d)( ?h| ?o'clock)");

    public static final Pattern WEEK_DAY_PATTERN = Pattern.compile("(?i)monday|tuesday|wednesday|thursday|friday|saturday|sunday|mon|tue|tues|wed|thur|thurs|fri|sat|sun|(?:segunda|ter[cç]a|quarta|quinta|sexta)(?:-feira)?|s[aá]bado|domingo");
    public static final Pattern DATE_IN_FULL_EN_PATTERN = Pattern.compile("(?i)((?!00)[0-2][0-9]|[1-9]|30|31)(?:st|nd|rd|th)?(?: ?of ?| *)?(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|of)? *)?((?:19|20)?\\d\\d)?");
    public static final Pattern DATE_IN_FULL_MONTH_FIRST_EN_PATTERN = Pattern.compile("(?i)(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|the)? *)?((?!00)[0-2][0-9]|[1-9]|30|31)?(?:st|nd|rd|th)?(?: *(?:,|of)? *)?((?:19|20)?\\d\\d)?");
    public static final Pattern DATE_IN_FULL_PT_PATTERN = Pattern.compile("((?!00)[0-2][0-9]|[1-9]|30|31)?(?i)(?: ?de ?| *)?(janeiro|fevereiro|março|abril|maio|junho|julho|agosto|setembro|outubro|novembro|dezembro|jan|feb|mar|abr|jun|jul|ago|set|out|nov|dez)(?: ?de ?| *)?((?:19|20)?\\d\\d)?");

    public static SparseIntArray MONTH_INTEGER_TO_CALENDAR;

    static
    {
        MONTH_INTEGER_TO_CALENDAR = new SparseIntArray();
        MONTH_INTEGER_TO_CALENDAR.put(1, Calendar.JANUARY);
        MONTH_INTEGER_TO_CALENDAR.put(2, Calendar.FEBRUARY);
        MONTH_INTEGER_TO_CALENDAR.put(3, Calendar.MARCH);
        MONTH_INTEGER_TO_CALENDAR.put(4, Calendar.APRIL);
        MONTH_INTEGER_TO_CALENDAR.put(5, Calendar.MAY);
        MONTH_INTEGER_TO_CALENDAR.put(6, Calendar.JUNE);
        MONTH_INTEGER_TO_CALENDAR.put(7, Calendar.JULY);
        MONTH_INTEGER_TO_CALENDAR.put(8, Calendar.AUGUST);
        MONTH_INTEGER_TO_CALENDAR.put(9, Calendar.SEPTEMBER);
        MONTH_INTEGER_TO_CALENDAR.put(10, Calendar.OCTOBER);
        MONTH_INTEGER_TO_CALENDAR.put(11, Calendar.NOVEMBER);
        MONTH_INTEGER_TO_CALENDAR.put(12, Calendar.DECEMBER);
    }
}
