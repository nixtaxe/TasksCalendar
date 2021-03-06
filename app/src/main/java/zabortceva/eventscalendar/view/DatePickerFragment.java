package zabortceva.eventscalendar.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import zabortceva.eventscalendar.activity.AddEditEventActivity;
import zabortceva.eventscalendar.activity.AddEditTaskActivity;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener onDateSet;
    private int year, month, day;

    public DatePickerFragment() {
    }

    public void setCallback(DatePickerDialog.OnDateSetListener onDateSet) {
        this.onDateSet = onDateSet;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        year = args.getInt(AddEditEventActivity.EXTRA_YEAR);
        month = args.getInt(AddEditEventActivity.EXTRA_MONTH);
        day = args.getInt(AddEditEventActivity.EXTRA_DAY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }
}
