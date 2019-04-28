package zabortceva.taskscalendar;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    private static final String TAG = "DayActivity";

    private TextView date;
    private Button btnGoToCalendar;
    private RecyclerView tasksView;
//    private TasksAdapter tasksAdapter;
    private Button btnAddTask;

    public List<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        date = (TextView) findViewById(R.id.date);
//        btnGoToCalendar = (Button) findViewById(R.id.btnGoCalendar);
        tasksView = (RecyclerView) findViewById(R.id.tasksView);
//        btnAddTask = (Button) findViewById(R.id.add_task_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tasksView.setLayoutManager(layoutManager);

//        tasksAdapter = new TasksAdapter(this);
//        tasksView.setAdapter(tasksAdapter);

        Intent incomingIntent = getIntent();
        String sDate = incomingIntent.getStringExtra("date");
        date.setText(sDate);

        btnGoToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.add(String.valueOf(1));
//                tasksAdapter.notifyDataSetChanged();
            }
        });
    }
}
