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
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.serverdata.Events;

public interface EventsApi {

    @GET("/api/v1/events")
    Call<Events> getEventsByInterval(@Query("from") Long from, @Query("to") Long to, @Header("X-Firebase-Auth") String idToken);

    @GET("/api/v1/events/{id}")
    Call<Events> getEventById(@Path("id") long id, @Header("X-Firebase-Auth") String idToken);

    @GET("/api/v1/events")
    Call<Events> getAllEvents(@Query("count") int count, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("/api/v1/events")
    Call<Events> insert(@Body Event event, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("/api/v1/events/{id}")
    Call<Events> delete(@Path("id") long id, @Header("X-Firebase-Auth") String idToken);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PATCH("/api/v1/events/{id}")
    Call<Events> update(@Path("id") long id, @Body Event event, @Header("X-Firebase-Auth") String idToken);
}
