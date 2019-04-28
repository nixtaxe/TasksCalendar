package zabortceva.taskscalendar.localdata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_pattern_table")
public class EventPattern {
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
