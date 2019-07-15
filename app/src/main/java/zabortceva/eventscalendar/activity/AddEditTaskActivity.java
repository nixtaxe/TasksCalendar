package zabortceva.eventscalendar.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.view.DatePickerFragment;
import zabortceva.eventscalendar.view.model.EventViewModel;
import zabortceva.eventscalendar.view.EventsSpinnerAdapter;
import zabortceva.eventscalendar.view.TimePickerFragment;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_EVENTS_NAME = "zabortceva.eventscalendar.EXTRA_TASK_EVENTS_NAME";
    public static final String EXTRA_TASK_ID = "zabortceva.eventscalendar.EXTRA_TASK_ID";
    public static final String EXTRA_TASK_NAME = "zabortceva.eventscalendar.EXTRA_TASK_NAME";
    public static final String EXTRA_TASK_DETAILS = "zabortceva.eventscalendar.EXTRA_TASK_DETAILS";
    public static final String EXTRA_TASK_DEADLINE_AT = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_AT";

    public static final String EXTRA_TASK_DEADLINE_YEAR = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_YEAR";
    public static final String EXTRA_TASK_DEADLINE_MONTH = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_MONTH";
    public static final String EXTRA_TASK_DEADLINE_DAY = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_DAY";
    public static final String EXTRA_TASK_DEADLINE_HOUR = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_HOUR";
    public static final String EXTRA_TASK_DEADLINE_MINUTES = "zabortceva.eventscalendar.EXTRA_TASK_DEADLINE_MINUTES";

    public static final int PICK_DEADLINE_DAY_REQUEST = 1;

    private ArrayList<String> eventsName;
    private EventViewModel eventViewModel;
    private Spinner eventsSpinner;
    private EditText editTaskName;
    private EditText editTaskDetails;
    private TextView editDeadlineAtDay;
    private TextView editDeadlineAtTime;
    private TextView selectedDate;
    private FloatingActionButton saveTaskButton;

    private Calendar c = Calendar.getInstance();
    private DateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dfTime = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //TODO: put all update related stuff in one place

        selectedDate = findViewById(R.id.selected_date);
        final String timestamp = getIntent().getStringExtra(EXTRA_TASK_DEADLINE_AT);
        String time = dfTime.format(Timestamp.valueOf(timestamp));
        String date = dfDay.format(Timestamp.valueOf(timestamp));
        try {
            c.setTime(dfDay.parse(date));
        } catch (ParseException p) {
            p.printStackTrace();
        }
        String mediumDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        selectedDate.setText(mediumDate);

        editTaskName = findViewById(R.id.edit_task_name);
        editTaskDetails = findViewById(R.id.edit_task_details);

        editDeadlineAtDay = findViewById(R.id.edit_deadline_at_day);
        editDeadlineAtDay.setText(date);
        editDeadlineAtDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();

                Bundle args = new Bundle();
                args.putInt(EXTRA_TASK_DEADLINE_YEAR, c.get(Calendar.YEAR));
                args.putInt(EXTRA_TASK_DEADLINE_MONTH, c.get(Calendar.MONTH));
                args.putInt(EXTRA_TASK_DEADLINE_DAY, c.get(Calendar.DAY_OF_MONTH));
                datePicker.setArguments(args);

                datePicker.setCallback(onDeadlineAtDay);
                datePicker.show(getSupportFragmentManager(), "Deadline at day:");
            }
        });

        editDeadlineAtTime = findViewById(R.id.edit_deadline_at_time);
        editDeadlineAtTime.setText(time);
        editDeadlineAtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();

                Timestamp t = Timestamp.valueOf(timestamp);
                Bundle args = new Bundle();
                args.putInt(EXTRA_TASK_DEADLINE_HOUR, t.getHours());
                args.putInt(EXTRA_TASK_DEADLINE_MINUTES, t.getMinutes());
                timePicker.setArguments(args);

                timePicker.setCallback(onDeadlineAtTime);
                timePicker.show(getSupportFragmentManager(), "Deadline at time:");
            }
        });

        final Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TASK_ID)) {
            editTaskName.setText(intent.getStringExtra(EXTRA_TASK_NAME));
            editTaskDetails.setText(intent.getStringExtra(EXTRA_TASK_DETAILS));
        }

        saveTaskButton = findViewById(R.id.save_task_button);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        eventsSpinner = (Spinner) findViewById(R.id.events);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                EventsSpinnerAdapter dataAdapter = new EventsSpinnerAdapter(AddEditTaskActivity.this,
                        android.R.layout.simple_spinner_item, events.toArray(new Event[events.size()]));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventsSpinner.setAdapter(dataAdapter);
                if (intent.hasExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME)) {
                    Long event_id = intent.getLongExtra(AddEditTaskActivity.EXTRA_TASK_EVENTS_NAME, -1);
                    for (Event event : events) {
                        if (Objects.equals(event.getId(), event_id)) {
                            int event_pos = dataAdapter.getPosition(event);
                            eventsSpinner.setSelection(event_pos);
                        }
                    }
                }
            }
        });

    }

    private void saveTask() {
        String name = editTaskName.getText().toString();
        String details = editTaskDetails.getText().toString();
        String deadline_at = editDeadlineAtDay.getText().toString();
        long event_id = ((Event) eventsSpinner.getSelectedItem()).getId();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            deadline_at += " " + editDeadlineAtTime.getText().toString() + ":00";
            Timestamp timestamp = Timestamp.valueOf(deadline_at);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TASK_EVENTS_NAME, event_id);
        data.putExtra(EXTRA_TASK_NAME, name);
        data.putExtra(EXTRA_TASK_DETAILS, details);
        data.putExtra(EXTRA_TASK_DEADLINE_AT, deadline_at);

        long id = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_TASK_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    DatePickerDialog.OnDateSetListener onDeadlineAtDay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String mediumDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
            selectedDate.setText(mediumDate);

            String date = dfDay.format(c.getTime());
            editDeadlineAtDay.setText(date);
        }
    };

    TimePickerDialog.OnTimeSetListener onDeadlineAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = editDeadlineAtTime.getText().toString();
            time = editDeadlineAtDay.getText().toString() + " " + time + ":00";
            Timestamp timestamp = Timestamp.valueOf(time);
            timestamp.setHours(hourOfDay);
            timestamp.setMinutes(minute);
            time = dfTime.format(timestamp);
            editDeadlineAtTime.setText(time);
        }
    };
}
