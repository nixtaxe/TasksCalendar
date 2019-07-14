package zabortceva.eventscalendar.requests;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Context.MODE_PRIVATE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TOKEN = "TOKEN";
    private static final String PREFERENCS = "PREFERENCES";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences(PREFERENCS, MODE_PRIVATE).edit().putString(TOKEN, s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(PREFERENCS, MODE_PRIVATE).getString(TOKEN, null);
    }
}