package zabortceva.eventscalendar.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.repository.ServerCalendarRepository;
import zabortceva.eventscalendar.serverdata.Patterns;

public class PatternViewModel extends AndroidViewModel {
    private ServerCalendarRepository repository;
    private LiveData<List<Pattern>> dayPatterns;
    private LiveData<Pattern> eventPattern;

    public PatternViewModel(@NonNull Application application) {
        super(application);

        repository = ServerCalendarRepository.getInstance(application);
    }

    public LiveData<Patterns> insert(long event_id, Pattern pattern) {
        return repository.insertPattern(event_id, pattern);
    }

    public LiveData<Patterns> update(Pattern pattern) {
        return repository.updatePattern(pattern);
    }

    public LiveData<Patterns> delete(long id) {
        return repository.deletePattern(id);
    }


}
