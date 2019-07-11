package zabortceva.eventscalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditEventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_NAME = "zabortceva.eventscalendar.EXTRA_EVENT_NAME";
    public static final String EXTRA_EVENT_DETAILS = "zabortceva.eventscalendar.EXTRA_EVENT_DETAILS";
    public static final String EXTRA_EVENT_LOCATION = "zabortceva.eventscalendar.EXTRA_EVENT_LOCATION";
    public static final String EXTRA_EVENT_STATUS = "zabortceva.eventscalendar.EXTRA_EVENT_STATUS";

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

        Intent data = new Intent();
        data.putExtra(EXTRA_EVENT_NAME, name);
        data.putExtra(EXTRA_EVENT_DETAILS, details);
        data.putExtra(EXTRA_EVENT_LOCATION, location);
        data.putExtra(EXTRA_EVENT_STATUS, status);

        setResult(RESULT_OK, data);
        finish();
    }
}
