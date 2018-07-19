package com.company.david.fts.Utils;

import android.util.SparseIntArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
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

    public static final Pattern WEEK_DAY_PATTERN = Pattern.compile("(?i)mon|tue|wed|thur|fri|sat|sun|segunda|ter[cç]a|quarta|quinta|sexta|s[aá]bado|domingo");
    public static final Pattern DATE_IN_FULL_EN_PATTERN = Pattern.compile("(?i)((?!00)[0-2][0-9]|[1-9]|30|31)(?:st|nd|rd|th)?(?: ?of ?| *)?(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|of)? *)?((?:19|20)?\\d\\d)?");
    public static final Pattern DATE_IN_FULL_MONTH_FIRST_EN_PATTERN = Pattern.compile("(?i)(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|the)? *)?((?!00)[0-2][0-9]|[1-9]|30|31)?(?:st|nd|rd|th)?(?: *(?:,|of)? *)?((?:19|20)?\\d\\d)?");
    public static final Pattern DATE_IN_FULL_PT_PATTERN = Pattern.compile("((?!00)[0-2][0-9]|[1-9]|30|31)?(?i)(?: ?de ?| *)?(janeiro|fevereiro|março|abril|maio|junho|julho|agosto|setembro|outubro|novembro|dezembro|jan|feb|mar|abr|jun|jul|ago|set|out|nov|dez)(?: ?de ?| *)?((?:19|20)?\\d\\d)?");

    public static SparseIntArray MONTH_INTEGER_TO_CALENDAR;
    public static HashMap<String, Integer> WEEK_DAY_TO_CALENDAR;

    static
    {
        // Adding month values
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

        // Adding week day values
        // mon|tue|wed|thur|fri|sat|sun|segunda|ter[cç]a|quarta|quinta|sexta|s[aá]bado|domingo
        WEEK_DAY_TO_CALENDAR = new HashMap<String, Integer>();
        WEEK_DAY_TO_CALENDAR.put("mon", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("tue", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("wed", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("thur", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("fri", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("sat", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sun", Calendar.SUNDAY);
        WEEK_DAY_TO_CALENDAR.put("segunda", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("terca", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("terça", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("quarta", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("quinta", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("sexta", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("sabado", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sábado", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("domingo", Calendar.SUNDAY);
    }

    public static DateMatch detectDates(String query) {
        DateMatch result = new DateMatch();

        // TODO identificar aqui as datas e trabalhar com elas

        // TODO problema de ser bastante abrangente (sat -> saturado, mon -> monte, etc.)
        // Detecting day of week
        Matcher m = Date.WEEK_DAY_PATTERN.matcher(query);
        if (m.matches()) {
            result.day_of_week = Date.WEEK_DAY_TO_CALENDAR.get(m.group()) + "";
        }

        // Matching DD/MM/(YY)YY
        m = Date.DATE_DD_MM_YYYY_PATTERN.matcher(query);
        if (m.matches()) {
            result.day = m.group(1);
            result.month = Date.MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(2))) + "";
            if(m.group(3) != null) {
                result.year = m.group(3);
                if (result.year.length() == 2)
                    result.year = "20" + result.year;
            }

            return result;
        }

        // Matching MM/DD/(YY)YY
        m = Date.DATE_MM_DD_YYYY_PATTERN.matcher(query);
        if (m.matches()) {
            result.month = Date.MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(1))) + "";
            result.day = m.group(2);
            if(m.group(3) != null) {
                result.year = m.group(3);
                if (result.year.length() == 2)
                    result.year = "20" + result.year;
            }

            return result;
        }

        // Matching YYYY/MM/DD
        m = Date.DATE_YYYY_MM_DD_PATTERN.matcher(query);
        if(m.matches()) {
            result.year = m.group(1);
            if (result.year.length() == 2)
                result.year = "20" + result.year;
            result.month = Date.MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(2))) + "";
            result.day = m.group(3);

            return result;
        }

        // Identificação da lingua em que o dispositivo esta
        if (Locale.getDefault().getLanguage().equals(new Locale("pt").getLanguage())) {// Português

        } else { // Inglês

        }

        return result;
    }
}
