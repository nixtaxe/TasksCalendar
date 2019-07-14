package zabortceva.eventscalendar.view;

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
import zabortceva.eventscalendar.repository.WebCalendarRepository;

public class TaskViewModel extends AndroidViewModel {
    private WebCalendarRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private Date selectedDay;
    private LiveData<List<CalendarDay>> busyDays;

    public TaskViewModel(Application application) {
        super(application);

        repository = WebCalendarRepository.getInstance();
        selectedDay = new Date(System.currentTimeMillis());
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

    public void insert(Task task) {
        repository.insertTask(task);
    }
    public void update(Task task) {
        repository.updateTask(task);
    }
    public void delete(Task task) {
        repository.deleteTask(task);
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
}
