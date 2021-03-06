package zabortceva.eventscalendar.repository;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import scala.util.parsing.combinator.testing.Str;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.requests.EventsApi;
import zabortceva.eventscalendar.requests.PatternsApi;
import zabortceva.eventscalendar.requests.PermissionsApi;
import zabortceva.eventscalendar.requests.TasksApi;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.serverdata.FullEvent;
import zabortceva.eventscalendar.serverdata.Instance;
import zabortceva.eventscalendar.serverdata.Instances;
import zabortceva.eventscalendar.serverdata.Patterns;
import zabortceva.eventscalendar.serverdata.Permissions;
import zabortceva.eventscalendar.serverdata.ServerApi;
import zabortceva.eventscalendar.serverdata.Tasks;
import zabortceva.eventscalendar.serverdata.User;

public class ServerCalendarRepository {
    public static final String TAG = "ServerCalendar";

    private static ServerCalendarRepository repository;
    private FirebaseAuth auth;
    private ServerApi serverApi;

    private TasksApi tasksApi;
    private EventsApi eventsApi;
    private PermissionsApi permissionsApi;
    private PatternsApi patternsApi;

    private ServerCalendarRepository() {
        serverApi = ServerApi.getInstance();
        tasksApi = serverApi.getTasksApi();
        eventsApi = serverApi.getEventsApi();
        permissionsApi = serverApi.getPermissionsApi();
        patternsApi = serverApi.getPatternsApi();
        
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized ServerCalendarRepository getInstance() {
        if (repository == null) {
            repository = new ServerCalendarRepository();
        }

        return repository;
    }

    public LiveData<Tasks> insertTask(final Task newTask) {
        final MutableLiveData<Tasks> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

    public LiveData<List<Event>> getDayEvents(final Timestamp timestamp) {
        Pair<Date, Date> dayRange = DateRangeHelper.getDayDateRange(new Date(timestamp.getTime()));
        final long startOfDay = dayRange.first.getTime(), endOfDay = dayRange.second.getTime();

        final MutableLiveData<List<Event>> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.getEventsByInterval(startOfDay, endOfDay, idToken).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        if (response.body() != null) {
                            List<Event> list = Arrays.asList(response.body().getData());
                            Collections.sort(list, new Comparator<Event>() {
                                @Override
                                public int compare(Event lhs, Event rhs) {
                                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                    return lhs.getCreated_at() < rhs.getCreated_at() ? -1 : (lhs.getCreated_at() > rhs.getCreated_at()) ? 1 : 0;
                                }
                            });
                            data.setValue(list);
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

    public LiveData<List<FullEvent>> getMonthFullEvents(final Date start, final Date end) {
        final long startOfDay = start.getTime(), endOfDay = end.getTime();

        final MutableLiveData<List<FullEvent>> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                final String idToken = task.getResult().getToken();
                eventsApi.getInstances(startOfDay, endOfDay, idToken).enqueue(new Callback<Instances>() {
                    @Override
                    public void onResponse(Call<Instances> call, Response<Instances> response) {

                        Log.e(TAG, "log: -----------------------------");
                        Log.d(TAG, "onResponse: " + response.body());

                        if(response.raw().networkResponse() != null){
                            Log.d(TAG, "onResponse: response is from NETWORK...");
                        }
                        else if(response.raw().cacheResponse() != null
                                && response.raw().networkResponse() == null){
                            Log.d(TAG, "onResponse: response is from CACHE...");
                        }

                        if (response.body() != null) {
                            List<Instance> list = Arrays.asList(response.body().getData());
                            Collections.sort(list, new Comparator<Instance>() {
                                @Override
                                public int compare(Instance lhs, Instance rhs) {
                                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                    return lhs.getStarted_at() < rhs.getStarted_at() ? -1 : (lhs.getStarted_at() > rhs.getStarted_at()) ? 1 :
                                            (lhs.getEnded_at() < rhs.getEnded_at()) ? -1 : (lhs.getEnded_at() > rhs.getEnded_at()) ? 1 : 0;
                                }
                            });
                            List<FullEvent> fullEvents = new ArrayList<>();
                            for (Instance instance : list) {
                                FullEvent fullEvent = new FullEvent();
                                fullEvent.setInstance(instance);
                                fullEvents.add(fullEvent);
                            }
                            final MutableLiveData<List<FullEvent>> buffer = new MutableLiveData<>();
                            buffer.setValue(fullEvents);

                            eventsApi.getEventsByInterval(startOfDay, endOfDay, idToken).enqueue(new Callback<Events>() {
                                @Override
                                public void onResponse(Call<Events> call, Response<Events> response) {
                                    HashMap<Long, Event> eventHashMap = new HashMap<>();
                                    List<Event> events = Arrays.asList(response.body().getData());
                                    for (Event event : events) {
                                        eventHashMap.put(event.getId(), event);
                                    }
                                    List<FullEvent> fullEvents = new ArrayList<>(buffer.getValue());
                                    for (FullEvent fullEvent : fullEvents) {
                                        fullEvent.setEvent(eventHashMap.get(fullEvent.getInstance().getEvent_id()));
                                    }
                                    buffer.setValue(fullEvents);
                                    patternsApi.getPatternByInterval(startOfDay, endOfDay, idToken).enqueue(new Callback<Patterns>() {
                                        @Override
                                        public void onResponse(Call<Patterns> call, Response<Patterns> response) {
                                            HashMap<Long, Pattern> patternHashMap = new HashMap<>();
                                            List<Pattern> patterns = Arrays.asList(response.body().getData());
                                            for (Pattern pattern : patterns) {
                                                patternHashMap.put(pattern.getId(), pattern);
                                            }
                                            List<FullEvent> fullEvents = new ArrayList<>(buffer.getValue());
                                            for (int i = 0; i < fullEvents.size(); ++i) {
                                                FullEvent fullEvent = fullEvents.get(i);
                                                fullEvent.setPattern(patternHashMap.get(fullEvent.getInstance().getPattern_id()));
                                                fullEvents.set(i, fullEvent);
                                            }
                                            data.setValue(fullEvents);
                                        }

                                        @Override
                                        public void onFailure(Call<Patterns> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<Events> call, Throwable t) {

                                }
                            });

//                            data.setValue(fullEvents);
                            Log.v("GetMonthFullEvents", String.valueOf(response.code()));
                        } else {
                            Log.e("GetMonthFullEvents", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Instances> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<List<Task>> getTasksByEventId(final long id) {
        final MutableLiveData<List<Task>> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                tasksApi.getEventTasks(id, idToken).enqueue(new Callback<Tasks>() {
                    @Override
                    public void onResponse(Call<Tasks> call, Response<Tasks> response) {
                        if (response.body() != null) {
                            List<Task> dayTasks = Arrays.asList(response.body().getData());
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.getAllInstances(100, idToken).enqueue(new Callback<Instances>() {
                    @Override
                    public void onResponse(Call<Instances> call, Response<Instances> response) {
                        if (response.body() != null) {
                            List<CalendarDay> days = new ArrayList<>();
                            for (Instance instance : response.body().getData()) {
                                days.add(new CalendarDay(new Date(instance.getStarted_at())));
                            }
                            data.setValue(days);
                            Log.v("GetBusyDays", String.valueOf(response.code()));
                        } else {
                            Log.wtf("GetBusyDays", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Instances> call, Throwable t) {
                        //
                    }
                });
            }
        });

        return data;
    }

    public LiveData<Events> insertEvent(final Event event) {
        final MutableLiveData<Events> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                eventsApi.delete(event.getId(), idToken).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            Log.v("DeleteEvent", String.valueOf(response.code()));
                        } else {
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

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
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

    public LiveData<List<Permission>> getAllPermissions(final String entity_type) {
        final MutableLiveData<List<Permission>> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                permissionsApi.getSharedByYou(entity_type, idToken).enqueue(new Callback<Permissions>() {
                    @Override
                    public void onResponse(Call<Permissions> call, Response<Permissions> response) {
                        if (response.body() != null) {
                            data.setValue(Arrays.asList(response.body().getData()));
                            Log.v("GetAllPermissions", String.valueOf(response.code()));
                        } else {
                            Log.e("GetAllPermissions", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Permissions> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return data;
    }

    public LiveData<String> insertPermission(final String user_id, final String entity_type, final String action) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                final String idToken = task.getResult().getToken();
                if (user_id == null)
                    permissionsApi.shareOne(null, entity_type, action, idToken).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful())
                                data.setValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                else {
                    permissionsApi.findUser(user_id, idToken).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            String user_id = response.body().getId();
                            permissionsApi.grant(user_id, null, entity_type, action, idToken).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });
        return data;
    }

    public LiveData<Pattern> getPatternById(final long id) {
        final MutableLiveData<Pattern> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                patternsApi.getPatternById(id, idToken).enqueue(new Callback<Patterns>() {
                    @Override
                    public void onResponse(Call<Patterns> call, Response<Patterns> response) {
                        data.setValue(response.body().getData()[0]);
                    }

                    @Override
                    public void onFailure(Call<Patterns> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }

    public LiveData<Patterns> insertPattern(final long event_id, final Pattern pattern) {
        final MutableLiveData<Patterns> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                patternsApi.insertPattern(event_id, pattern, idToken).enqueue(new Callback<Patterns>() {
                    @Override
                    public void onResponse(Call<Patterns> call, Response<Patterns> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Patterns> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }

    public LiveData<Patterns> updatePattern(final Pattern pattern) {
        final MutableLiveData<Patterns> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                patternsApi.updatePattern(pattern.getId(), pattern, idToken).enqueue(new Callback<Patterns>() {
                    @Override
                    public void onResponse(Call<Patterns> call, Response<Patterns> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Patterns> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }

    public LiveData<Patterns> deletePattern(final long id) {
        final MutableLiveData<Patterns> data = new MutableLiveData<>();

        auth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<GetTokenResult> task) {
                String idToken = task.getResult().getToken();
                patternsApi.deletePattern(id, idToken).enqueue(new Callback<Patterns>() {
                    @Override
                    public void onResponse(Call<Patterns> call, Response<Patterns> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Patterns> call, Throwable t) {

                    }
                });
            }
        });

        return data;
    }

}
