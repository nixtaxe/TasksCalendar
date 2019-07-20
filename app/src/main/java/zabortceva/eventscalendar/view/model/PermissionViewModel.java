package zabortceva.eventscalendar.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.repository.ServerCalendarRepository;

public class PermissionViewModel extends AndroidViewModel {
    private ServerCalendarRepository repository;
    private LiveData<List<Permission>> allPermissions;

    public PermissionViewModel(@NonNull Application application) {
        super(application);

        repository = ServerCalendarRepository.getInstance(application);
    }

    public LiveData<List<Permission>> getSavedAllPermissions() {
        return allPermissions;
    }

    public LiveData<List<Permission>> getAllPermissions(String entity_type) {
        allPermissions = repository.getAllPermissions(entity_type);
        return allPermissions;
    }

    public LiveData<String> insertPermission(String user_id, String entity_type, String action) {
        return repository.insertPermission(user_id, entity_type, action);
    }
}
