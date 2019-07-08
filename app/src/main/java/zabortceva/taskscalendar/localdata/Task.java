package zabortceva.taskscalendar.localdata;

import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @Expose
    private Long id;

    //Название задачи
    @NonNull
    @Expose
    private String name;

    //Описание задачи
    @Nullable
    @Expose
    private String details;

    @NonNull
    @Expose
    private String status = "q";

    @Nullable
    @Expose
    private Long event_id;

    @Nullable
    @Expose
    private Long parent_id;

    @NonNull
    @Expose
    @TypeConverters({TimestampConverters.class})
    private Long deadline_at;

    @NonNull
    @Expose
    @TypeConverters({TimestampConverters.class})
    private Long created_at;

    @Nullable
    @Expose
    @TypeConverters({TimestampConverters.class})
    private Long updated_at;

    public Task(String name, String details, Timestamp deadline_at) {
        this.name = name;
        this.details = details;
        this.deadline_at = deadline_at.getTime();
        this.created_at = System.currentTimeMillis();
    }

    public Task() {
        this.id = 0L;
        this.name = "";
        this.details = "";
        this.event_id = 0L;
        this.parent_id = 0L;
        this.deadline_at = System.currentTimeMillis();
        this.created_at = System.currentTimeMillis();
        this.updated_at = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", deadline_at=" + deadline_at.toString() +
                ", created_at=" + created_at.toString() +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getStatus() {
        return status;
    }

    public Long getEvent_id() {
        return event_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public Long getDeadline_at() {
        return deadline_at;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public void setDeadline_at(Long deadline_at) {
        this.deadline_at = deadline_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }
}
