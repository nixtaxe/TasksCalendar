package zabortceva.taskscalendar.repository;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.List;

import androidx.lifecycle.LiveData;

import zabortceva.taskscalendar.localdata.Task;

public interface CalendarRepository {
    public void insertTask(Task task);

    public void updateTask(Task task);

    public void deleteTask(Task task);

    public LiveData<List<Task>> getDayTasks(Timestamp day);

    public LiveData<List<Task>> getAllTasks();

    public LiveData<List<CalendarDay>> getAllBusyDays();
}
