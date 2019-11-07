package gr.nightwall.deliveryapp.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Date {
    private static final String DATE_AND_TIME_FORMAT = "EEE, dd MMM @ HH:mm";
    private static final String MONTH_AND_DATE_FORMAT = "dd MMM";
    private static final String DAY_3_FORMAT = "EEE";
    private static final String ID_FORMAT = "yyMMddHHmmss";

    private int year, monthOfYear, dayOfMonth;
    private Time time = new Time();


    /* = = = = = = = = = = = = = = = = *
     *              STATIC             *
     * = = = = = = = = = = = = = = = = */

    public static Date getNow(){
        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.SECOND);

        return new Date(year, month, dayOfMonth, hours, minutes, seconds, millis);
    }

    public static int getTimeStamp(){
        Date date = getNow();

        return date.getMilliseconds()
                + date.getSeconds() * 1000
                + date.getMinute() * 24000;
    }


    /* = = = = = = = = = = = = = = = = *
     *           CONSTRUCTORS          *
     * = = = = = = = = = = = = = = = = */

    public Date() {}

    public Date(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

        time = new Time(hourOfDay, minute);
    }

    public Date(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int seconds) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

        time = new Time(hourOfDay, minute, seconds);
    }

    public Date(int year, int monthOfYear, int dayOfMonth, int hourOfDay,
                int minute, int seconds, int millis) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

        time = new Time(hourOfDay, minute, seconds, millis);
    }

    public Date(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

        time = new Time(0, 0);
    }


    /* = = = = = = = = = = = = = = = = *
     *              SETTERS            *
     * = = = = = = = = = = = = = = = = */

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHourOfDay(int hourOfDay) {
        time.setHours(hourOfDay);
    }

    public void setMinute(int minute) {
        time.setMinutes(minute);
    }

    public void setSeconds(int seconds) {
        time.setSeconds(seconds);
    }


    /* = = = = = = = = = = = = = = = = *
     *              GETTERS            *
     * = = = = = = = = = = = = = = = = */

    public int getYear() {
        return year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHourOfDay() {
        return time.getHours();
    }

    public int getMinute() {
        return time.getMinutes();
    }

    public int getSeconds() {
        return time.getSeconds();
    }

    public int getMilliseconds() {
        return time.getMillis();
    }

    public Time getTime() {
        return time;
    }


    /* = = = = = = = = = = = = = = = = *
     *           MORE GETTERS          *
     * = = = = = = = = = = = = = = = = */

    private String getDateByFormat(String format) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, time.getHours());
        c.set(Calendar.MINUTE, time.getMinutes());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        return simpleDateFormat.format(c.getTime());
    }

    public String getDateAndTimeString() {
        return getDateByFormat(DATE_AND_TIME_FORMAT);
    }

    public String getDateForId() {
        return getDateByFormat(ID_FORMAT);
    }

    public String getTimeString() {
        return time.getTimeString();
    }

    public String getMonthAndDay() {
        return getDateByFormat(MONTH_AND_DATE_FORMAT);
    }

    public String getDayName3() {
        return getDateByFormat(DAY_3_FORMAT);
    }


    /* = = = = = = = = = = = = = = = = *
     *            DIFFERENCE           *
     * = = = = = = = = = = = = = = = = */

    public static int getHoursDifference(Date start, Date end) {
        int extraHour = 0;
        if (end.getMinute() >= start.getMinute())
            extraHour = 1;

        if (getDaysDifference(start, end) > 0) {
            int difference = (24 + (end.getHourOfDay() - start.getHourOfDay())
                    + 24 * (getDaysDifference(start, end) - 1)
                    + extraHour);

            return difference == 0 ? 0 : difference - 1;
        }

        int difference = end.getHourOfDay() - start.getHourOfDay() + extraHour;
        return difference == 0 ? 0 : difference - 1;
    }

    public static int getDaysDifference(Date start, Date end) {
        if (getMonthsDifference(start, end) > 0) {
            int days = getDaysInMonth(start.getMonthOfYear(), start.getYear());

            return (days + (end.getDayOfMonth() - start.getDayOfMonth())
                    + days * (getMonthsDifference(start, end) - 1));
        }

        return end.getDayOfMonth() - start.getDayOfMonth();
    }

    public static int getMonthsDifference(Date start, Date end) {
        if (getYearsDifference(start, end) > 0) {
            return (12 + (end.getMonthOfYear() - start.getMonthOfYear())
                    + 12 * (getYearsDifference(start, end) - 1));
        }


        return end.getMonthOfYear() - start.getMonthOfYear();
    }

    public static int getYearsDifference(Date start, Date end) {

        return end.getYear() - start.getYear();
    }

    private static int getDaysInMonth(int month, int year) {
        int feb = 28;
        if (isLeapYear(year))
            feb ++;

        int[] days = {31, feb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return days[month - 1];
    }

    private static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else return year % 100 != 0;
    }


    /* = = = = = = = = = = = = = = = = *
     *            CONVERTERS           *
     * = = = = = = = = = = = = = = = = */

    public int toHours(){
        return getHourOfDay();
    }
}
