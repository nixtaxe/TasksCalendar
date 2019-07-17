package zabortceva.eventscalendar.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.repository.WebCalendarRepository;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.serverdata.Patterns;
import zabortceva.eventscalendar.view.EventsAdapter;
import zabortceva.eventscalendar.view.model.EventViewModel;
import zabortceva.eventscalendar.view.TasksAdapter;

import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_EVENT;
import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_PATTERN;

public class CalendarActivity extends AppCompatActivity {

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;
    
    private ArrayList<String> events;
    private MaterialCalendarView calendarView;
    private RecyclerView eventsView;
    private FloatingActionButton addEventButton;
    private FloatingActionButton viewAccountButton;
    final EventsAdapter eventsAdapter = new EventsAdapter();
    private Observer<List<Event>> dayEventsObserver;
    private Observer<List<CalendarDay>> busyDaysObserver;
    private EventDecorator busyDaysDecorator = new EventDecorator(0, new ArrayList<CalendarDay>());

    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

        viewAccountButton = findViewById(R.id.view_account_button);
        viewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, LoginActivity.class));
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            viewAccountButton.performClick();

        eventsView = findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventsView.setLayoutManager(layoutManager);
        eventsView.setHasFixedSize(true);
        eventsView.setAdapter(eventsAdapter);

        dayEventsObserver = new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventsAdapter.submitList(events);
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
        eventViewModel.getAllBusyDays().observe(this, busyDaysObserver);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                eventViewModel.delete(eventsAdapter.getEventAt(viewHolder.getAdapterPosition()));
                updateAllObservers();
                Toast.makeText(CalendarActivity.this, R.string.task_was_deleted, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(eventsView);

        eventsAdapter.setOnItemClickListener(new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                //TODO implement
            }
        });

        calendarView = findViewById(R.id.calendar_view);
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

        addEventButton = findViewById(R.id.add_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddEditEventActivity.class);
                Timestamp dateTime = new Timestamp(calendarView.getSelectedDate().getDate().getTime());
                intent.putExtra(AddEditEventActivity.EXTRA_STARTS_AT, dateTime.getTime());
                
                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });
    }

    private void updateAllObservers() {
        observeNewDay(new Timestamp(calendarView.getSelectedDate().getDate().getTime()));
        observeBusyDays();
    }

    private void observeNewDay(Timestamp timestamp) {
        LiveData<List<Event>> dayEvents = eventViewModel.getSavedDayEvents();
        if (dayEvents != null && dayEvents.hasObservers())
            dayEvents.removeObserver(dayEventsObserver);

        eventViewModel.getDayEvents(timestamp).observe(this, dayEventsObserver);
    }

    private void observeBusyDays() {
        LiveData<List<CalendarDay>> allBusyDays = eventViewModel.getSavedAllBusyDays();
        if (allBusyDays != null && allBusyDays.hasObservers())
            allBusyDays.removeObserver(busyDaysObserver);

        eventViewModel.getAllBusyDays().observe(this, busyDaysObserver);
    }

    Observer<Events> responseObserver = new Observer<Events>() {
        @Override
        public void onChanged(Events response) {
            if (response.isSuccess()) {

                Toast.makeText(CalendarActivity.this, R.string.event_was_saved, Toast.LENGTH_SHORT).show();
                updateAllObservers();
            }
            else
                Toast.makeText(CalendarActivity.this, R.string.changes_was_not_saved, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {
            final Event event = (new Gson()).fromJson(data.getStringExtra(EXTRA_EVENT), Event.class);
            final Pattern pattern = (new Gson()).fromJson(data.getStringExtra(EXTRA_PATTERN), Pattern.class);
            eventViewModel.insert(event).observe(this, new Observer<Events>() {
                @Override
                public void onChanged(Events events) {
                    if (events.isSuccess()) {
                        WebCalendarRepository.getInstance().insertPattern(events.getData()[0].getId(), pattern).observe(CalendarActivity.this, new Observer<Patterns>() {
                            @Override
                            public void onChanged(Patterns patterns) {
                                if (patterns.isSuccess())
                                    Toast.makeText(CalendarActivity.this, "Pattern inserted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        } else if (requestCode == EDIT_EVENT_REQUEST && resultCode == RESULT_OK) {
            final Event event = (new Gson()).fromJson(data.getStringExtra(EXTRA_EVENT), Event.class);
            final Pattern pattern = (new Gson()).fromJson(data.getStringExtra(EXTRA_PATTERN), Pattern.class);
            eventViewModel.update(event);

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
