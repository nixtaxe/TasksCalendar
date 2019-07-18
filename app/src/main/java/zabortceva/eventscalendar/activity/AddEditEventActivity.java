package zabortceva.eventscalendar.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.ical.values.Frequency;
import com.google.ical.values.RRule;
import com.google.ical.values.Weekday;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.requests.ApiStrings;
import zabortceva.eventscalendar.serverdata.Instance;
import zabortceva.eventscalendar.view.DatePickerFragment;
import zabortceva.eventscalendar.view.DateTimeString;
import zabortceva.eventscalendar.view.TimePickerFragment;
import zabortceva.eventscalendar.view.UpdateUi;

public class AddEditEventActivity extends AppCompatActivity {
    public static final Integer[] WEEKDAY_BUTTONS_IDS = {R.id.monday, R.id.tuesday, R.id.wednesday,
            R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};
    public static final Frequency[] FREQUENCIES = {null, Frequency.DAILY, Frequency.WEEKLY,
            Frequency.MONTHLY, Frequency.YEARLY};

    public static final String EXTRA_EVENT = "zabortceva.eventscalendar.EXTRA_EVENT";
    public static final String EXTRA_PATTERN = "zabortceva.eventscalendar.EXTRA_PATTERN";
    public static final String EXTRA_INSTANCE = "zabortceva.eventscalendar.EXTRA_INSTANCE";

    public static final String EXTRA_STARTS_AT = "zabortceva.eventscalendar.EXTRA_STARTS_AT";

    public static final String EXTRA_DAY = "zabortceva.eventscalendar.EXTRA_DAY";
    public static final String EXTRA_MONTH = "zabortceva.eventscalendar.EXTRA_MONTH";
    public static final String EXTRA_YEAR = "zabortceva.eventscalendar.EXTRA_YEAR";

    public static final String EXTRA_MINUTE = "zabortceva.eventscalendar.EXTRA_MINUTE";
    public static final String EXTRA_HOUR = "zabortceva.eventscalendar.EXTRA_HOUR";

    public static final long INFINITE = Long.MAX_VALUE - 1;

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
    private Spinner timezoneSpinner;

    private Calendar c = Calendar.getInstance();
    private HashMap<ToggleButton, String> weekdayHashMap;
    private HashMap<String, Frequency> frequencyHashMap;
    private HashMap<String, UpdateUi> updateUiMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        weekdayHashMap = new HashMap<>();
        for (int i = 0; i < WEEKDAY_BUTTONS_IDS.length; i++) {
            weekdayHashMap.put((ToggleButton) findViewById(WEEKDAY_BUTTONS_IDS[i]), ApiStrings.WEEKDAYS[i]);
        }

        frequencyHashMap = new HashMap<>();
        for (int i = 0; i < FREQUENCIES.length; i++) {
            frequencyHashMap.put(ApiStrings.FREQUENCIES[i], FREQUENCIES[i]);
        }

        updateUiMap = new HashMap<>();
        updateUiMap.put(ApiStrings.PATTERN_OPTIONS.ONCE, new UpdateUi() {
            @Override
            public void updateUi() {
                findViewById(R.id.weekdays).setVisibility(View.GONE);
                findViewById(R.id.freq_and_count).setVisibility(View.GONE);
            }
        });
        updateUiMap.put(ApiStrings.PATTERN_OPTIONS.DAILY, new UpdateUi() {
            @Override
            public void updateUi() {
                findViewById(R.id.freq_and_count).setVisibility(View.VISIBLE);
                findViewById(R.id.weekdays).setVisibility(View.GONE);
            }
        });
        updateUiMap.put(ApiStrings.PATTERN_OPTIONS.WEEKLY, new UpdateUi() {
            @Override
            public void updateUi() {
                findViewById(R.id.weekdays).setVisibility(View.VISIBLE);
                findViewById(R.id.freq_and_count).setVisibility(View.VISIBLE);
            }
        });
        updateUiMap.put(ApiStrings.PATTERN_OPTIONS.MONTHLY, new UpdateUi() {
            @Override
            public void updateUi() {
                findViewById(R.id.weekdays).setVisibility(View.GONE);
                findViewById(R.id.freq_and_count).setVisibility(View.VISIBLE);
            }
        });
        updateUiMap.put(ApiStrings.PATTERN_OPTIONS.YEARLY, new UpdateUi() {
            @Override
            public void updateUi() {
                findViewById(R.id.weekdays).setVisibility(View.GONE);
                findViewById(R.id.freq_and_count).setVisibility(View.VISIBLE);
            }
        });

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

