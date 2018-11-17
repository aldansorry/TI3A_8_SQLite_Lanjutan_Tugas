package sorry.aldan.ti3a_8_sqlite_lanjutan_tugas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateDeleteActivity extends AppCompatActivity {


    private UserModel userModel;
    private EditText etname,ethobby,etcity;
    private Button btnupdate,btndelete;
    private DatabaseHelper databaseHelper;
    private Spinner department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        Intent intent= getIntent();
        userModel= (UserModel) intent.getSerializableExtra("user");
        databaseHelper= new DatabaseHelper(this);

        etname=(EditText) findViewById(R.id.etname);
        ethobby=(EditText) findViewById(R.id.ethobby);
        etcity=(EditText) findViewById(R.id.etcity);
        btndelete=(Button) findViewById(R.id.btndelete);
        btnupdate=(Button) findViewById(R.id.btnupdate);
        department = findViewById(R.id.department);

        String[] arraySpinner = databaseHelper.getDepartment();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);

        etname.setText(userModel.getNama());
        ethobby.setText(userModel.getHobby());
        etcity.setText(userModel.getCity());
        department.setSelection(adapter.getPosition(userModel.getDepartment()));
        btnupdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v ){
                databaseHelper.updateUser(userModel.getId(),etname.getText().toString(),ethobby.getText().toString(),etcity.getText().toString(),department.getSelectedItem().toString());
                Toast.makeText(UpdateDeleteActivity.this, "Updated Successfully!",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(UpdateDeleteActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v ){
                databaseHelper.deleteUSer(userModel.getId());
                Toast.makeText(UpdateDeleteActivity.this, "Deleted Successfully!",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(UpdateDeleteActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
