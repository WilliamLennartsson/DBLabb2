package tantmutti.labb2williaml;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private ListView listView;
    private EditText titleTextView, contentTextView;
    private Button submitBtn, addTodoBtn;

    private List<Todo> todoList;
    private List<String> titleArray;
    private ArrayAdapter<String> adapter;
    private String TAG = "willeTodo";
    private DBHelper dbHelper;
    private User currentUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        importViewElements();
        dbHelper = new DBHelper(this);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null){
            int userID = b.getInt("userID");
            currentUser.setUserID(userID);
            Log.d(TAG, "TODO userID: " + userID);
        }
        Log.d(TAG, "USERID: " + currentUser.getUserID());

        todoList = dbHelper.getAllUserTodos(currentUser.getUserID());
        for (int i = 0; i < todoList.size(); i ++){
            Log.d(TAG, "Loop all todos : "+ i + todoList.get(i).getTodoTitle());
            Log.d(TAG, "Loop all todos : "+ i + todoList.get(i).getTodoUserID());
        }

        titleArray = new ArrayList<>();
        //titleArray = dbHelper.getAllUserNames();

        for (int i = 0; i < todoList.size(); i++) {
            titleArray.add(todoList.get(i).getTodoTitle());
        }

        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titleArray);

        listView.setAdapter(adapter);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTextView.getText().toString();
                String content = contentTextView.getText().toString();
                int category = 1;
                int userID = currentUser.getUserID();
                dbHelper.createTodo(title, content, category, userID);

                titleArray.add(titleTextView.getText().toString());

                showListAfterInput();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showListAfterInput() {
        titleTextView.setVisibility(View.INVISIBLE);
        contentTextView.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }
    private void importViewElements(){
        listView = findViewById(R.id.todoListView);

        titleTextView = findViewById(R.id.editTodoTitle);
        contentTextView = findViewById(R.id.editTodoContent);
        submitBtn = findViewById(R.id.addTodoBtn);

        titleTextView.setVisibility(View.INVISIBLE);
        contentTextView.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todomenu, menu);
        return true;
    }
    private void onClickAddBtn(){
        listView.setVisibility(View.INVISIBLE);

        titleTextView.setVisibility(View.VISIBLE);
        contentTextView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addBtn:
                onClickAddBtn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
