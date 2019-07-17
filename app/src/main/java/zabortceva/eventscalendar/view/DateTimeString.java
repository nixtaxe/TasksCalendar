package zabortceva.eventscalendar.view;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import scala.util.parsing.combinator.testing.Str;

public class DateTimeString {
    private String date;
    private String time;
    private Timestamp timestamp;

    private static DateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dfTime = new SimpleDateFormat("HH:mm");

    public DateTimeString(Timestamp t) {
        timestamp = t;
        date = dfDay.format(t);
        time = dfTime.format(t);
    }

    public DateTimeString(String date) {
        this.date = date;
    }

    public static String getDateString(long timeInMillis) {
        return dfDay.format(new Timestamp(timeInMillis));
    }

    public Date getDate() {
        Date result = new Date();
        try {
            result = dfDay.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date getDate(String date) {
        Date result = new Date();
        try {
            result = dfDay.parse(date);
        } catch (ParseException e) {
            try {
                result = dfTime.parse(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    public static String getTimeString(long timeInMlllis) {
        return dfTime.format(new Timestamp(timeInMlllis));
    }
}
