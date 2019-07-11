package zabortceva.eventscalendar.localdata;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.Timestamp;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.NonNull;

@Database(entities = {Task.class, Event.class, Pattern.class, User.class},
version = 2)
@TypeConverters({TimestampConverters.class})
public abstract class CalendarDatabase extends RoomDatabase {

    private static CalendarDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized CalendarDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CalendarDatabase.class, "calendar_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private PopulateDbAsyncTask(CalendarDatabase db) {
            taskDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task("Title 1", "Description 1", new Timestamp(System.currentTimeMillis())));
            taskDao.insert(new Task("Title 2", "Description 2", new Timestamp(System.currentTimeMillis())));
            return null;
        }
    }
}
