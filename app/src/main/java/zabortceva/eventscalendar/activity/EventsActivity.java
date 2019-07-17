package zabortceva.eventscalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.serverdata.Events;
import zabortceva.eventscalendar.view.model.EventViewModel;
import zabortceva.eventscalendar.view.EventsAdapter;

public class EventsActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 2;
    public static final String EXTRA_TASK_EVENTS_NAME = "zabortceva.eventscalendar.EXTRA_TASK_EVENTS_NAME";

    private EventViewModel eventViewModel;
    private ArrayList<String> eventsName;

    final EventsAdapter eventsAdapter = new EventsAdapter();

    private RecyclerView eventsView;
    private TextView event;
    private TextView editDeadlineAtTime;

    private FloatingActionButton addEventButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);

        eventsView = findViewById(R.id.eventsView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventsView.setLayoutManager(layoutManager);
        eventsView.setHasFixedSize(true);
        eventsView.setAdapter(eventsAdapter);

        eventsAdapter.setOnItemClickListener(new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Intent intent = new Intent(EventsActivity.this, AddEditEventActivity.class);

                intent.putExtra(AddEditEventActivity.EXTRA_EVENT, (new Gson()).toJson(event));

                startActivityForResult(intent, EDIT_EVENT_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                eventViewModel.delete(eventsAdapter.getEventAt(viewHolder.getAdapterPosition()));
                Toast.makeText(EventsActivity.this, R.string.task_was_deleted, Toast.LENGTH_SHORT).show();
                updateEvents();
            }
        }).attachToRecyclerView(eventsView);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventsAdapter.submitList(events);
            }
        });

        addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsActivity.this, AddEditEventActivity.class);

                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });
    }

    public void updateEvents() {
        LiveData<List<Event>> events = eventViewModel.getSavedAllEvents();
        if (events != null && events.hasActiveObservers())
            events.removeObservers(this);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventsAdapter.submitList(events);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TextView selectedDate;


}
