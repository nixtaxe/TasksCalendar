package zabortceva.eventscalendar.serverdata;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.Date;

import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.localdata.Pattern;

public class FullEvent implements WeekViewDisplayable<FullEvent> {
    private Instance instance;
    private Pattern pattern;
    private Event event;

    public FullEvent(){}

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public WeekViewEvent<FullEvent> toWeekViewEvent() {
        int backgroundColor = Color.WHITE;
        int textColor = Color.WHITE;
        int borderWidth = 4;

        WeekViewEvent.Style style = new  WeekViewEvent.Style.Builder()
                .setTextColor(textColor)
                .setBackgroundColor(backgroundColor)
                .setTextStrikeThrough(false)
                .setBorderWidth(borderWidth)
                .setBorderColor(Color.WHITE)
                .build();

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(new Date(instance.getStarted_at()));
        Calendar endTime = (Calendar) startTime.clone();
        endTime.setTime(new Date(instance.getEnded_at()));

        return new WeekViewEvent.Builder<FullEvent>()
                .setId(event.getId())
                .setTitle(event.getName())
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setLocation(event.getLocation())
                .setAllDay(false)
                .setStyle(style)
                .setData(this)
                .build();
    }
}
