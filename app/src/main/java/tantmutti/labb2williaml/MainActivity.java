package tantmutti.labb2williaml;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "willeMain";

    private ListView listView;
    EditText userNameEditText, userPasswordEditText;
    Button regBtn, logInBtn;
    DBHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<String> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        importViewElements();

        accounts = dbHelper.getAllUserNames();
        //titleArray = dbHelper.getAllUserNames();

        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, accounts);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                userNameEditText.setText(accounts.get(position).toString());

            }
        });

    }

    private void importViewElements(){
        listView = findViewById(R.id.mainMenuListView);
        userNameEditText = (EditText) findViewById(R.id.userNameTextInput);
        userPasswordEditText = (EditText)findViewById(R.id.userPasswordTextInput);
        logInBtn = findViewById(R.id.logInBtn);
        regBtn = findViewById(R.id.regBtn);
        dbHelper  = new DBHelper(this);
    }

    public void onClickLogin(View view){

        String userName = userNameEditText.getText().toString();
        String userPassword = userPasswordEditText.getText().toString();

        boolean b = dbHelper.loginUser(userName, userPassword);
        if (b){
            User user = dbHelper.getCurrentUser();
            int userID = user.getUserID();
            Intent intent = new Intent(this, TodoActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);

            Log.d(TAG, "Du är inloggad");
        }

    }

    public void onClickReg (View v){
        String userName = userNameEditText.getText().toString();
        String userPassword = userPasswordEditText.getText().toString();

        boolean userExists = dbHelper.checkIfuserExists(userName);


        if (!userExists){
            boolean b = dbHelper.createUser(userName, userPassword);
            if (b) {
                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClickReg: " + b);
                accounts.add(userName);
                adapter.notifyDataSetChanged();
            }

        } else {
            Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show();
        }
    }
}
