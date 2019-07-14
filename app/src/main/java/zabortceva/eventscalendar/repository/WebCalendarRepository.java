package zabortceva.eventscalendar.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.requests.EventsApi;
import zabortceva.eventscalendar.requests.MyFirebaseMessagingService;
import zabortceva.eventscalendar.requests.TasksApi;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.serverdata.ServerDatabase;
import zabortceva.eventscalendar.serverdata.Tasks;

public class WebCalendarRepository implements CalendarRepository {
    private static WebCalendarRepository repository;
    private Retrofit serverDatabase;
    
    private TasksApi tasksApi;
    private EventsApi eventsApi;
    private String idToken;
    private Context context;

    private WebCalendarRepository(Application app) {
        context = app;
        serverDatabase = ServerDatabase.getInstance();
        tasksApi = ServerDatabase.getTasksTable();
        eventsApi = ServerDatabase.getEventsTable();
    }

    public static synchronized WebCalendarRepository getInstance(Application app) {
        if (repository == null) {
            repository = new WebCalendarRepository(app);
        }

        return repository;
    }

    @Override
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public void insertTask(Task task) {
        tasksApi.insert(task.getEvent_id(), task, idToken).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                Log.v("InsertTask", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void updateTask(Task task) {
        tasksApi.update(task.getId(), task, idToken).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                //
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void deleteTask(Task task) {
        tasksApi.delete(task.getId(), idToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public LiveData<List<Event>> getAllEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();

        String token = MyFirebaseMessagingService.getToken(context);

        eventsApi.getAllEvents(1000, token).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(Arrays.asList(response.body().getData()));
                    Log.v("GetAllEvents", String.valueOf(response.code()));
                }
                else {
                    Log.e("GetAllEvents", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

    public LiveData<List<Event>> getDayEvents(Timestamp day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        long startOfDay = day.getTime();
        long endOfDay = calendar.getTimeInMillis();

        final MutableLiveData<List<Event>> data = new MutableLiveData<>();

        eventsApi.getEventsByInterval(startOfDay, endOfDay, idToken).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(Arrays.asList(response.body().getData()));
                    Log.v("GetDayEvents", String.valueOf(response.code()));
                }
                else {
                    Log.e("GetDayEvents", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return data;
    }

    @Override
    public LiveData<List<Task>> getDayTasks(Timestamp timestamp) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date day = new Date();
        try {
            day = formatter.parse(formatter.format(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        final long startOfDay = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        final long endOfDay = calendar.getTimeInMillis();

        final MutableLiveData<List<Task>> data = new MutableLiveData<>();

        String token = MyFirebaseMessagingService.getToken(context);

        tasksApi.getAllTasks(1000, token).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null) {
                    List<Task> dayTasks = new ArrayList<>();
                    for (Task task : response.body().getData()) {
                        if (task.getDeadline_at() <= endOfDay && task.getDeadline_at() >= startOfDay) {
                            dayTasks.add(task);
                        }
                    }
                    Collections.sort(dayTasks, new Comparator<Task>() {
                        @Override
                        public int compare(Task lhs, Task rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            return lhs.getDeadline_at() < rhs.getDeadline_at() ? -1 : (lhs.getDeadline_at() > rhs.getDeadline_at()) ? 1 : 0;
                        }
                    });
                    data.setValue(dayTasks);
                    Log.v("GetDayTasks", String.valueOf(response.code()));
                }
                else {
                    Log.wtf("GetDayTasks", String.valueOf(response.code()));
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

        tasksApi.getAllTasks(1000, idToken).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null) {
                    data.setValue(Arrays.asList(response.body().getData()));
                    Log.v("GetAllTasks", String.valueOf(response.code()));
                }
                else {
                    Log.wtf("GetAllTasks", String.valueOf(response.code()));
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

        tasksApi.getAllTasks(1000, idToken).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null) {
                    List<CalendarDay> days = new ArrayList<>();
                    for (Task task : response.body().getData()) {
                        days.add(new CalendarDay(new Date(task.getDeadline_at())));
                    }
                    data.setValue(days);
                    Log.v("GetBusyDays", String.valueOf(response.code()));
                } else {
                    Log.wtf("GetBusyDays", String.valueOf(response.code()));
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
    public LiveData<Events> insertEvent(Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        eventsApi.insert(event, idToken).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                    Log.v("InsertEvent", String.valueOf(response.code()));
                } else {
                    Log.e("InsertEvent", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });

        return data;
    }

    @Override
    public LiveData<Event> getEventById(long id) {
        final MutableLiveData<Event> data = new MutableLiveData<>();

        eventsApi.getEventById(id, idToken).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(response.body().getData()[0]);
                } else {
                    Log.v("GetEventById", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });

        return data;
    }

    @Override
    public void deleteEvent(Event event) {
        eventsApi.delete(event.getId(), idToken).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {

            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });
    }

    @Override
    public void updateEvent(Event event) {
        eventsApi.update(event.getId(), event, idToken).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {

            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });
    }
}
