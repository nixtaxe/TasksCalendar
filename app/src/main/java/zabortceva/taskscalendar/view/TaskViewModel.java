package zabortceva.taskscalendar.view;

import android.app.Application;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.repository.CalendarRepository;
import zabortceva.taskscalendar.repository.SQLiteCalendarRepository;
import zabortceva.taskscalendar.repository.WebCalendarRepository;

public class TaskViewModel extends AndroidViewModel {
    private CalendarRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private Date selectedDay;
    private LiveData<List<CalendarDay>> busyDays;

    public TaskViewModel(Application application) {
        super(application);

        repository = new WebCalendarRepository(application);
        allTasks = repository.getAllTasks();
        selectedDay = new Date(System.currentTimeMillis());
        busyDays = repository.getAllBusyDays();
    }

    public void insert(Task task) {
        repository.insertTask(task);
    }

    public void update(Task task) {
        repository.updateTask(task);
    }

    public void delete(Task task) {
        repository.deleteTask(task);
    }

    public LiveData<List<Task>> getDayTasks(Timestamp timestamp) {
        final int EQUAL_DATES = 0;
        Date day = new Date(timestamp.getTime());
        if (dayTasks != null && day.compareTo(selectedDay) == EQUAL_DATES)
            return dayTasks;

        selectedDay = day;
        dayTasks = repository.getDayTasks(new Timestamp(day.getTime()));
        return dayTasks;
    }

    public Timestamp getSelectedDay() {return new Timestamp(selectedDay.getTime());}

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<CalendarDay>> getAllBusyDays() {
        return busyDays;
    }
}
