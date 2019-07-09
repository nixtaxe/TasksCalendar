package zabortceva.taskscalendar.serverdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zabortceva.taskscalendar.requests.EventsApi;
import zabortceva.taskscalendar.requests.TasksApi;

public abstract class ServerDatabase {
    private final static String BASE_URL = "http://planner.skillmasters.ga/";

    private static Retrofit retrofit = null;
    public abstract TasksApi taskApi();

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    public static synchronized Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static TasksApi getTasksTable() {
        return retrofit.create(TasksApi.class);
    }

    public static EventsApi getEventsTable() {
        return retrofit.create(EventsApi.class);
    }
}
