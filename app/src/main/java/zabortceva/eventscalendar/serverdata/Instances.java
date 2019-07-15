package zabortceva.eventscalendar.serverdata;

import com.google.gson.annotations.Expose;

public class Instances {
    @Expose
    private Long count;
    @Expose
    private Instance[] data;
    @Expose
    private String message;
    @Expose
    private Long offset;
    @Expose
    private Long status;
    @Expose
    private Boolean success;

    public Instances(){}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Instance[] getData() {
        return data;
    }

    public void setData(Instance[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
