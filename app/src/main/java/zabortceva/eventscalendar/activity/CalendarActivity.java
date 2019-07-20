package zabortceva.eventscalendar.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.Period;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Pair;
import android.view.View;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.repository.DateRangeHelper;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.serverdata.FullEvent;
import zabortceva.eventscalendar.serverdata.Instance;
import zabortceva.eventscalendar.serverdata.Patterns;
import zabortceva.eventscalendar.view.FullEventsAdapter;
import zabortceva.eventscalendar.view.model.EventViewModel;
import zabortceva.eventscalendar.view.model.PatternViewModel;

import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_EVENT;
import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_INSTANCE;
import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_PATTERN;

public class CalendarActivity extends AppCompatActivity {

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;
    
    private ArrayList<String> events;
    private MaterialCalendarView calendarView;
    private RecyclerView eventsView;
    private FloatingActionButton addEventButton;
    private FloatingActionButton viewAccountButton;
    final FullEventsAdapter eventsAdapter = new FullEventsAdapter();
    private Observer<List<FullEvent>> dayEventsObserver;
//    private Observer<List<CalendarDay>> busyDaysObserver;
    private Observer<List<FullEvent>> monthEventsObserver;
    private EventDecorator busyDaysDecorator = new EventDecorator(0, new ArrayList<CalendarDay>());
    private List<FullEvent> monthEvents = new ArrayList<>();

    private EventViewModel eventViewModel;
    private PatternViewModel patternViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        patternViewModel = ViewModelProviders.of(this).get(PatternViewModel.class);

//        final WeekView<FullEvent> weekView = findViewById(R.id.weekView);
//        weekView.setMonthChangeListener(new MonthChangeListener<FullEvent>() {
//            @Override
//            public List<WeekViewDisplayable<FullEvent>> onMonthChange(Calendar calendar, Calendar calendar1) {
//                return new ArrayList<>();
//            }
//        });

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

        dayEventsObserver = new Observer<List<FullEvent>>() {
            @Override
            public void onChanged(final List<FullEvent> events) {
                eventsAdapter.submitList(events);
//                weekView.setMonthChangeListener(new MonthChangeListener<FullEvent>() {
//                    @Override
//                    public List<WeekViewDisplayable<FullEvent>> onMonthChange(Calendar calendar, Calendar calendar1) {
//                        List<WeekViewDisplayable<FullEvent>> list = new ArrayList<>();
//                        list.addAll(events);
//                        return list;
//                    }
//                });
            }
        };
        //observeNewDay(new Timestamp(System.currentTimeMillis()));

        monthEventsObserver = new Observer<List<FullEvent>>() {
            @Override
            public void onChanged(List<FullEvent> fullEvents) {
                monthEvents = fullEvents;
                HashSet<CalendarDay> busyDays = new HashSet<>();
                for(FullEvent fullEvent : fullEvents)
                    busyDays.add(new CalendarDay(new Date(fullEvent.getInstance().getStarted_at())));
                if (busyDaysDecorator.getDates().equals(busyDays))
                    return;

                calendarView.removeDecorator(busyDaysDecorator);
                busyDaysDecorator.setDates(busyDays);
                calendarView.addDecorator(busyDaysDecorator);
            }
        };

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                eventViewModel.delete(eventsAdapter.getEventAt(viewHolder.getAdapterPosition()).getEvent()).observe(CalendarActivity.this, new Observer<Events>() {
                    @Override
                    public void onChanged(Events events) {
                        updateAllObservers();
                        Toast.makeText(CalendarActivity.this, R.string.task_was_deleted, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).attachToRecyclerView(eventsView);

        eventsAdapter.setOnItemClickListener(new FullEventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FullEvent event) {
                Intent intent = new Intent(CalendarActivity.this, AddEditEventActivity.class);
                intent.putExtra(EXTRA_EVENT, (new Gson()).toJson(event.getEvent()));
                intent.putExtra(EXTRA_PATTERN, (new Gson()).toJson(event.getPattern()));
                intent.putExtra(EXTRA_INSTANCE, (new Gson()).toJson(event.getInstance()));
                startActivityForResult(intent, EDIT_EVENT_REQUEST);
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
                List<FullEvent> dayEvents = new ArrayList<>();
                Date day = calendarDay.getDate();
                Pair<Date, Date> dayRange = DateRangeHelper.getDayDateRange(day);
                long startOfDay = dayRange.first.getTime(), endOfDay = dayRange.second.getTime();
                for (FullEvent fullEvent : monthEvents) {
                    Instance instance = fullEvent.getInstance();
                    if (instance.getStarted_at() >= startOfDay && instance.getStarted_at() <= endOfDay ||
                    instance.getEnded_at() >= startOfDay && instance.getEnded_at() <= endOfDay)
                        dayEvents.add(fullEvent);
                }
                eventsAdapter.submitList(dayEvents);
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Pair<Date, Date> monthRange = DateRangeHelper.getDateRange(date.getDate());
                observeNewMonth(monthRange.first, monthRange.second);
            }
        });
        calendarView.setSelectedDate(new Date(System.currentTimeMillis()));
        updateAllObservers();

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
        Pair<Date, Date> dateRange = DateRangeHelper.getDateRange(calendarView.getSelectedDate().getDate());
        observeNewMonth(dateRange.first, dateRange.second);
    }

    private void observeNewMonth(Date start, Date end) {
        LiveData<List<FullEvent>> monthEvents = eventViewModel.getSavedMonthFullEvents();
        if (monthEvents != null && monthEvents.hasObservers())
            monthEvents.removeObserver(monthEventsObserver);

        eventViewModel.getMonthFullEvents(start, end).observe(this, monthEventsObserver);
    }

    private void observeNewDay(Timestamp timestamp) {
        LiveData<List<FullEvent>> dayEvents = eventViewModel.getSavedDayFullEvents();
        if (dayEvents != null && dayEvents.hasObservers())
            dayEvents.removeObserver(dayEventsObserver);

        eventViewModel.getDayFullEvents(timestamp).observe(this, dayEventsObserver);
    }

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
                        patternViewModel.insert(events.getData()[0].getId(), pattern).observe(CalendarActivity.this, new Observer<Patterns>() {
                            @Override
                            public void onChanged(Patterns patterns) {
                                if (patterns.isSuccess()) {
                                    Toast.makeText(CalendarActivity.this, "Pattern inserted", Toast.LENGTH_SHORT).show();
                                    updateAllObservers();
                                }
                            }
                        });
                    }
                }
            });
        } else if (requestCode == EDIT_EVENT_REQUEST && resultCode == RESULT_OK) {
            final Event event = (new Gson()).fromJson(data.getStringExtra(EXTRA_EVENT), Event.class);
            final Pattern pattern = (new Gson()).fromJson(data.getStringExtra(EXTRA_PATTERN), Pattern.class);
            eventViewModel.update(event).observe(this, new Observer<Events>() {
                @Override
                public void onChanged(Events events) {
                    if (events.isSuccess()) {
                        patternViewModel.update(pattern).observe(CalendarActivity.this, new Observer<Patterns>() {
                            @Override
                            public void onChanged(Patterns patterns) {
                                if (patterns.isSuccess()) {
                                    Toast.makeText(CalendarActivity.this, "Event updated", Toast.LENGTH_SHORT).show();
                                    updateAllObservers();
                                }
                            }
                        });
                    }
                }
            });
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
