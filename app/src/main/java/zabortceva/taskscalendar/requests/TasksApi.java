package zabortceva.taskscalendar.requests;

import androidx.lifecycle.LiveData;
import androidx.room.TypeConverters;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.taskscalendar.localdata.CalendarDayConverters;
import zabortceva.taskscalendar.localdata.Task;
import zabortceva.taskscalendar.serverdata.Tasks;

public interface TasksApi {

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("api/v1/tasks")
    Call<Tasks> getDayTasks(@Query("id") Long[] id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/tasks")
    Call<Tasks> getAllTasks(@Query("count") int count);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/tasks")
    Call<Tasks> getEventTasks(@Query("event_id") long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @POST("/api/v1/tasks")
    Call<Tasks> insert(@Query("event_id") Long event_id, @Body Task task);

}
