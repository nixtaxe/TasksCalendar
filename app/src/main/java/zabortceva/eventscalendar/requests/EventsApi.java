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
import zabortceva.eventscalendar.serverdata.Instances;

public interface EventsApi {

    @GET(ApiStrings.EVENTS_PATH)
    Call<Events> getEventsByInterval(@Query(ApiStrings.FROM) Long from, @Query(ApiStrings.TO) Long to, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.EVENTS_ID_PATH)
    Call<Events> getEventById(@Path(ApiStrings.ID) long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.EVENTS_PATH)
    Call<Events> getAllEvents(@Query(ApiStrings.COUNT) int count, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @POST(ApiStrings.EVENTS_PATH)
    Call<Events> insert(@Body Event event, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @DELETE(ApiStrings.EVENTS_ID_PATH)
    Call<Events> delete(@Path(ApiStrings.ID) long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON,
            ApiStrings.CONTENT_TYPE
    })
    @PATCH(ApiStrings.EVENTS_ID_PATH)
    Call<Events> update(@Path(ApiStrings.ID) long id, @Body Event event, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON
    })
    @GET(ApiStrings.INSTANCES_PATH)
    Call<Instances> getInstances(@Query(ApiStrings.FROM) long from, @Query(ApiStrings.TO) long to);
}
