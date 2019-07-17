package zabortceva.eventscalendar.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.List;

import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.repository.WebCalendarRepository;
import zabortceva.eventscalendar.serverdata.Events;

public class EventViewModel extends AndroidViewModel {
    private WebCalendarRepository repository;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<Event>> dayEvents;
    private LiveData<List<CalendarDay>> allBusyDays;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = WebCalendarRepository.getInstance();
    }

    public LiveData<List<Event>> getSavedAllEvents() {return allEvents;}
    public LiveData<List<Event>> getAllEvents() {
        allEvents = repository.getAllEvents();
        return allEvents;
    }
    public LiveData<List<Event>> getSavedDayEvents() {return dayEvents;};
    public LiveData<List<Event>> getDayEvents(Timestamp timestamp) {
        dayEvents = repository.getDayEvents(timestamp);
        return dayEvents;
    }
    public LiveData<Events> insert(Event event) {
        return repository.insertEvent(event);
    }
    public LiveData<Events> update(Event event) {
        return repository.updateEvent(event);
    }
    public LiveData<Events> delete(Event event) {
        return repository.deleteEvent(event);
    }
    public LiveData<Event> getEventById(long id) {
        return repository.getEventById(id);
    }
    public LiveData<List<CalendarDay>> getSavedAllBusyDays() {return  allBusyDays;}
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        allBusyDays = repository.getAllBusyDays();
        return allBusyDays;
    }
}
