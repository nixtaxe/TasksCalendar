package zabortceva.eventscalendar.localdata;

import androidx.room.TypeConverter;

import java.sql.Timestamp;

public class TimestampConverters {

    @TypeConverter
    public static Timestamp fromLong(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long timestampToLong(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getTime();
    }
}
