package zabortceva.eventscalendar.view;

import android.app.Application;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.repository.CalendarRepository;
import zabortceva.eventscalendar.repository.WebCalendarRepository;

public class TaskViewModel extends AndroidViewModel {
    private CalendarRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private Date selectedDay;
    private LiveData<List<CalendarDay>> busyDays;

    public TaskViewModel(Application application) {
        super(application);

        repository = WebCalendarRepository.getInstance();
        selectedDay = new Date(System.currentTimeMillis());
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
        Date day = new Date(timestamp.getTime());
        selectedDay = day;
        return repository.getDayTasks(timestamp);
    }
    public Timestamp getSelectedDay() {return new Timestamp(selectedDay.getTime());}
    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        return repository.getAllBusyDays();
    }
}
