package zabortceva.eventscalendar.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.repository.WebCalendarRepository;
import zabortceva.eventscalendar.serverdata.Events;

public class EventViewModel extends AndroidViewModel {
    private WebCalendarRepository repository;
    private LiveData<List<Event>> allEvents;

    public EventViewModel(@NonNull Application application) {
        super(application);

        repository = WebCalendarRepository.getInstance();
    }

    public LiveData<List<Event>> getSavedAllEvents() {return allEvents;}
    public LiveData<List<Event>> getAllEvents() {
        allEvents = repository.getAllEvents();
        return allEvents;
    }
    public LiveData<Events> insert(Event event) {
        return repository.insertEvent(event);
    }
    public void update(Event event) {
        repository.updateEvent(event);
    }
    public void delete(Event event) {
        repository.deleteEvent(event);
    }
    public LiveData<Event> getEventById(long id) {
        return repository.getEventById(id);
    }
}
