package zabortceva.taskscalendar.serverdata;

import com.google.gson.annotations.Expose;
import zabortceva.taskscalendar.localdata.Task;

import java.util.List;

public class Tasks {
    @Expose
    private int count;
    @Expose
    private Task[] data;
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

    public Task[] getData() {
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
