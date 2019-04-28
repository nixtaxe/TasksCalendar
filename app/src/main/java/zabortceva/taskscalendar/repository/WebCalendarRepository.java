package zabortceva.taskscalendar.repository;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.List;

import androidx.lifecycle.LiveData;
import zabortceva.taskscalendar.localdata.Task;

public class WebCalendarRepository implements CalendarRepository {
    @Override
    public void insert(Task task) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void delete(Task task) {

    }

    @Override
    public LiveData<List<Task>> getDayTasks(Timestamp day) {
        return null;
    }

    @Override
    public LiveData<List<Task>> getAllTasks() {
        return null;
    }

    @Override
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        return null;
    }
}
