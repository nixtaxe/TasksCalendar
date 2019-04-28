package zabortceva.taskscalendar.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import zabortceva.taskscalendar.AddEditTaskActivity;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification()
                .setContentTitle(intent.getStringExtra(AddEditTaskActivity.EXTRA_TASK_NAME))
                .setContentText(intent.getStringExtra(AddEditTaskActivity.EXTRA_TASK_DETAILS));
        notificationHelper.getManager().notify(1, nb.build());
    }
}
