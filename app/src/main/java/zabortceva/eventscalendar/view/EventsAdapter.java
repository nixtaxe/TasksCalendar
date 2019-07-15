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

import java.util.Objects;

import zabortceva.eventscalendar.R;
import zabortceva.eventscalendar.localdata.Event;

public class EventsAdapter extends ListAdapter<Event, EventsAdapter.EventViewHolder> {
    private OnItemClickListener listener;
    public EventsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event event, @NonNull Event t1) {
            return Objects.equals(event.getId(), t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event event, @NonNull Event t1) {
            return false;
        }
    };

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_item, viewGroup, false);
        return new EventViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.bind(getItem(i));
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView listItemNameView;
        TextView listItemDetailsView;
        TextView listItemLocationView;

        public EventViewHolder(View itemView) {
            super(itemView);
            
            listItemNameView = itemView.findViewById(R.id.event_item_name);
            listItemDetailsView = itemView.findViewById(R.id.event_item_details);
            listItemLocationView = itemView.findViewById(R.id.event_item_location);

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
        private void bind(Event event) {
            listItemNameView.setText(String.valueOf(event.getName()));
            listItemDetailsView.setText(event.getDetails());
            listItemLocationView.setText(event.getLocation());
        }

    }

    public Event getEventAt(int position) {
        return getItem(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
