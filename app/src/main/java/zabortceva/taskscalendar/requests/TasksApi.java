package zabortceva.taskscalendar.requests;

import androidx.lifecycle.LiveData;
import androidx.room.TypeConverters;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.taskscalendar.localdata.CalendarDayConverters;
import zabortceva.taskscalendar.serverdata.Tasks;

public interface TasksApi {

//    @GET("api/v1/tasks/get")
//    Call<TaskGetResponse> getTask();

    //@Query("SELECT * FROM task_table WHERE date(deadline_at) = date(:day) ORDER BY deadline_at ASC")
    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("api/v1/tasks/{id}")
    Call<Tasks> getDayTasks(@Path("id") long id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/tasks")
    Call<Tasks> getAllTasks(@Query("event_id") Long id);


//    @TypeConverters(TimestampConverters.class)
//    @Query("SELECT * FROM task_table WHERE strftime('%d-%m-%Y', deadline_at / 1000, 'unixepoch', 'localtime') " +
//            " = strftime('%d-%m-%Y', :day / 1000, 'unixepoch', 'localtime') ORDER BY deadline_at ASC")
//    LiveData<List<Task>> getDayTasks(Timestamp day);
//
//    @Query("SELECT * FROM task_table")
//    LiveData<List<Task>> getAllTasks();
//
//@Headers({
//        "X-Firebase-Auth: serega_mem"
//})
//@GET("api/v1/tasks/8")
//    @TypeConverters(CalendarDayConverters.class)
//    Call<List<CalendarDay>> getAllBusyDays();

}
