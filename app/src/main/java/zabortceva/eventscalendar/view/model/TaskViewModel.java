package zabortceva.eventscalendar.view.model;

import android.app.Application;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.repository.ServerCalendarRepository;
import zabortceva.eventscalendar.serverdata.Tasks;

public class TaskViewModel extends AndroidViewModel {
    private ServerCalendarRepository repository;
    private LiveData<List<Task>> tasksByEventId;
    private LiveData<List<Task>> dayTasks;
    private Date selectedDay;
    private LiveData<List<CalendarDay>> busyDays;

    public TaskViewModel(Application application) {
        super(application);

        repository = ServerCalendarRepository.getInstance(application);
        selectedDay = getDay(new Timestamp(System.currentTimeMillis()));
    }

    public Date getDay(Timestamp timestamp) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        try {
            day = formatter.parse(formatter.format(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public LiveData<Tasks> insert(Task task) {
        return repository.insertTask(task);
    }
    public LiveData<Tasks> update(Task task) {
        return repository.updateTask(task);
    }
    public LiveData<Tasks> delete(Task task) {
        return repository.deleteTask(task);
    }
    public LiveData<List<Task>> getSavedDayTasks() {
        return dayTasks;
    }
    public LiveData<List<Task>> getDayTasks(Timestamp timestamp) {
        Date day = getDay(timestamp);
        selectedDay = day;
        dayTasks = repository.getDayTasks(timestamp);
        return dayTasks;
    }
    public Timestamp getSelectedDay() {return new Timestamp(selectedDay.getTime());}
    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }
    public LiveData<List<CalendarDay>> getSavedAllBusyDays() {
        return busyDays;
    }
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        busyDays = repository.getAllBusyDays();
        return busyDays;
    }

    public LiveData<List<Task>> getSavedTasksByEventId() {
        return tasksByEventId;
    }

    public LiveData<List<Task>> getTasksByEventId(long id) {
        tasksByEventId = repository.getTasksByEventId(id);
        return tasksByEventId;
    }
}
