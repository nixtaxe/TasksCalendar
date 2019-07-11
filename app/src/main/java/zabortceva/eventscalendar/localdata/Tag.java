package zabortceva.eventscalendar.localdata;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag_table")
public class Tag {
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
