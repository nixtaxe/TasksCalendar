package zabortceva.eventscalendar.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.repository.WebCalendarRepository;

public class PermissionViewModel extends AndroidViewModel {
    private WebCalendarRepository repository;
    private LiveData<List<Permission>> allPermissions;

    public PermissionViewModel(@NonNull Application application) {
        super(application);

        repository = WebCalendarRepository.getInstance();
    }

    public LiveData<List<Permission>> getSavedAllPermissions() {
        return allPermissions;
    }

    public LiveData<List<Permission>> getAllPermissions(String entity_type) {
        allPermissions = repository.getAllPermissions(entity_type);
        return allPermissions;
    }
}
