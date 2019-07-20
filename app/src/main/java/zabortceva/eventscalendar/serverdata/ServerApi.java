package zabortceva.eventscalendar.serverdata;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zabortceva.eventscalendar.activity.CalendarApplication;
import zabortceva.eventscalendar.requests.EventsApi;
import zabortceva.eventscalendar.requests.PatternsApi;
import zabortceva.eventscalendar.requests.PermissionsApi;
import zabortceva.eventscalendar.requests.TasksApi;

public class ServerApi {
    public static final String TAG = "ServerApi";
    private final static String BASE_URL = "http://planner.skillmasters.ga/";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    private static ServerApi instance;
    private static Retrofit retrofit;

    private static long cacheSize = 10L * 1024L * 1024L;

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    private ServerApi() {
        Cache myCache = new Cache(new File(CalendarApplication.getInstance().getCacheDir(), "requests"), cacheSize);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(myCache)
                .addInterceptor(logging)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Log.d(TAG, "network interceptor: called.");

                        Response response = chain.proceed(chain.request());

                        // Maximum age of cache in seconds when the application is connected to the internet
                        int maxConnectedAge = 5;

                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(maxConnectedAge, TimeUnit.SECONDS)
                                .build();

                        return response.newBuilder()
                                .removeHeader(HEADER_PRAGMA)
                                .removeHeader(HEADER_CACHE_CONTROL)
                                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                                .build();
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // Maximum age of stored cache in days
                        int maxStoredAge = 7;
                        Log.d(TAG, "offline interceptor: called.");
                        Request request = chain.request();

                        // prevent caching when network is on. For that we use the "networkInterceptor"
                        if (!CalendarApplication.hasNetwork()) {
                            CacheControl cacheControl = new CacheControl.Builder()
                                    .maxStale(maxStoredAge, TimeUnit.DAYS)
                                    .build();

                            request = request.newBuilder()
                                    .removeHeader(HEADER_PRAGMA)
                                    .removeHeader(HEADER_CACHE_CONTROL)
                                    .cacheControl(cacheControl)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public static synchronized ServerApi getInstance() {
        if (instance == null) {
            instance = new ServerApi();
        }

        return instance;
    }

    public TasksApi getTasksApi() {
        return retrofit.create(TasksApi.class);
    }

    public EventsApi getEventsApi() {
        return retrofit.create(EventsApi.class);
    }

    public PermissionsApi getPermissionsApi() {
        return retrofit.create(PermissionsApi.class);
    }

    public PatternsApi getPatternsApi() {
        return retrofit.create(PatternsApi.class);
    }
}
