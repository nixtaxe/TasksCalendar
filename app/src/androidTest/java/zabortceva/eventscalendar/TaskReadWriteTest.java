package zabortceva.eventscalendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import android.content.Context;

import androidx.annotation.Nullable;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import zabortceva.eventscalendar.localdata.CalendarDatabase;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.localdata.TaskDao;

@RunWith(AndroidJUnit4.class)
public class TaskReadWriteTest {
    private TaskDao taskDao;
    private CalendarDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), CalendarDatabase.class)
                .allowMainThreadQueries()
                .build();
        taskDao = db.taskDao();
    }

    @After
    public void closeDb() throws IOException {
        if (!(db == null))
            db.close();
    }

    @Test
    public void writeTaskAndReadInList() throws Exception {
        Task day_task = new Task("Test", "Test", new Timestamp(System.currentTimeMillis()));
        taskDao.insert(new Task());
        taskDao.insert(new Task());

        LiveData<List<Task>> allTasks = taskDao.getAllTasks();
        allTasks.observeForever(new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
               //
            }
        });

        Long time = day_task.getDeadline_at().getTime();
        LiveData<List<Task>> dayTasks = taskDao.getDayTasks(time);
        dayTasks.observeForever(new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                //
            }
        });

        assert(dayTasks.getValue().size() == 2);
    }
}

