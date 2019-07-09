package zabortceva.taskscalendar.requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import zabortceva.taskscalendar.serverdata.Events;

public interface EventsApi {
    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<Events> getEventsByInterval(@Query("from") Long from, @Query("to") Long to);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<Events> getAllEvents(@Query("count") int count);
}
