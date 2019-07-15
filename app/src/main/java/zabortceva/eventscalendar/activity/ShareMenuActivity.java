package zabortceva.eventscalendar.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import zabortceva.eventscalendar.R;

public class ShareMenuActivity extends AppCompatActivity {
    private AppCompatButton shareButton;
    private AppCompatButton viewPermissionsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_menu);

        shareButton = findViewById(R.id.share_button);
        viewPermissionsButton = findViewById(R.id.view_permissions_button);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(ShareMenuActivity.this, ShareActivity.class);

            }
        });
    }
}