        patternSpinner = findViewById(R.id.pattern_spinner);
        final ArrayList<String> patterns = new ArrayList<>();
        patterns.add(ApiStrings.PATTERN_OPTIONS.ONCE);
        patterns.add(ApiStrings.PATTERN_OPTIONS.DAILY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.WEEKLY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.MONTHLY);
        patterns.add(ApiStrings.PATTERN_OPTIONS.YEARLY);
        ArrayAdapter<String> patternsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patterns);
        patternsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        patternSpinner.setAdapter(patternsAdapter);
        patternSpinner.setOnItemSelectedListener(onPatternSelected);

        Intent intent = getIntent();

        long timeInMillis;
        Instance instance = new Instance();
        if (intent.hasExtra(EXTRA_STARTS_AT))
            timeInMillis = getIntent().getLongExtra(EXTRA_STARTS_AT, 0);
        else {
            instance = (new Gson()).fromJson(intent.getStringExtra(EXTRA_INSTANCE), Instance.class);
            timeInMillis = instance.getStarted_at();
        }

        if (intent.hasExtra(EXTRA_PATTERN)) {
            Pattern pattern = (new Gson()).fromJson(intent.getStringExtra(EXTRA_PATTERN), Pattern.class);
            TimeZone timeZone = TimeZone.getTimeZone(pattern.getTimezone());
            LocalDateTime time = LocalDateTime.fromDateFields(new Date(timeInMillis));
            DateTime zonedDateTime = time.toDateTime(DateTimeZone.forTimeZone(timeZone));
            timeInMillis = zonedDateTime.getMillis();
        }

        startsAtDay = findViewById(R.id.starts_at_day);
        startsAtDay.setText(DateTimeString.getDateString(timeInMillis));
        startsAtDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();

                Bundle args = new Bundle();
                c.setTime(DateTimeString.getDate(startsAtDay.getText().toString()));
                args.putInt(EXTRA_YEAR, c.get(Calendar.YEAR));
                args.putInt(EXTRA_MONTH, c.get(Calendar.MONTH));
                args.putInt(EXTRA_DAY, c.get(Calendar.DAY_OF_MONTH));
                datePicker.setArguments(args);

                datePicker.setCallback(onStartsAtDay);
                datePicker.show(getSupportFragmentManager(), "Starts at day:");
            }
        });

        startsAtTime = findViewById(R.id.starts_at_time);
        startsAtTime.setText(DateTimeString.getTimeString(timeInMillis));
        startsAtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();

                Bundle args = new Bundle();
                c.setTime(DateTimeString.getDate(startsAtTime.getText().toString()));
                args.putInt(EXTRA_HOUR, c.get(Calendar.HOUR));
                args.putInt(EXTRA_MINUTE, c.get(Calendar.MINUTE));
                timePicker.setArguments(args);

                timePicker.setCallback(onStartsAtTime);
                timePicker.show(getSupportFragmentManager(), "Starts at time:");
            }
        });


        if (intent.hasExtra(EXTRA_STARTS_AT)) {
            c.setTime(new Date(timeInMillis));
            c.add(Calendar.HOUR_OF_DAY, 1);
            timeInMillis = c.getTimeInMillis();
        } else {
            timeInMillis = instance.getEnded_at();
            Pattern pattern = (new Gson()).fromJson(intent.getStringExtra(EXTRA_PATTERN), Pattern.class);
            TimeZone timeZone = TimeZone.getTimeZone(pattern.getTimezone());
            LocalDateTime time = LocalDateTime.fromDateFields(new Date(timeInMillis));
            DateTime zonedDateTime = time.toDateTime(DateTimeZone.forTimeZone(timeZone));
            timeInMillis = zonedDateTime.getMillis();
        }

        endsAtDay = findViewById(R.id.ends_at_day);
        endsAtDay.setText(DateTimeString.getDateString(timeInMillis));
        endsAtDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();

                Bundle args = new Bundle();
                c.setTime(DateTimeString.getDate(startsAtDay.getText().toString()));
                args.putInt(EXTRA_YEAR, c.get(Calendar.YEAR));
                args.putInt(EXTRA_MONTH, c.get(Calendar.MONTH));
                args.putInt(EXTRA_DAY, c.get(Calendar.DAY_OF_MONTH));
                datePicker.setArguments(args);

                datePicker.setCallback(onEndsAtDay);
                datePicker.show(getSupportFragmentManager(), "Ends at day:");
            }
        });

        endsAtTime = findViewById(R.id.ends_at_time);
        endsAtTime.setText(DateTimeString.getTimeString(timeInMillis));
        endsAtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();

                Bundle args = new Bundle();
                c.setTime(DateTimeString.getDate(startsAtTime.getText().toString()));
                args.putInt(EXTRA_HOUR, c.get(Calendar.HOUR));
                args.putInt(EXTRA_MINUTE, c.get(Calendar.MINUTE));
                timePicker.setArguments(args);

                timePicker.setCallback(onEndsAtTime);
                timePicker.show(getSupportFragmentManager(), "Starts at time:");
            }
        });

        if (intent.hasExtra(EXTRA_EVENT)) {
            Event event = (new Gson()).fromJson(intent.getStringExtra(EXTRA_EVENT), Event.class);
            editEventName.setText(event.getName());
            editEventDetails.setText(event.getDetails());
            editEventLocation.setText(event.getLocation());
            editEventStatus.setText(event.getStatus());
        }

        timezoneSpinner = findViewById(R.id.timezone_spinner);
        final ArrayList<String> timezones = new ArrayList<>();

        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++) {
            TimeZone d = TimeZone.getTimeZone(ids[i]);
            if (!ids[i].matches(".*/.*")) {
                continue;
            }

            timezones.add(prettifyTimezone(ids[i]));
        }

        Collections.sort(timezones);

        ArrayAdapter<String> timezonesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timezones);
        timezonesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timezoneSpinner.setAdapter(timezonesAdapter);

        if (intent.hasExtra(EXTRA_PATTERN)) {
            Pattern pattern = (new Gson()).fromJson(intent.getStringExtra(EXTRA_PATTERN), Pattern.class);
            if (pattern.getRrule() != null && !Objects.equals(pattern.getRrule(), "")) {
                RRule rule = null;
                try {
                    rule = new RRule("RRULE:" + pattern.getRrule());
                    String frequency = rule.getFreq().name();
                    int pattern_pos = patternsAdapter.getPosition(frequency);
                    patternSpinner.setSelection(pattern_pos);
                } catch (ParseException e) {
                    int pattern_pos = patternsAdapter.getPosition(ApiStrings.PATTERN_OPTIONS.ONCE);
                    patternSpinner.setSelection(pattern_pos);
                    e.printStackTrace();
                }


            } else {
                int pattern_pos = patternsAdapter.getPosition(ApiStrings.PATTERN_OPTIONS.ONCE);
                patternSpinner.setSelection(pattern_pos);
            }
            int pattern_pos = timezonesAdapter.getPosition(prettifyTimezone(pattern.getTimezone()));
            timezoneSpinner.setSelection(pattern_pos);
        }

    }

    AdapterView.OnItemSelectedListener onPatternSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateUiMap.get(parent.getSelectedItem().toString()).updateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private String prettifyTimezone(String id) {
        TimeZone d = TimeZone.getTimeZone(id);
        if (!id.matches(".*/.*")) {
            return id;
        }
        String region = id.replaceAll(".*/", "").replaceAll("_", " ");
        int hours = Math.abs(d.getRawOffset()) / 3600000;
        int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
        String sign = d.getRawOffset() >= 0 ? "+" : "-";

        String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);
        return timeZonePretty;
    }

    DatePickerDialog.OnDateSetListener onEndsAtDay = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = DateTimeString.getDateString(c.getTimeInMillis());
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

            String date = DateTimeString.getDateString(c.getTimeInMillis());
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
            time = DateTimeString.getTimeString(timestamp.getTime());
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
            time = DateTimeString.getTimeString(timestamp.getTime());
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

        pattern.setTimezone(timezoneSpinner.getSelectedItem().toString());

