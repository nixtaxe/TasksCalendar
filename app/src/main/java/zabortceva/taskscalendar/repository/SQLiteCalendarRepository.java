package zabortceva.taskscalendar.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import zabortceva.taskscalendar.localdata.CalendarDatabase;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.localdata.TaskDao;

public class SQLiteCalendarRepository implements CalendarRepository {
    //TODO make interface Repository
    //TODO implement Room repository
    //TODO implement offline web repository
    //TODO implement online web repository

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> dayTasks;
    private LiveData<List<CalendarDay>> busyDays;
    private Date selectedDay;

    public SQLiteCalendarRepository(Application application) {
        CalendarDatabase localDatabase = CalendarDatabase.getInstance(application);
        taskDao = localDatabase.taskDao();
        allTasks = taskDao.getAllTasks();
        busyDays = taskDao.getAllBusyDays();
        selectedDay = new Date(System.currentTimeMillis());
        dayTasks = taskDao.getDayTasks(new Timestamp(selectedDay.getTime()));
    }

    @Override
    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    @Override
    public void update(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    @Override
    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    @Override
    public LiveData<List<Task>> getDayTasks(Timestamp day) {
        dayTasks = taskDao.getDayTasks(day);
        return dayTasks;
    }

    @Override
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    @Override
    public LiveData<List<CalendarDay>> getAllBusyDays() {
        return busyDays;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
 }
