package zabortceva.eventscalendar.view;

import zabortceva.eventscalendar.localdata.Task;

public class TaskViewData extends Task {
    private String event_name;
    public TaskViewData() {
        super();
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}