//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss z");
        //TimeZone timeZone = TimeZone.getTimeZone(pattern.getTimezone());
//        sdf.setTimeZone(timeZone);
        long duration = INFINITE;
        Timestamp timestamp;
        try {
            String startsAt = startsAtDay.getText().toString() + " " + startsAtTime.getText().toString() + ":00";
            String endsAt = startsAtDay.getText().toString() + " " + endsAtTime.getText().toString() + ":00";
            timestamp = Timestamp.valueOf(startsAt);
            duration = Timestamp.valueOf(endsAt).getTime() - timestamp.getTime();
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_SHORT).show();
            return;
        }
        //LocalDateTime time = LocalDateTime.fromDateFields(new Date(timestamp.getTime()));
        //DateTime zonedDateTime = time.toDateTime(DateTimeZone.forTimeZone(timeZone));
        pattern.setStarted_at(timestamp.getTime());

        try {
            String endsAt = endsAtDay.getText().toString() + " " + endsAtTime.getText().toString() + ":00";
            timestamp = Timestamp.valueOf(endsAt);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_SHORT).show();
            return;
        }
        //time = LocalDateTime.fromDateFields(new Date(timestamp.getTime()));
        //zonedDateTime = time.toDateTime(DateTimeZone.forTimeZone(timeZone));
        pattern.setEnded_at(timestamp.getTime());
        pattern.setDuration(duration);
        //pattern.setDuration(pattern.getEnded_at() - pattern.getStarted_at());

        RRule rule = new RRule();
        rule.setWkSt(Weekday.MO);
        rule.setFreq(frequencyHashMap.get(patternSpinner.getSelectedItem().toString()));
        EditText everyX = findViewById(R.id.every_x);
        int x = Integer.valueOf(everyX.getText().toString());
        rule.setInterval(x);
        EditText xTimes = findViewById(R.id.x_times);
        x = Integer.valueOf(xTimes.getText().toString());
        rule.setCount(x);
        String stRule = rule.toIcal().substring(6);

        if (rule.getFreq() == Frequency.WEEKLY) {
            StringBuilder byDay = new StringBuilder(";BYDAY=");
            Iterator it = weekdayHashMap.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ToggleButton button = (ToggleButton) pair.getKey();
                if (button.isChecked()) {
                    byDay.append(pair.getValue().toString());
                    byDay.append(",");
                }
            }
            stRule += byDay.substring(0, byDay.length() - 1);
        }

        pattern.setRrule(stRule);

        data.putExtra(EXTRA_EVENT, (new Gson()).toJson(event));
        data.putExtra(EXTRA_PATTERN, (new Gson()).toJson(pattern));

        setResult(RESULT_OK, data);
        finish();
    }
}
