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
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.serverdata.Events;

public interface EventsApi {
    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<Events> getEventsByInterval(@Query("from") Long from, @Query("to") Long to);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events/{id}")
    Call<Events> getEventById(@Path("id") long id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<Events> getAllEvents(@Query("count") int count);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @POST("/api/v1/events")
    Call<Events> insert(@Body Event event);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @DELETE("/api/v1/events/{id}")
    Call<Events> delete(@Path("id") long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @PATCH("/api/v1/events/{id}")
    Call<Events> update(@Path("id") long id, @Body Event event);
}
