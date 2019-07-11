package zabortceva.eventscalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import zabortceva.eventscalendar.R;

public class AddEditEventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_NAME = "zabortceva.eventscalendar.EXTRA_EVENT_NAME";
    public static final String EXTRA_EVENT_DETAILS = "zabortceva.eventscalendar.EXTRA_EVENT_DETAILS";
    public static final String EXTRA_EVENT_LOCATION = "zabortceva.eventscalendar.EXTRA_EVENT_LOCATION";
    public static final String EXTRA_EVENT_STATUS = "zabortceva.eventscalendar.EXTRA_EVENT_STATUS";
    public static final String EXTRA_EVENT_OWNER_ID = "zabortceva.eventscalendar.EXTRA_EVENT_OWNER_ID";
    public static final String EXTRA_EVENT_ID = "zabortceva.eventscalendar.EXTRA_EVENT_ID";

    private FloatingActionButton saveEventButton;
    private EditText editEventName;
    private EditText editEventDetails;
    private EditText editEventLocation;
    private EditText editEventStatus;

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
    }

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

        Intent data = new Intent();
        if (intent.hasExtra(EXTRA_EVENT_ID)) {
            long id = intent.getLongExtra(EXTRA_EVENT_ID, -1);
            String owner_id = intent.getStringExtra(EXTRA_EVENT_OWNER_ID);
            data.putExtra(EXTRA_EVENT_ID, id);
            data.putExtra(EXTRA_EVENT_OWNER_ID, owner_id);
        }
        data.putExtra(EXTRA_EVENT_NAME, name);
        data.putExtra(EXTRA_EVENT_DETAILS, details);
        data.putExtra(EXTRA_EVENT_LOCATION, location);
        data.putExtra(EXTRA_EVENT_STATUS, status);

        setResult(RESULT_OK, data);
        finish();
    }
}