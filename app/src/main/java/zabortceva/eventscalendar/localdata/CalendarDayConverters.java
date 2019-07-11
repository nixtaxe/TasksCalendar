package zabortceva.eventscalendar.localdata;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.room.TypeConverter;

public class CalendarDayConverters {

    @TypeConverter
    public static CalendarDay fromString(String value) {
        CalendarDay result = null;
        try {
            result = CalendarDay.from(new SimpleDateFormat("yy-MM-dd").parse(value));
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return result;
    }

    @TypeConverter
    public static String calendarDayToString(CalendarDay calendarDay) {
        return calendarDay == null ? null : calendarDay.toString();
    }
}
