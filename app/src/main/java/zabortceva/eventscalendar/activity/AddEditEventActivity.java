package zabortceva.eventscalendar.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.ical.iter.RecurrenceIterator;
import com.google.ical.values.DateTimeValue;
import com.google.ical.values.DateValue;
import com.google.ical.values.Frequency;
import com.google.ical.values.RRule;
import com.google.ical.values.Weekday;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.requests.ApiStrings;
import zabortceva.eventscalendar.view.DatePickerFragment;
import zabortceva.eventscalendar.view.TimePickerFragment;

import static zabortceva.eventscalendar.activity.AddEditTaskActivity.EXTRA_TASK_DEADLINE_DAY;
import static zabortceva.eventscalendar.activity.AddEditTaskActivity.EXTRA_TASK_DEADLINE_MONTH;
import static zabortceva.eventscalendar.activity.AddEditTaskActivity.EXTRA_TASK_DEADLINE_YEAR;

public class AddEditEventActivity extends AppCompatActivity {
    public static final String EXTRA_EVENT = "zabortceva.eventscalendar.EXTRA_EVENT";
    public static final String EXTRA_EVENT_NAME = "zabortceva.eventscalendar.EXTRA_EVENT_NAME";
    public static final String EXTRA_EVENT_DETAILS = "zabortceva.eventscalendar.EXTRA_EVENT_DETAILS";
    public static final String EXTRA_EVENT_LOCATION = "zabortceva.eventscalendar.EXTRA_EVENT_LOCATION";
    public static final String EXTRA_EVENT_STATUS = "zabortceva.eventscalendar.EXTRA_EVENT_STATUS";
    public static final String EXTRA_EVENT_OWNER_ID = "zabortceva.eventscalendar.EXTRA_EVENT_OWNER_ID";
    public static final String EXTRA_EVENT_ID = "zabortceva.eventscalendar.EXTRA_EVENT_ID";

    public static final String EXTRA_STARTS_AT = "zabortceva.eventscalendar.EXTRA_STARTS_AT";

    public static final String EXTRA_DAY = "zabortceva.eventscalendar.EXTRA_DAY";
    public static final String EXTRA_MONTH = "zabortceva.eventscalendar.EXTRA_MONTH";
    public static final String EXTRA_YEAR = "zabortceva.eventscalendar.EXTRA_YEAR";

    public static final String EXTRA_MINUTE = "zabortceva.eventscalendar.EXTRA_MINUTE";
    public static final String EXTRA_HOUR = "zabortceva.eventscalendar.EXTRA_HOUR";
    public static final String EXTRA_PATTERN = "zabortceva.eventscalendar.EXTRA_PATTERN";

    private FloatingActionButton saveEventButton;
    private EditText editEventName;
    private EditText editEventDetails;
    private EditText editEventLocation;
    private EditText editEventStatus;

    private TextView startsAtDay;
    private TextView startsAtTime;
    private TextView endsAtDay;
    private TextView endsAtTime;

    private Spinner patternSpinner;

    private Calendar c = Calendar.getInstance();
    private DateFormat dfDay = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dfTime = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editEventName = findViewById(R.id.edit_event_name);
        editEventDetails = findViewById(R.id.edit_event_details);
        editEventLocation = findViewById(R.id.edit_event_location);
        editEventStatus = findViewById(R.id.edit_event_status);

