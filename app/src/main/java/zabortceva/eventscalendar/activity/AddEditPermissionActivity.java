package zabortceva.eventscalendar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.requests.ApiStrings;
import zabortceva.eventscalendar.view.model.PermissionViewModel;

public class AddEditPermissionActivity extends AppCompatActivity {
    private EditText userId;
    private Spinner entityTypesSpinner;
    private Spinner actionsSpinner;
    private PermissionViewModel permissionViewModel;
    private FloatingActionButton savePermissionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_permission);

        userId = findViewById(R.id.user_id);

        entityTypesSpinner = findViewById(R.id.entity_types_spinner);
        final ArrayList<String> entityTypes = new ArrayList<>();
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.EVENT);
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.PATTERN);
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.TASK);
        ArrayAdapter<String> entityTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, entityTypes);
        entityTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        entityTypesSpinner.setAdapter(entityTypesAdapter);

        actionsSpinner = findViewById(R.id.actions_spinner);
        final ArrayList<String> actions = new ArrayList<>();
        actions.add(ApiStrings.ACTION_OPTIONS.READ);
        actions.add(ApiStrings.ACTION_OPTIONS.UPDATE);
        actions.add(ApiStrings.ACTION_OPTIONS.DELETE);
        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, actions);
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        actionsSpinner.setAdapter(actionsAdapter);

        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);

        savePermissionButton = findViewById(R.id.save_permission_button);
        savePermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userId.getText().toString().trim();
                if (Objects.equals(user,""))
                    user = null;
                permissionViewModel.insertPermission(user, entityTypesSpinner.getSelectedItem().toString(), actionsSpinner.getSelectedItem().toString());
            }
        });
    }
}
