package gr.nightwall.deliveryapp.models;


import java.util.Calendar;
import java.util.Locale;

public class Time {
    private static final String TIME_FORMAT = "%02d:%02d";

    private int hours, minutes, seconds, millis;

    public Time() {}

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        seconds = 0;
    }

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Time(int hours, int minutes, int seconds, int millis) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
    }

    public static Time getNow(){
        Calendar now = Calendar.getInstance();

        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);

        return new Time(hours, minutes, seconds, millis);
    }


    // SETTERS
    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    // GETTERS
    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMillis() {
        return millis;
    }

    public String getTimeString(){
        return String.format(Locale.getDefault(), TIME_FORMAT, hours, minutes);
    }
}
