package zabortceva.taskscalendar.repository;

import android.app.Application;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.requests.TasksApi;
import zabortceva.taskscalendar.serverdata.ServerDatabase;
import zabortceva.taskscalendar.serverdata.Tasks;

public class WebCalendarRepository implements CalendarRepository {
    private Retrofit serverDatabase;
    private TasksApi tasksApi;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private LiveData<List<CalendarDay>> busyDays;
    private Date selectedDay;

    public WebCalendarRepository(Application app) {
        serverDatabase = ServerDatabase.getInstance();
        tasksApi = ServerDatabase.getTasksTable();
        allTasks = null;//tasksApi.getAllTasks();
        busyDays = null;//taskDao.getAllBusyDays();
        selectedDay = new Date(System.currentTimeMillis());
        dayTasks = null;//tasksApi.getDayTasks(0);//taskDao.getDayTasks(new Timestamp(selectedDay.getTime()));
    }

    @Override
    public void insert(Task task) {
        //Retrofit serverDatabase = ServerDatabase.getInstance();
    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void delete(Task task) {

    }

    @Override
    public LiveData<List<Task>> getDayTasks(Timestamp day) {
//        dayTasks = taskApi.getDayTasks(day);
        final MutableLiveData<List<Task>> data = new MutableLiveData<>();

        tasksApi.getDayTasks(10).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null)
                    data.setValue(Arrays.asList(response.body().getData()));
                else {
                    Log.v("GetDayTasks", "Null");
                }
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

    @Override
    public LiveData<List<Task>> getAllTasks() {
        final MutableLiveData<List<Task>> data = new MutableLiveData<>();

        tasksApi.getAllTasks(484L).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null)
                    data.setValue(Arrays.asList(response.body().getData()));
                else {
                    Log.v("GetAllTasks", "Null");
                }
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                //
            }
        });

        return data;
    }

    @Override
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        final MutableLiveData<List<CalendarDay>> data = new MutableLiveData<>();
        data.setValue(Arrays.asList(new CalendarDay()));

        return data;
    }
}
