package zabortceva.eventscalendar.serverdata;

import com.google.gson.annotations.Expose;

import zabortceva.eventscalendar.localdata.Pattern;

public class Patterns {
    @Expose
    private int count;
    @Expose
    private Pattern[] data;
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

    public Pattern[] getData() {
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
