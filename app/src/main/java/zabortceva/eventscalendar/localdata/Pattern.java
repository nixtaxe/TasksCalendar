package zabortceva.eventscalendar.localdata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "event_pattern_table")
public class Pattern {
    @PrimaryKey
    @Expose
    private Long id;
    @Expose
    private Long created_at;
    @Expose
    private Long updated_at;
    @Expose
    private Long duration;
    @Expose
    private Long ended_at;
    @Expose
    private String exrule;
    @Expose
    private String rrule;
    @Expose
    private Long started_at;
    @Expose
    private String timezone;

    /*Default constructor*/
    public Pattern() {
    }

    public Pattern(Long duration, Long ended_at, String exrule, String rrule, Long started_at, String timezone) {
        this.duration = duration;
        this.ended_at = ended_at;
        this.exrule = exrule;
        this.rrule = rrule;
        this.started_at = started_at;
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(Long ended_at) {
        this.ended_at = ended_at;
    }

    public String getExrule() {
        return exrule;
    }

    public void setExrule(String exrule) {
        this.exrule = exrule;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public Long getStarted_at() {
        return started_at;
    }

    public void setStarted_at(Long started_at) {
        this.started_at = started_at;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }
}
