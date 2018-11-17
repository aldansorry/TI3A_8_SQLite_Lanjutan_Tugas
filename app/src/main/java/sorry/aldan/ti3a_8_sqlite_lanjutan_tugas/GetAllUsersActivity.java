package sorry.aldan.ti3a_8_sqlite_lanjutan_tugas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GetAllUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<UserModel> userModelArrayList;
    private CustomAdapter customAdapter;
    RecyclerView.LayoutManager lm;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_users);

        recyclerView= findViewById(R.id.rv);
        lm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(lm);
        databaseHelper = new DatabaseHelper(this);

        userModelArrayList = databaseHelper.getAllUsers();

        customAdapter= new CustomAdapter(userModelArrayList);
        recyclerView.setAdapter(customAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(GetAllUsersActivity.this, UpdateDeleteActivity.class);
                intent.putExtra("user", userModelArrayList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(GetAllUsersActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));

    }
}
