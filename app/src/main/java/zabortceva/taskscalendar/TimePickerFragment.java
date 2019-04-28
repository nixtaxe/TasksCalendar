package zabortceva.taskscalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
        hour = args.getInt(AddEditTaskActivity.EXTRA_TASK_DEADLINE_HOUR);
        minutes = args.getInt(AddEditTaskActivity.EXTRA_TASK_DEADLINE_MINUTES);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), onTimeSet, hour, minutes, true);
    }
}
