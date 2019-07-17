package zabortceva.eventscalendar.view;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;
import zabortceva.eventscalendar.serverdata.FullEvent;
import zabortceva.eventscalendar.serverdata.Instance;

public class FullEventsAdapter extends ListAdapter<FullEvent, FullEventsAdapter.FullEventViewHolder> {
    private OnItemClickListener listener;
    public FullEventsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<FullEvent> DIFF_CALLBACK = new DiffUtil.ItemCallback<FullEvent>() {
        @Override
        public boolean areItemsTheSame(@NonNull FullEvent event, @NonNull FullEvent t1) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(@NonNull FullEvent event, @NonNull FullEvent t1) {
            return false;
        }
    };

    @NonNull
    @Override
    public FullEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_item, viewGroup, false);
        return new FullEventViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull FullEventViewHolder eventViewHolder, int i) {
        eventViewHolder.bind(getItem(i));
    }

    class FullEventViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNameView;
        TextView listItemDetailsView;
        TextView listItemLocationView;
        TextView listItemTimeView;

        public FullEventViewHolder(View itemView) {
            super(itemView);

            listItemNameView = itemView.findViewById(R.id.event_item_name);
            listItemDetailsView = itemView.findViewById(R.id.event_item_details);
            listItemLocationView = itemView.findViewById(R.id.event_item_location);
            listItemTimeView = itemView.findViewById(R.id.event_item_time);

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
        private void bind(FullEvent fullEvent) {
            Event event = fullEvent.getEvent();
            if (event != null) {
                listItemNameView.setText(String.valueOf(event.getName()));
                listItemDetailsView.setText(event.getDetails());
                listItemLocationView.setText(event.getLocation());
            }

            Instance instance = fullEvent.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            listItemTimeView.setText(sdf.format(instance.getStarted_at()) + " - " + sdf.format(instance.getEnded_at()));
        }

    }

    public FullEvent getEventAt(int position) {
        return getItem(position);
    }

    public interface OnItemClickListener {
        void onItemClick(FullEvent event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
