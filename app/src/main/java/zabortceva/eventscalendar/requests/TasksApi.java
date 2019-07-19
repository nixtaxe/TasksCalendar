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

    @GET(ApiStrings.TASKS_PATH)
    Call<Tasks> getDayTasks(@Query(ApiStrings.ID) Long[] id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.TASKS_PATH)
    Call<Tasks> getAllTasks(@Query(ApiStrings.COUNT) int count, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.TASKS_PATH)
    Call<Tasks> getEventTasks(@Query(ApiStrings.EVENT_ID) Long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @POST(ApiStrings.TASKS_PATH)
    Call<Tasks> insert(@Query(ApiStrings.EVENT_ID) Long event_id, @Body Task task, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @DELETE(ApiStrings.TASKS_ID_PATH)
    Call<Tasks> delete(@Path(ApiStrings.ID) Long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @PATCH(ApiStrings.TASKS_ID_PATH)
    Call<Tasks> update(@Path(ApiStrings.ID) Long id, @Body Task task, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);
}
