package zabortceva.eventscalendar.requests;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.eventscalendar.localdata.Pattern;
import zabortceva.eventscalendar.serverdata.Patterns;

public interface PatternsApi {
    @GET(ApiStrings.PATTERNS_PATH)
    Call<Patterns> getAllPatterns(@Query(ApiStrings.COUNT) long count, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.PATTERNS_PATH)
    Call<Patterns> getPatternByInterval(@Query(ApiStrings.FROM) long from, @Query(ApiStrings.TO) long to,
                                        @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @POST(ApiStrings.PATTERNS_PATH)
    Call<Patterns> insertPattern(@Query(ApiStrings.EVENT_ID) Long event_id, @Body Pattern pattern,
                                 @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.PATTERNS_ID_PATH)
    Call<Patterns> getPatternById(@Path(ApiStrings.ID) long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @DELETE(ApiStrings.PATTERNS_ID_PATH)
    Call<Patterns> deletePattern(@Path(ApiStrings.ID) long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @PATCH(ApiStrings.PATTERNS_ID_PATH)
    Call<Patterns> updatePattern(@Path(ApiStrings.ID) long id, @Body Pattern pattern, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);
}
