package zabortceva.taskscalendar.repository;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.List;

import androidx.lifecycle.LiveData;

import zabortceva.taskscalendar.localdata.Task;

public interface CalendarRepository {
    public void insert(Task task);

    public void update(Task task);

    public void delete(Task task);

    public LiveData<List<Task>> getDayTasks(Timestamp day);

    public LiveData<List<Task>> getAllTasks();

    public LiveData<List<CalendarDay>> getAllBusyDays();
}
