package zabortceva.eventscalendar.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import zabortceva.eventscalendar.activity.AddEditTaskActivity;

import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_HOUR;
import static zabortceva.eventscalendar.activity.AddEditEventActivity.EXTRA_MINUTE;

public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener onTimeSet;
    private int hour, minutes;

    public TimePickerFragment() {
    }

    public void setCallback(TimePickerDialog.OnTimeSetListener onTimeSet) {
        this.onTimeSet = onTimeSet;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        hour = args.getInt(EXTRA_HOUR);
        minutes = args.getInt(EXTRA_MINUTE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSet, hour, minutes, true);
    }
}
