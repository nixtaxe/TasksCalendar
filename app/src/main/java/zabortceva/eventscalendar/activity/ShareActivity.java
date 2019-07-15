package zabortceva.eventscalendar.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.requests.ApiStrings;
import zabortceva.eventscalendar.view.PermissionsAdapter;
import zabortceva.eventscalendar.view.model.PermissionViewModel;

public class ShareActivity extends AppCompatActivity {
    private Spinner entityTypesSpinner;
    private final PermissionsAdapter permissionsAdapter = new PermissionsAdapter();
    private RecyclerView permissionsView;
    private PermissionViewModel permissionViewModel;
    private Observer<List<Permission>> permissionsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);

        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);

        permissionsView = findViewById(R.id.permissions_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        permissionsView.setLayoutManager(layoutManager);
        permissionsView.setHasFixedSize(true);
        permissionsView.setAdapter(permissionsAdapter);

        entityTypesSpinner = findViewById(R.id.entity_types_spinner);
        final ArrayList<String> entityTypes = new ArrayList<>();
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.EVENT);
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.PATTERN);
        entityTypes.add(ApiStrings.ENTITY_TYPE_OPTIONS.TASK);
        ArrayAdapter<String> entityTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, entityTypes);
        entityTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        entityTypesSpinner.setAdapter(entityTypesAdapter);

        permissionsObserver = new Observer<List<Permission>>() {
            @Override
            public void onChanged(List<Permission> permissions) {
                permissionsAdapter.submitList(permissions);
            }
        };

        permissionViewModel.getAllPermissions(ApiStrings.ENTITY_TYPE_OPTIONS.EVENT).observe(this, permissionsObserver);

        entityTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                observeNewEntityType(selectedItem);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void observeNewEntityType(String type) {
        LiveData<List<Permission>> data = permissionViewModel.getSavedAllPermissions();
        if (data != null && data.hasObservers())
            data.removeObserver(permissionsObserver);

        permissionViewModel.getAllPermissions(type).observe(this, permissionsObserver);
    }

}
