package zabortceva.eventscalendar.serverdata;

import com.google.gson.annotations.Expose;

public class Instance {
    @Expose
    private Long started_at;
    @Expose
    private Long ended_at;
    @Expose
    private Long event_id;
    @Expose
    private Long pattern_id;

    public Instance(){}

    public Long getStarted_at() {
        return started_at;
    }

    public void setStarted_at(Long started_at) {
        this.started_at = started_at;
    }

    public Long getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(Long ended_at) {
        this.ended_at = ended_at;
    }

    public Long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public Long getPattern_id() {
        return pattern_id;
    }

    public void setPattern_id(Long pattern_id) {
        this.pattern_id = pattern_id;
    }
}
