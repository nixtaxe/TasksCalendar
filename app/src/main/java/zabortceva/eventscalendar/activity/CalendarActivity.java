package zabortceva.eventscalendar.activity;

import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;

import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.view.EventViewModel;
import zabortceva.eventscalendar.view.TaskViewModel;
import zabortceva.eventscalendar.view.TasksAdapter;

public class CalendarActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    //TODO adjust insertion into table

    //TODO figure out how to implement events and users
    private ArrayList<String> events;
    private MaterialCalendarView calendarView;
    private RecyclerView tasksView;
    private FloatingActionButton addTaskButton;
    private FloatingActionButton viewEventsButton;
    private FloatingActionButton viewAccountButton;
    final TasksAdapter tasksAdapter = new TasksAdapter();
    private Observer<List<Task>> dayTasksObserver;
    private Observer<List<CalendarDay>> busyDaysObserver;
    private EventDecorator busyDaysDecorator = new EventDecorator(0, new ArrayList<CalendarDay>());

    private TaskViewModel taskViewModel;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        tasksView = findViewById(R.id.tasksView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tasksView.setLayoutManager(layoutManager);
        tasksView.setHasFixedSize(true);
        tasksView.setAdapter(tasksAdapter);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        dayTasksObserver = new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                tasksAdapter.submitList(tasks);
            }
        };
        observeNewDay(new Timestamp(System.currentTimeMillis()));

        busyDaysObserver = new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> busyDays) {
                HashSet<CalendarDay> newDays = new HashSet<CalendarDay>(busyDays);
                if (busyDaysDecorator.getDates().equals(newDays))
                    return;

                calendarView.removeDecorator(busyDaysDecorator);
                busyDaysDecorator.setDates(newDays);
                calendarView.addDecorator(busyDaysDecorator);
            }
        };
        taskViewModel.getAllBusyDays().observe(this, busyDaysObserver);

//        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onChanged(List<Task> tasks) {
//                Task task = tasks.get(tasks.size() - 1);
//                Calendar c = Calendar.getInstance();
//                c.set(Calendar.HOUR, task.getDeadline_at().getHours());
//                c.set(Calendar.MINUTE, task.getDeadline_at().getMinutes());
//                c.set(Calendar.SECOND, 0);
//
//                if (c.before(Calendar.getInstance())) {
//                    return;
//                }
//
//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(CalendarActivity.this, AlertReceiver.class);
//                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_NAME, task.getName());
//                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_DETAILS, task.getDetails());
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(CalendarActivity.this, 1, intent, 0);
//
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            }
//        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(tasksAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                updateAllObservers();
                Toast.makeText(CalendarActivity.this, R.string.task_was_deleted, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(tasksView);

        tasksAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(CalendarActivity.this, AddEditTaskActivity.class);

                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, task.getEvent_id());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, task.getEvent_id());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_NAME, task.getName());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_DETAILS, task.getDetails());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_DEADLINE_AT, new Timestamp(task.getDeadline_at()).toString());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_DEADLINE_AT, new Timestamp(task.getDeadline_at()).toString());

                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });

        calendarView = findViewById(R.id.calendarView);
        calendarView.addDecorator(busyDaysDecorator);
        calendarView.setTileHeightDp(40);
        calendarView.setTileWidthDp(60);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_DEFAULTS);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                //Mind the localtime in sql query
                observeNewDay(new Timestamp(calendarDay.getDate().getTime()));
            }
        });
        calendarView.setSelectedDate(new Date(System.currentTimeMillis()));

        addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddEditTaskActivity.class);

                Timestamp dateTime = new Timestamp(calendarView.getSelectedDate().getDate().getTime());
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_DEADLINE_AT, dateTime.toString());
                intent.putStringArrayListExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, events);

                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
//        events = new ArrayList<>();
//        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
//            @Override
//            public void onChanged(List<Event> new_events) {
//                events.clear();
//                for (Event event : new_events) {
//                    events.add(event.getName());
//                }
//            }
//        });

        viewEventsButton = findViewById(R.id.view_events_button);
        viewEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, EventsActivity.class);
                intent.putStringArrayListExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, events);

                startActivity(intent);
            }
        });

        viewAccountButton = findViewById(R.id.view_account_button);
        viewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, LoginActivity.class));
            }
        });

    }

    private void updateAllObservers() {
        observeNewDay(taskViewModel.getSelectedDay());
        observeBusyDays();
    }

    private void observeNewDay(Timestamp timestamp) {
        if (taskViewModel.getDayTasks(taskViewModel.getSelectedDay()).hasObservers())
            taskViewModel.getDayTasks(taskViewModel.getSelectedDay()).removeObserver(dayTasksObserver);

        taskViewModel.getDayTasks(timestamp).observe(this, dayTasksObserver);
    }

    private void observeBusyDays() {
        if (taskViewModel.getAllBusyDays().hasActiveObservers())
            taskViewModel.getAllBusyDays().removeObserver(busyDaysObserver);

        taskViewModel.getAllBusyDays().observe(this, busyDaysObserver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            long event_id = data.getLongExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, -1);
            String name = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_NAME);
            String details = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_DETAILS);
            Timestamp deadline_at = Timestamp.valueOf(data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_DEADLINE_AT));

            Task task = new Task(name, details, deadline_at);
            task.setEvent_id(event_id);
            taskViewModel.insert(task);

            updateAllObservers();

            Toast.makeText(this, R.string.task_was_saved, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            long id = data.getLongExtra(AddEditTaskActivity.EXTRA_TASK_ID, -1);
            if (id == -1) {
                Toast.makeText(this, R.string.task_can_not_be_updated, Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_NAME);
            String details = data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_DETAILS);
            Timestamp deadline_at = Timestamp.valueOf(data.getStringExtra(AddEditTaskActivity.EXTRA_TASK_DEADLINE_AT));
            long event_id = data.getLongExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, -1);

            Task task = new Task(name, details, deadline_at);
            task.setId(id);
            task.setEvent_id(event_id);
            taskViewModel.update(task);

            updateAllObservers();

            Toast.makeText(this, R.string.task_was_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.changes_was_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        public void setDates(HashSet<CalendarDay> dates) {
            this.dates = dates;
        }

        public HashSet<CalendarDay> getDates() {
            return dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {

            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
}
