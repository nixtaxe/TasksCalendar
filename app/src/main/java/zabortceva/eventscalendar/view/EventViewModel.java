package zabortceva.eventscalendar.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.repository.CalendarRepository;
import zabortceva.eventscalendar.repository.WebCalendarRepository;
import zabortceva.eventscalendar.serverdata.Events;

public class EventViewModel extends AndroidViewModel {
    private CalendarRepository repository;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<Event>> dayEvents;
    private Date selectedDay;

    public EventViewModel(@NonNull Application application) {
        super(application);

        repository = new WebCalendarRepository(application);
        allEvents = repository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }
    public LiveData<Events> insert(Event event) {
        return repository.insertEvent(event);
    }
    public LiveData<Event> getEventById(long id) {
        return repository.getEventById(id);
    }
}
