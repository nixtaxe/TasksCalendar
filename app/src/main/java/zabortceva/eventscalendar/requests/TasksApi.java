package zabortceva.eventscalendar.requests;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.eventscalendar.localdata.Task;
import zabortceva.eventscalendar.serverdata.Tasks;

public interface TasksApi {

    @GET("api/v1/tasks")
    Call<Tasks> getDayTasks(@Query("id") Long[] id, @Header("X-Firebase_Auth") String idToken);

    @GET("/api/v1/tasks")
    Call<Tasks> getAllTasks(@Query("count") int count, @Header("X-Firebase_Auth") String idToken);

    @GET("/api/v1/tasks")
    Call<Tasks> getEventTasks(@Query("event_id") long id, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("/api/v1/tasks")
    Call<Tasks> insert(@Query("event_id") Long event_id, @Body Task task, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("/api/v1/tasks/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PATCH("/api/v1/tasks/{id}")
    Call<Tasks> update(@Path("id") Long id, @Body Task task, @Header("X-Firebase-Auth") String idToken);
}
