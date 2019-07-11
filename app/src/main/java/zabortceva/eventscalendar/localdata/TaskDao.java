package zabortceva.eventscalendar.localdata;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table WHERE id = :id")
    void deleteDuplicates(int id);

    @Query("SELECT * FROM task_table WHERE date(deadline_at) = date(:day) ORDER BY deadline_at ASC")
    LiveData<List<Task>> getDayTasks(String day);

    @TypeConverters(TimestampConverters.class)
    @Query("SELECT * FROM task_table WHERE strftime('%d-%m-%Y', deadline_at / 1000, 'unixepoch', 'localtime') " +
            " = strftime('%d-%m-%Y', :day / 1000, 'unixepoch', 'localtime') ORDER BY deadline_at ASC")
    LiveData<List<Task>> getDayTasks(Timestamp day);

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @TypeConverters(CalendarDayConverters.class)
    @Query("SELECT DISTINCT strftime('%Y-%m-%d', deadline_at / 1000, 'unixepoch', 'localtime') FROM task_table ORDER BY deadline_at ASC")
    LiveData<List<CalendarDay>> getAllBusyDays();
}
