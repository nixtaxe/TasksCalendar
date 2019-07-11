package zabortceva.eventscalendar.requests;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.serverdata.Tasks;

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

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @DELETE("/api/v1/tasks/{id}")
    Call<Void> delete(@Path("id") Long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @PATCH("/api/v1/tasks/{id}")
    Call<Tasks> update(@Path("id") Long id, @Body Task task);
}
