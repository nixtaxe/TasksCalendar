package zabortceva.taskscalendar.view;

import android.app.Application;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.repository.SQLiteCalendarRepository;

public class TaskViewModel extends AndroidViewModel {
    private SQLiteCalendarRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private Date selectedDay;
    private LiveData<List<CalendarDay>> busyDays;

    public TaskViewModel(Application application) {
        super(application);

        repository = new SQLiteCalendarRepository(application);
        allTasks = repository.getAllTasks();
        selectedDay = new Date(System.currentTimeMillis());
        busyDays = repository.getAllBusyDays();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public LiveData<List<Task>> getDayTasks(Timestamp timestamp) {
        final int EQUAL_DATES = 0;
        if (dayTasks != null && timestamp.compareTo(selectedDay) == EQUAL_DATES)
            return dayTasks;

        selectedDay = new Date(selectedDay.getTime());
        dayTasks = repository.getDayTasks(timestamp);
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
