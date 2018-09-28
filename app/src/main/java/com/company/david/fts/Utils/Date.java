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

    private static final Pattern DATE_DD_MM_YYYY_PATTERN = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])[- \\/.](0?[1-9]|1[012])(?:[- \\/.]((?:19|20)?\\d\\d))?");
    private static final Pattern DATE_MM_DD_YYYY_PATTERN = Pattern.compile("(0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])(?:[- \\/.]((?:19|20)?\\d\\d))?");
    private static final Pattern DATE_YYYY_MM_DD_PATTERN = Pattern.compile("((?:19|20)?\\d\\d)[- \\/.](0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])");

    // TODO ver se vale a pena ou não introduzir horas
    // TODO Falar com a professora
    //public static final Pattern HOURS_PATTERN = Pattern.compile("([0-9]|0[0-9]|1[0-9]|2[0-3])(?::|h)([0-5][0-9]|[1-9])? *(?i)(A.?M.?|P.?M.?)?");
    //public static final Pattern HOURS_IN_FULL_PATTERN = Pattern.compile("(?!00)(\\d\\d)( ?h| ?o'clock)");

    private static final Pattern WEEK_DAY_PATTERN = Pattern.compile("\\b(?i)(?:mon|tue|wed|thur|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|segunda|ter[cç]a|quarta|quinta|sexta|s[aá]bado|domingo|seg|ter|qua|qui|sex|s[aá]b)\\b");
    private static final Pattern DATE_IN_FULL_EN_PATTERN = Pattern.compile("(?i)((?!00)[0-2][0-9]|[1-9]|30|31)(?:st|nd|rd|th)?(?: ?of ?| *)?(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|of)? *)?((?:19|20)?\\d\\d)?");
    private static final Pattern DATE_IN_FULL_MONTH_FIRST_EN_PATTERN = Pattern.compile("(?i)(january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|jun|jul|aug|sep|sept|oct|nov|dec)(?: *(?:,|the)? *)?((?!00)[0-2][0-9]|[1-9]|30|31)?(?:st|nd|rd|th)?(?: *(?:,|of)? *)((?:19|20)?\\d\\d)?");
    private static final Pattern DATE_IN_FULL_PT_PATTERN = Pattern.compile("((?!00)[0-2][0-9]|[1-9]|30|31)?(?i)(?: ?de ?| *)?\\b(janeiro|fevereiro|mar[çc]o|abril|maio|junho|julho|agosto|setembro|outubro|novembro|dezembro|jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez)\\b(?: ?de ?| *)?((?:19|20)?\\d\\d)?");

    private static SparseIntArray MONTH_INTEGER_TO_CALENDAR;
    private static HashMap<String, Integer> WEEK_DAY_TO_CALENDAR;
    private static HashMap<String, Integer> MONTH_TO_CALENDAR;

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
        WEEK_DAY_TO_CALENDAR = new HashMap<>();
        WEEK_DAY_TO_CALENDAR.put("mon", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("tue", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("wed", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("thur", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("fri", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("sat", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sun", Calendar.SUNDAY);
        WEEK_DAY_TO_CALENDAR.put("monday", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("tuesday", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("wednesday", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("thursday", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("friday", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("saturday", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sunday", Calendar.SUNDAY);
        WEEK_DAY_TO_CALENDAR.put("segunda", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("terca", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("terça", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("quarta", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("quinta", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("sexta", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("sabado", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sábado", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("domingo", Calendar.SUNDAY);
        WEEK_DAY_TO_CALENDAR.put("seg", Calendar.MONDAY);
        WEEK_DAY_TO_CALENDAR.put("ter", Calendar.TUESDAY);
        WEEK_DAY_TO_CALENDAR.put("qua", Calendar.WEDNESDAY);
        WEEK_DAY_TO_CALENDAR.put("qui", Calendar.THURSDAY);
        WEEK_DAY_TO_CALENDAR.put("sex", Calendar.FRIDAY);
        WEEK_DAY_TO_CALENDAR.put("sab", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("sáb", Calendar.SATURDAY);
        WEEK_DAY_TO_CALENDAR.put("dom", Calendar.SUNDAY);

        // Adding month string values English and Portuguese
        // janeiro|fevereiro|mar[çc]o|abril|maio|junho|julho|agosto|setembro|outubro|novembro|dezembro|jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez
        MONTH_TO_CALENDAR = new HashMap<>();
        MONTH_TO_CALENDAR.put("janeiro",Calendar.JANUARY);
        MONTH_TO_CALENDAR.put("fevereiro",Calendar.FEBRUARY);
        MONTH_TO_CALENDAR.put("marco",Calendar.MARCH);
        MONTH_TO_CALENDAR.put("março",Calendar.MARCH);
        MONTH_TO_CALENDAR.put("abril",Calendar.APRIL);
        MONTH_TO_CALENDAR.put("maio",Calendar.MAY);
        MONTH_TO_CALENDAR.put("junho",Calendar.JUNE);
        MONTH_TO_CALENDAR.put("julho",Calendar.JULY);
        MONTH_TO_CALENDAR.put("agosto",Calendar.AUGUST);
        MONTH_TO_CALENDAR.put("setembro",Calendar.SEPTEMBER);
        MONTH_TO_CALENDAR.put("outubro",Calendar.OCTOBER);
        MONTH_TO_CALENDAR.put("novembro",Calendar.NOVEMBER);
        MONTH_TO_CALENDAR.put("dezembro",Calendar.DECEMBER);
        MONTH_TO_CALENDAR.put("jan",Calendar.JANUARY);
        MONTH_TO_CALENDAR.put("fev",Calendar.FEBRUARY);
        MONTH_TO_CALENDAR.put("mar",Calendar.MARCH);
        MONTH_TO_CALENDAR.put("abr",Calendar.APRIL);
        MONTH_TO_CALENDAR.put("mai",Calendar.MAY);
        MONTH_TO_CALENDAR.put("jun",Calendar.JUNE);
        MONTH_TO_CALENDAR.put("jul",Calendar.JULY);
        MONTH_TO_CALENDAR.put("ago",Calendar.AUGUST);
        MONTH_TO_CALENDAR.put("set",Calendar.SEPTEMBER);
        MONTH_TO_CALENDAR.put("out",Calendar.OCTOBER);
        MONTH_TO_CALENDAR.put("nov",Calendar.NOVEMBER);
        MONTH_TO_CALENDAR.put("dez",Calendar.DECEMBER);
        MONTH_TO_CALENDAR.put("january",Calendar.JANUARY);
        MONTH_TO_CALENDAR.put("february",Calendar.FEBRUARY);
        MONTH_TO_CALENDAR.put("march",Calendar.MARCH);
        MONTH_TO_CALENDAR.put("april",Calendar.APRIL);
        MONTH_TO_CALENDAR.put("may",Calendar.MAY);
        MONTH_TO_CALENDAR.put("june",Calendar.JUNE);
        MONTH_TO_CALENDAR.put("july",Calendar.JULY);
        MONTH_TO_CALENDAR.put("august",Calendar.AUGUST);
        MONTH_TO_CALENDAR.put("september",Calendar.SEPTEMBER);
        MONTH_TO_CALENDAR.put("october",Calendar.OCTOBER);
        MONTH_TO_CALENDAR.put("november",Calendar.NOVEMBER);
        MONTH_TO_CALENDAR.put("december",Calendar.DECEMBER);
        MONTH_TO_CALENDAR.put("feb",Calendar.FEBRUARY);
        MONTH_TO_CALENDAR.put("apr",Calendar.APRIL);
        MONTH_TO_CALENDAR.put("aug",Calendar.AUGUST);
        MONTH_TO_CALENDAR.put("sep",Calendar.SEPTEMBER);
        MONTH_TO_CALENDAR.put("sept",Calendar.SEPTEMBER);
        MONTH_TO_CALENDAR.put("oct",Calendar.OCTOBER);
        MONTH_TO_CALENDAR.put("dec",Calendar.DECEMBER);
    }

    public static DateMatch detectDates(String query) {
        DateMatch result = new DateMatch();

        // Detecting day of week
        Matcher m = WEEK_DAY_PATTERN.matcher(query);
        if (m.find()) {
            result.day_of_week = WEEK_DAY_TO_CALENDAR.get(m.group().toLowerCase());
        }

        // Matching DD/MM/(YY)YY
        m = DATE_DD_MM_YYYY_PATTERN.matcher(query);
        if (m.find()) {
            result.day = m.group(1);
            result.month = MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(2)));
            if(m.group(3) != null) {
                result.year = m.group(3);
                if (result.year.length() == 2)
                    result.year = "20" + result.year;
            }

            return result;
        }

        // Matching MM/DD/(YY)YY
        m = DATE_MM_DD_YYYY_PATTERN.matcher(query);
        if (m.find()) {
            result.month = MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(1)));
            result.day = m.group(2);
            if(m.group(3) != null) {
                result.year = m.group(3);
                if (result.year.length() == 2)
                    result.year = "20" + result.year;
            }

            return result;
        }

        // Matching YYYY/MM/DD
        m = DATE_YYYY_MM_DD_PATTERN.matcher(query);
        if(m.find()) {
            result.year = m.group(1);
            if (result.year.length() == 2)
                result.year = "20" + result.year;
            result.month = MONTH_INTEGER_TO_CALENDAR.get(Integer.parseInt(m.group(2)));
            result.day = m.group(3);

            return result;
        }

        m = DATE_IN_FULL_PT_PATTERN.matcher(query);
        if (m.find()) {
            result.day = m.group(1);
            result.month = MONTH_TO_CALENDAR.get(m.group(2).toLowerCase());
            result.year = m.group(3);
            if (result.year != null)
                if (result.year.length() == 2)
                    result.year = "20" + result.year;

            return result;
        }


        m = DATE_IN_FULL_EN_PATTERN.matcher(query);
        if (m.find()) {
            result.day = m.group(1);
            result.month = MONTH_TO_CALENDAR.get(m.group(2).toLowerCase());
            result.year = m.group(3);
            if (result.year != null)
                if (result.year.length() == 2)
                    result.year = "20" + result.year;

            return result;
        }

        m = DATE_IN_FULL_MONTH_FIRST_EN_PATTERN.matcher(query);
        if (m.find()) {
            result.day = m.group(2);
            result.month = MONTH_TO_CALENDAR.get(m.group(1).toLowerCase());
            result.year = m.group(3);
            if(result.year != null)
                if (result.year.length() == 2)
                    result.year = "20" + result.year;

            return result;
        }

        return result;
    }
}
