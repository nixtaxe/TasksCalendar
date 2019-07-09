package zabortceva.taskscalendar.serverdata;

import com.google.gson.annotations.Expose;

import zabortceva.taskscalendar.localdata.EventPattern;

public class EventPatterns {
    @Expose
    private int count;
    @Expose
    private EventPattern[] data;
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

    public EventPattern[] getData() {
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
