package zabortceva.eventscalendar.serverdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zabortceva.eventscalendar.localdata.Permission;
import zabortceva.eventscalendar.requests.EventsApi;
import zabortceva.eventscalendar.requests.PatternsApi;
import zabortceva.eventscalendar.requests.PermissionsApi;
import zabortceva.eventscalendar.requests.TasksApi;

public abstract class ServerDatabase {
    private final static String BASE_URL = "http://planner.skillmasters.ga/";

    private static Retrofit retrofit;

    public abstract TasksApi taskApi();

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    public static synchronized Retrofit getInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

    public static TasksApi getTasksApi() {
        Retrofit retrofit = ServerDatabase.getInstance();
        return retrofit.create(TasksApi.class);
    }

    public static EventsApi getEventsApi() {
        Retrofit retrofit = ServerDatabase.getInstance();
        return retrofit.create(EventsApi.class);
    }

    public static PermissionsApi getPermissionsApi() {
        Retrofit retrofit = ServerDatabase.getInstance();
        return retrofit.create(PermissionsApi.class);
    }

    public static PatternsApi getPatternsApi() {
        Retrofit retrofit = ServerDatabase.getInstance();
        return retrofit.create(PatternsApi.class);
    }
}
