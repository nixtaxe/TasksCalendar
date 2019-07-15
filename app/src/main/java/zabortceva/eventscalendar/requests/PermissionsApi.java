package zabortceva.eventscalendar.requests;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.serverdata.Permissions;
import zabortceva.eventscalendar.serverdata.User;

public interface PermissionsApi {

    @GET(ApiStrings.GRANT_PERMISSION_PATH)
    Call<Void> grant(@Query(ApiStrings.USER_ID) String user_id, @Query(ApiStrings.ENTITY_ID) String entity_id, @Query(ApiStrings.ENTITY_TYPE) String entity_type,
                     @Query(ApiStrings.ACTION) String action, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_TEXT
    })
    @GET(ApiStrings.SHARE_PATH)
    Call<String> shareOne(@Query(ApiStrings.ENTITY_ID) String entity_id, @Query(ApiStrings.ENTITY_TYPE) String entity_type,
                       @Query(ApiStrings.ACTION) String action, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @POST(ApiStrings.SHARE_PATH)
    Call<String> shareMultiple(@Query(ApiStrings.PERMISSIONS) Permission[] permissions, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @GET(ApiStrings.SHARE_TOKEN_PATH)
    Call<Permissions> getSharedToYou(@Path(ApiStrings.TOKEN) String token, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON
    })
    @GET(ApiStrings.PERMISSIONS_PATH)
    Call<Permissions> getSharedByYou(@Query(ApiStrings.ENTITY_TYPE) String entity_type, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON
    })
    @DELETE(ApiStrings.PERMISSIONS_ID_PATH)
    Call<Permissions> revokePermission(@Path(ApiStrings.ID) long id, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);

    @Headers({
            ApiStrings.ACCEPT_JSON
    })
    @GET(ApiStrings.USER_PATH)
    Call<User> findUser(@Query(ApiStrings.EMAIL) String email, @Header(ApiStrings.X_FIREBASE_AUTH) String idToken);
}
