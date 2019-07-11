package zabortceva.eventscalendar.serverdata;

import com.google.gson.annotations.Expose;

import zabortceva.eventscalendar.localdata.Event;

public class Events {
    @Expose
    private int count;
    @Expose
    private Event[] data;
    @Expose
    private String message;
    @Expose
    private int offset;
    @Expose
    private int status;
    @Expose
    private boolean success;

    public int getCount() {
        return count;
    }

    public Event[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getOffset() {
        return offset;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
