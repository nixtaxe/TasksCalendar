package zabortceva.eventscalendar.repository;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
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

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.requests.EventsApi;
import zabortceva.eventscalendar.requests.TasksApi;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.serverdata.ServerDatabase;
import zabortceva.eventscalendar.serverdata.Tasks;

public class WebCalendarRepository {
    private static WebCalendarRepository repository;
    private Retrofit serverDatabase;

    private TasksApi tasksApi;
    private EventsApi eventsApi;

    private WebCalendarRepository() {
        serverDatabase = ServerDatabase.getInstance();
        tasksApi = ServerDatabase.getTasksTable();
        eventsApi = ServerDatabase.getEventsTable();
    }

    public static synchronized WebCalendarRepository getInstance() {
        if (repository == null) {
            repository = new WebCalendarRepository();
        }

        return repository;
    }

    public LiveData<Tasks> insertTask(final Task newTask) {
        final MutableLiveData<Tasks> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                tasksApi.insert(newTask.getEvent_id(), newTask, task.getResult().getToken()).enqueue(new Callback<Tasks>() {
                    @Override
                    public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            Log.v("InsertTask", String.valueOf(response.code()));
                        } else {
                            Log.e("InsertTask", String.valueOf(response.code()));
                        }

                    }

                    @Override
                    public void onFailure(Call<Tasks> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<Tasks> updateTask(final Task current_task) {
        final MutableLiveData<Tasks> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                tasksApi.update(current_task.getId(), current_task, idToken).enqueue(new Callback<Tasks>() {
                    @Override
                    public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            Log.v("UpdateTask", String.valueOf(response.code()));
                        } else {
                            Log.e("UpdateTask", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Tasks> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<Tasks> deleteTask(final Task current_task) {
        final MutableLiveData<Tasks> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                tasksApi.delete(current_task.getId(), idToken).enqueue(new Callback<Tasks>() {
                    @Override
                    public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Tasks> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<List<Event>> getAllEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.getAllEvents(1000, idToken).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        if (response.body() != null) {
                            data.setValue(Arrays.asList(response.body().getData()));
                            Log.v("GetAllEvents", String.valueOf(response.code()));
                        } else {
                            Log.e("GetAllEvents", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

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

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                tasksApi.getAllTasks(1000, idToken).enqueue(new Callback<Tasks>() {
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
                        } else {
                            Log.wtf("GetDayTasks", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Tasks> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<List<Task>> getAllTasks() {
        final MutableLiveData<List<Task>> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                tasksApi.getAllTasks(1000, idToken).enqueue(new Callback<Tasks>() {
                    @Override
                    public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                        if (response.body() != null) {
                            data.setValue(Arrays.asList(response.body().getData()));
                            Log.v("GetAllTasks", String.valueOf(response.code()));
                        } else {
                            Log.wtf("GetAllTasks", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Tasks> call, Throwable t) {
                    }
                });
            }
        });

        return data;
    }

    public LiveData<List<CalendarDay>> getAllBusyDays() {
        final MutableLiveData<List<CalendarDay>> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
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
            }
        });

        return data;
    }

    public LiveData<Events> insertEvent(final Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
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
            }
        });

        return data;
    }

    public LiveData<Event> getEventById(final long id) {
        final MutableLiveData<Event> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
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
            }
        });

        return data;
    }

    public LiveData<Events> deleteEvent(final Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.delete(event.getId(), idToken).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            Log.v("DeleteEvent", String.valueOf(response.code()));
                        }
                        else {
                            Log.e("DeleteTask", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }

    public LiveData<Events> updateEvent(final Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.update(event.getId(), event, idToken).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());

                        }
                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }
}
