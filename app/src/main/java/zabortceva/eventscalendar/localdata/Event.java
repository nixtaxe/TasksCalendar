package zabortceva.eventscalendar.localdata;

import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.Expose;

import java.util.Calendar;

@Entity(tableName = "event_table")
public class Event {
    @PrimaryKey
    @Expose
    private Long id;

    @Expose
    private String owner_id;

    @Expose
    private Long created_at;

    @Expose
    private Long updated_at;

    @Expose
    private String details;

    @Expose
    private String location;

    @Expose
    private String name;

    @Expose
    private String status;

    public Event() {
    }

    public Event(String details, String location, String name, String status) {
        this.details = details;
        this.location = location;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }
}
