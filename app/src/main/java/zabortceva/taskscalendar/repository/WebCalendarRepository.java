package zabortceva.taskscalendar.repository;

import android.app.Application;
import android.util.Log;

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
import androidx.lifecycle.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import zabortceva.taskscalendar.localdata.Event;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.requests.EventsApi;
import zabortceva.taskscalendar.requests.TasksApi;
import zabortceva.taskscalendar.serverdata.Events;
import zabortceva.taskscalendar.serverdata.ServerDatabase;
import zabortceva.taskscalendar.serverdata.Tasks;

public class WebCalendarRepository implements CalendarRepository {
    private Retrofit serverDatabase;
    
    private TasksApi tasksApi;
//    final private MutableLiveData<List<Task>> allTasks;
//    final private MutableLiveData<List<Task>> dayTasks;
//    final private MutableLiveData<List<CalendarDay>> busyDays;
//    private Date selectedDay;

    private EventsApi eventsApi;
//    final private MutableLiveData<List<Event>> allEvents;

    public WebCalendarRepository(Application app) {
        serverDatabase = ServerDatabase.getInstance();
        tasksApi = ServerDatabase.getTasksTable();
        eventsApi = ServerDatabase.getEventsTable();
//        allTasks = new MutableLiveData<>();//tasksApi.getAllTasks();
//        busyDays = new MutableLiveData<>();//taskDao.getAllBusyDays();
//
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        selectedDay = new Date();
//        try {
//            selectedDay = formatter.parse(formatter.format(System.currentTimeMillis()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        dayTasks = new MutableLiveData<>();//tasksApi.getDayTasks(0);//taskDao.getDayTasks(new Timestamp(selectedDay.getTime()));
//
//        allEvents = new MutableLiveData<>();
    }

    @Override
    public void insertTask(Task task) {
        tasksApi.insert(task.getEvent_id(), task).enqueue(new Callback<Tasks>() {
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
    public void updateTask(Task task) {
        tasksApi.update(task.getId(), task).enqueue(new Callback<Tasks>() {
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
        tasksApi.delete(task.getId()).enqueue(new Callback<Void>() {
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

        eventsApi.getAllEvents(1000).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null)
                    data.setValue(Arrays.asList(response.body().getData()));
                else {
                    Log.v("GetAllEvents", "Null");
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

        eventsApi.getEventsByInterval(startOfDay, endOfDay).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null)
                    data.setValue(Arrays.asList(response.body().getData()));
                else {
                    Log.v("GetDayTasks", "Null");
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

        tasksApi.getAllTasks(1000).enqueue(new Callback<Tasks>() {
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
                }
                else {
                    Log.e("GetDayTasks", "Null");
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

        tasksApi.getAllTasks(1000).enqueue(new Callback<Tasks>() {
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

        tasksApi.getAllTasks(1000).enqueue(new Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                if (response.body() != null) {
                    List<CalendarDay> days = new ArrayList<>();
                    for (Task task : response.body().getData()) {
                        days.add(new CalendarDay(new Date(task.getDeadline_at())));
                    }
                    data.setValue(days);
                } else {
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
    public LiveData<Events> insertEvent(Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        eventsApi.insert(event).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    Log.v("InsertEvent", "Null");
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

        eventsApi.getEventById(id).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                if (response.body() != null) {
                    data.setValue(response.body().getData()[0]);
                } else {
                    Log.v("GetEventById", "Null");
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {

            }
        });

        return data;
    }
}
