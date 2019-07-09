package zabortceva.taskscalendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import zabortceva.taskscalendar.localdata.Event;
import zabortceva.taskscalendar.repository.CalendarRepository;
import zabortceva.taskscalendar.serverdata.Events;
import zabortceva.taskscalendar.view.EventViewModel;
import zabortceva.taskscalendar.view.EventsAdapter;

public class EventsActivity extends AppCompatActivity {
    public static final int ADD_EVENT_REQUEST = 1;
    public static final int EDIT_EVENT_REQUEST = 1;
    public static final String EXTRA_TASK_EVENTS_NAME = "zabortceva.taskscalendar.EXTRA_TASK_EVENTS_NAME";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditEventActivity.EXTRA_EVENT_NAME);
            String details = data.getStringExtra(AddEditEventActivity.EXTRA_EVENT_DETAILS);
            String location = data.getStringExtra(AddEditEventActivity.EXTRA_EVENT_LOCATION);
            String status = data.getStringExtra(AddEditEventActivity.EXTRA_EVENT_STATUS);

            Event event = new Event(details, location, name, status);
            eventViewModel.insert(event).observe(this, new Observer<Events>() {
                @Override
                public void onChanged(Events result) {
                    if (result.isSuccess())
                        Log.v("InsertEvent", "OK");
                    else
                        Log.e("InsertEvent", result.getMessage());;
                }
            });

        } else if (requestCode == EDIT_EVENT_REQUEST && resultCode == RESULT_OK) {
//            int id = data.getIntExtra(AddEditEventActivity.EXTRA_TASK_ID, -1);
//            if (id == -1) {
//                Toast.makeText(this, R.string.task_can_not_be_updated, Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String name = data.getStringExtra(AddEditEventActivity.EXTRA_TASK_NAME);
//            String details = data.getStringExtra(AddEditEventActivity.EXTRA_TASK_DETAILS);
//            Timestamp deadline_at = Timestamp.valueOf(data.getStringExtra(AddEditEventActivity.EXTRA_TASK_DEADLINE_AT));
//
//            Task task = new Task(name, details, deadline_at);
//            task.setId(Long.valueOf(id));
//            taskViewModel.update(task);
//
//            Toast.makeText(this, R.string.task_was_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.changes_was_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    private TextView selectedDate;


}
