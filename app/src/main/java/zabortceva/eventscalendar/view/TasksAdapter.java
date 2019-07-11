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
            return task.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task task, @NonNull Task t1) {
            return task.getName().equals(t1.getName()) &&
                    Objects.equals(task.getDetails(), t1.getDetails()) &&
                    Objects.equals(task.getEvent_id(), t1.getEvent_id()) &&
                    Objects.equals(task.getParent_id(), t1.getParent_id()) &&
                    task.getDeadline_at().equals(t1.getDeadline_at()) &&
                    task.getCreated_at().equals(t1.getCreated_at());
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
        TextView listItemEventNameView;
        TextView listItemTimeView;
        TextView listItemNameView;
        TextView listItemTaskContentView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            listItemEventNameView = itemView.findViewById(R.id.event_name);
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
            listItemEventNameView.setText(String.valueOf(task.getEvent_id()));
            listItemTimeView.setText(new SimpleDateFormat("HH:mm").format(task.getDeadline_at()));
            listItemNameView.setText(task.getName());
            listItemTaskContentView.setText(task.getDetails());
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
