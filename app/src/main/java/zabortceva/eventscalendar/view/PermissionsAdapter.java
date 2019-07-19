package zabortceva.eventscalendar.view;

import android.icu.text.SimpleDateFormat;
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
import zabortceva.eventscalendar.localdata.Permission;

public class PermissionsAdapter extends ListAdapter<Permission, PermissionsAdapter.PermissionViewHolder> {
    private OnItemClickListener listener;
    public PermissionsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Permission> DIFF_CALLBACK = new DiffUtil.ItemCallback<Permission>() {
        @Override
        public boolean areItemsTheSame(@NonNull Permission permission, @NonNull Permission p1) {
            return Objects.equals(permission.getId(), p1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Permission permission, @NonNull Permission p1) {
            return false;
        }
    };

    @NonNull
    @Override
    public PermissionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.permission_item, viewGroup, false);
        return new PermissionViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull PermissionViewHolder permissionViewHolder, int i) {
        permissionViewHolder.bind(getItem(i));
    }

    class PermissionViewHolder extends RecyclerView.ViewHolder {
        TextView listItemTimeView;
        TextView listItemNameView;
        TextView listItemUserNameView;

        public PermissionViewHolder(View itemView) {
            super(itemView);

            listItemTimeView = itemView.findViewById(R.id.permission_created_time);
            listItemNameView = itemView.findViewById(R.id.permission_name);
            listItemUserNameView = itemView.findViewById(R.id.user_name);

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
        private void bind(Permission permission) {
            listItemTimeView.setText(new SimpleDateFormat("HH:mm").format(permission.getCreated_at()));
            listItemNameView.setText(permission.getName());
            //listItemUserNameView.setText(permission.getUser_id());
        }

    }

    public Permission getTaskAt(int position) {
        return getItem(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Permission permission);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