        saveEventButton = findViewById(R.id.save_event_button);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EVENT_ID)) {
            editEventName.setText(intent.getStringExtra(EXTRA_EVENT_NAME));
            editEventDetails.setText(intent.getStringExtra(EXTRA_EVENT_DETAILS));
            editEventLocation.setText(intent.getStringExtra(EXTRA_EVENT_LOCATION));
            editEventStatus.setText(intent.getStringExtra(EXTRA_EVENT_STATUS));
        }

        patternSpinner = findViewById(R.id.pattern_spinner);
        final ArrayList<String> patterns = new ArrayList<>();
        patterns.add(ApiStrings.PATTERN_OPTIONS.DAILY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.WEEKLY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.MONTHLY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.YEARLY);
        ArrayAdapter<String> patternsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patterns);
        patternsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        patternSpinner.setAdapter(patternsAdapter);

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());//getIntent().getStringExtra(EXTRA_TASK_DEADLINE_AT);
        String time = dfTime.format(timestamp);
        String date = dfDay.format(timestamp);
        try {
            c.setTime(dfDay.parse(date));
        } catch (ParseException p) {
            p.printStackTrace();
        }

        startsAtDay = findViewById(R.id.starts_at_day);
        startsAtDay.setText(date);
        startsAtDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();

                Bundle args = new Bundle();
                args.putInt(EXTRA_YEAR, c.get(Calendar.YEAR));
                args.putInt(EXTRA_MONTH, c.get(Calendar.MONTH));
                args.putInt(EXTRA_DAY, c.get(Calendar.DAY_OF_MONTH));
                datePicker.setArguments(args);

                datePicker.setCallback(onStartsAtDay);
                datePicker.show(getSupportFragmentManager(), "Starts at day:");
            }
        });

        startsAtTime = findViewById(R.id.starts_at_time);
        startsAtTime.setText(time);
        startsAtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();

                Bundle args = new Bundle();
                args.putInt(EXTRA_HOUR, c.get(Calendar.HOUR));
                args.putInt(EXTRA_MINUTE, c.get(Calendar.MINUTE));
                timePicker.setArguments(args);

                timePicker.setCallback(onStartsAtTime);
                timePicker.show(getSupportFragmentManager(), "Starts at time:");
            }
        });

        c.setTime(timestamp);
        c.add(Calendar.HOUR_OF_DAY, 1);

        endsAtDay = findViewById(R.id.ends_at_day);
        endsAtDay.setText(date);
        endsAtDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();

                Bundle args = new Bundle();
                args.putInt(EXTRA_YEAR, c.get(Calendar.YEAR));
                args.putInt(EXTRA_MONTH, c.get(Calendar.MONTH));
                args.putInt(EXTRA_DAY, c.get(Calendar.DAY_OF_MONTH));
                datePicker.setArguments(args);

                datePicker.setCallback(onEndsAtDay);
                datePicker.show(getSupportFragmentManager(), "Ends at day:");
            }
        });

        endsAtTime = findViewById(R.id.ends_at_time);
        endsAtTime.setText(dfTime.format(c.getTime()));
        endsAtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();

                Bundle args = new Bundle();
                args.putInt(EXTRA_HOUR, c.get(Calendar.HOUR));
                args.putInt(EXTRA_MINUTE, c.get(Calendar.MINUTE));
                timePicker.setArguments(args);

                timePicker.setCallback(onEndsAtTime);
                timePicker.show(getSupportFragmentManager(), "Starts at time:");
            }
        });
    }

    DatePickerDialog.OnDateSetListener onEndsAtDay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = dfDay.format(c.getTime());
//            try {
//                if ((dfDay.parse(c.getTime().toString())).compareTo(dfDay.parse(startsAtDay.getText().toString())) < 0) {
//                    Toast.makeText(AddEditEventActivity.this, "Ends before start", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            } catch (ParseException pe) {
//                pe.printStackTrace();
//            }
            endsAtDay.setText(date);
        }
    };

    DatePickerDialog.OnDateSetListener onStartsAtDay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = dfDay.format(c.getTime());
            startsAtDay.setText(date);
        }
    };

    TimePickerDialog.OnTimeSetListener onStartsAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = startsAtTime.getText().toString();
            time = startsAtDay.getText().toString() + " " + time + ":00";
            Timestamp timestamp = Timestamp.valueOf(time);
            timestamp.setHours(hourOfDay);
            timestamp.setMinutes(minute);
            time = dfTime.format(timestamp);
            startsAtTime.setText(time);
        }
    };

    TimePickerDialog.OnTimeSetListener onEndsAtTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = endsAtTime.getText().toString();
            time = endsAtDay.getText().toString() + " " + time + ":00";
            Timestamp timestamp = Timestamp.valueOf(time);
            timestamp.setHours(hourOfDay);
            timestamp.setMinutes(minute);
            time = dfTime.format(timestamp);
            endsAtTime.setText(time);
        }
    };

    private void saveEvent() {
        String name = editEventName.getText().toString();
        String details = editEventDetails.getText().toString();
        String location = editEventLocation.getText().toString();
        String status = editEventStatus.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert event name", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();

        Event event = new Event();
        Pattern pattern = new Pattern();

        Intent data = new Intent();
        if (intent.hasExtra(EXTRA_EVENT)) {
            event = (new Gson()).fromJson(intent.getStringExtra(EXTRA_EVENT), Event.class);
            pattern = (new Gson()).fromJson(intent.getStringExtra(EXTRA_PATTERN), Pattern.class);
        }

        event.setName(name);
        event.setDetails(details);
        event.setLocation(location);
        event.setStatus(status);

        Timestamp timestamp;
        try {
            String startsAt = startsAtDay.getText().toString() + " " + startsAtTime.getText().toString() + ":00";
            timestamp = Timestamp.valueOf(startsAt);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_SHORT).show();
            return;
        }
        pattern.setStarted_at(timestamp.getTime());

        try {
            String endsAt = endsAtDay.getText().toString() + " " + endsAtTime.getText().toString() + ":00";
            timestamp = Timestamp.valueOf(endsAt);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_SHORT).show();
            return;
        }
        pattern.setEnded_at(timestamp.getTime());

        RRule rule = new RRule();
        rule.setWkSt(Weekday.MO);
        rule.setFreq(Frequency.DAILY);
        EditText everyX = findViewById(R.id.every_x);
        int x = Integer.valueOf(everyX.getText().toString());
        rule.setInterval(x);
        String stRule = rule.toIcal().substring(6);
        pattern.setRrule(stRule);
        //Long.MAX_VALUE - 1 to Ended_at

        data.putExtra(EXTRA_EVENT, (new Gson()).toJson(event));
        data.putExtra(EXTRA_PATTERN, (new Gson()).toJson(pattern));

        setResult(RESULT_OK, data);
        finish();
    }
}
