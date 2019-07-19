package zabortceva.eventscalendar.view;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import scala.util.parsing.combinator.testing.Str;
import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Task;

public class TasksAdapter extends ListAdapter<Task, TasksAdapter.TaskViewHolder> {
    private OnItemClickListener listener;
    public TasksAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task task, @NonNull Task t1) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task task, @NonNull Task t1) {
            return false;
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_item, viewGroup, false);
        return new TaskViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bind(getItem(i));
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
//        TextView listItemEventNameView;
        TextView listItemTimeView;
        TextView listItemDateView;
        TextView listItemNameView;
        TextView listItemTaskContentView;

        String taskNamePlaceholder = "Task: %s";
        String taskDetailsPlaceholder = "Details: %s";

        public TaskViewHolder(View itemView) {
            super(itemView);

//            listItemEventNameView = itemView.findViewById(R.id.event_name);
            listItemDateView = itemView.findViewById(R.id.task_date);
            listItemTimeView = itemView.findViewById(R.id.task_time);
            listItemNameView = itemView.findViewById(R.id.task_name);
            listItemTaskContentView = itemView.findViewById(R.id.task_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void bind(Task task) {
//            listItemEventNameView.setText(String.valueOf(task.getEvent_id()));
            long deadline = task.getDeadline_at();
            listItemDateView.setText(DateTimeString.getDateString(deadline));
            listItemTimeView.setText(DateTimeString.getTimeString(deadline));
            listItemNameView.setText(String.format(taskNamePlaceholder, task.getName()));
            listItemTaskContentView.setText(String.format(taskDetailsPlaceholder, task.getDetails()));
            if (Objects.equals(task.getDetails(), null) || Objects.equals(task.getDetails().trim(), ""))
                listItemTaskContentView.setVisibility(View.GONE);
            else
                listItemTaskContentView.setVisibility(View.VISIBLE);
            }

    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
