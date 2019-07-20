package zabortceva.eventscalendar.activity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CalendarApplication extends Application {
    private static CalendarApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null){
            instance = this;
        }
    }

    public static CalendarApplication getInstance(){
        return instance;
    }

    public static boolean hasNetwork(){
        return instance.isNetworkConnected();
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
