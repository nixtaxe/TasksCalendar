package zabortceva.taskscalendar.localdata;

import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    //Название задачи
    @NonNull
    private String name;

    //Описание задачи
    @Nullable
    private String details;

    @NonNull
    private char status = 'q';

    @Nullable
    private int event_id;

    @Nullable
    private int parent_id;

    @NonNull
    @TypeConverters({TimestampConverters.class})
    private Timestamp deadline_at;

    @NonNull
    @TypeConverters({TimestampConverters.class})
    private Timestamp created_at;

    @Nullable
    @TypeConverters({TimestampConverters.class})
    private Timestamp updated_at;

    public Task(String name, String details, Timestamp deadline_at) {
        this.name = name;
        this.details = details;
        this.deadline_at = deadline_at;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public Task() {
        this.id = 0;
        this.name = "";
        this.details = "";
        this.event_id = 0;
        this.parent_id = 0;
        this.deadline_at = new Timestamp(System.currentTimeMillis());
        this.created_at = new Timestamp(System.currentTimeMillis());
        this.updated_at = new Timestamp(System.currentTimeMillis());
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public char getStatus() {
        return status;
    }

    public int getEvent_id() {
        return event_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public Timestamp getDeadline_at() {
        return deadline_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setDeadline_at(Timestamp deadline_at) {
        this.deadline_at = deadline_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
