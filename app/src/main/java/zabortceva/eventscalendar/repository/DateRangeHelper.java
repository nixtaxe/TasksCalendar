package zabortceva.eventscalendar.repository;

import android.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateRangeHelper {
    private static DateRangeHelper instance;

    private DateRangeHelper() {

    }

    public static DateRangeHelper getInstance() {
        if (instance == null)
            instance = new DateRangeHelper();
        return instance;
    }

    public static Pair<Date, Date> getDayDateRange(Date day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        setTimeToBeginningOfDay(calendar);
        Date startOfDay = calendar.getTime();
        setTimeToEndofDay(calendar);
        Date endOfDay = calendar.getTime();

        return new Pair<>(startOfDay, endOfDay);
    }

    public static Pair<Date, Date> getDateRange(Date date) {
        Date begining, end;

        {
            Calendar calendar = getCalendarForNow(date);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow(date);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        return new Pair<>(begining, end);
    }

    private static Calendar getCalendarForNow(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
