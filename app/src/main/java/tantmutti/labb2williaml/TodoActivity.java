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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private ListView listView;
    private EditText titleTextView, contentTextView;
    private Button submitBtn, addTodoBtn;
    private RadioButton fritidRBtn, inteIdagRBtn, viktigtRBtn;
    private Spinner categorySpinner;

    private List<Todo> todoList;
    private List<String> titleArray;
    private ArrayAdapter<String> adapter;
    private String TAG = "willeTodo";
    private DBHelper dbHelper;
    private User currentUser = new User();
    private int categoryToggle;

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
        setInitialAdapter();
        setListeners();
        categoryToggle = 2;
        viktigtRBtn.setChecked(false);
        fritidRBtn.setChecked(false);
        inteIdagRBtn.setChecked(true);
    }

    public void setCategoryAdapter(int category){
        todoList = dbHelper.getAllUserTodos(currentUser.getUserID());

        titleArray = new ArrayList<>();
        for (int i = 0; i < todoList.size(); i ++){
            Todo tempTodo = todoList.get(i);
            if (tempTodo.getTodoCategory() == category){
                titleArray.add(tempTodo.getTodoTitle());
            }
        }
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titleArray);
        listView.setAdapter(adapter);
    }

    public void setInitialAdapter (){
        Log.d(TAG, "USERID: " + currentUser.getUserID());

        todoList = dbHelper.getAllUserTodos(currentUser.getUserID());
        for (int i = 0; i < todoList.size(); i ++){
            Log.d(TAG, "Loop all todos : "+ i + todoList.get(i).getTodoTitle());
            Log.d(TAG, "Loop all todos : "+ i + todoList.get(i).getTodoUserID());
        }

        titleArray = new ArrayList<>();
        for (int i = 0; i < todoList.size(); i++) {
            titleArray.add(todoList.get(i).getTodoTitle());
        }

        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titleArray);

        listView.setAdapter(adapter);


    }
    public void rCategoryFritidClicked (View view) {
        categoryToggle = 1;
        inteIdagRBtn.setChecked(false);
        viktigtRBtn.setChecked(false);
    }
    public void rCategoryInteIdagClicked (View view) {
        categoryToggle = 2;
        fritidRBtn.setChecked(false);
        viktigtRBtn.setChecked(false);
    }
    public void rCategoryViktigtClicked (View view) {
        categoryToggle = 3;
        fritidRBtn.setChecked(false);
        inteIdagRBtn.setChecked(false);
    }

    public void onClickDelete (View view){

        if (todoList.size() > 0) {
            Log.d(TAG, "todo id on delete" + todoList.get(1).getTodoID());

            dbHelper.deleteTodo(todoList.get(0).getTodoID());
            todoList.remove(0);
            titleArray.remove(0);
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "onClickDelete: Empty list");
        }
    }

    private void showListAfterInput() {
        titleTextView.setVisibility(View.INVISIBLE);
        contentTextView.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);
        fritidRBtn.setVisibility(View.INVISIBLE);
        inteIdagRBtn.setVisibility(View.INVISIBLE);
        viktigtRBtn.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
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

        fritidRBtn.setVisibility(View.VISIBLE);
        inteIdagRBtn.setVisibility(View.VISIBLE);
        viktigtRBtn.setVisibility(View.VISIBLE);

        inteIdagRBtn.setChecked(true);
        viktigtRBtn.setChecked(false);
        fritidRBtn.setChecked(false);
        categoryToggle = 2;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addBtn:
                onClickAddBtn();
                return true;
            case R.id.testButton:
//                Todo todo = todoList.get(2);
//                todo.setTotoContent("Bror");
//                boolean b = dbHelper.updateTodo(todo);
//                adapter.notifyDataSetChanged();
//                Log.d(TAG, "onOptionsItemSelected: " + b);
                if (todoList.size() > 0) {
                    dbHelper.updateTodo(todoList.get(0));
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setListeners (){
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    setInitialAdapter();
                } else {
                    setCategoryAdapter(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "" + todoList.get(position).getTotoContent());
                dbHelper.logTodo(todoList.get(position));
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTextView.getText().toString();
                String content = contentTextView.getText().toString();
                int category = categoryToggle;
                Log.d(TAG, "CategoryToggle" + categoryToggle);
                int userID = currentUser.getUserID();
                dbHelper.createTodo(title, content, category, userID);
                titleArray.add(titleTextView.getText().toString());

                Todo todo = new Todo(title, content, category, userID);
                todoList.add(todo);
                showListAfterInput();
                adapter.notifyDataSetChanged();

            }
        });
    }
    private void importViewElements() {
        listView = findViewById(R.id.todoListView);
        categorySpinner = findViewById(R.id.categorySpinner);

        titleTextView = findViewById(R.id.editTodoTitle);
        contentTextView = findViewById(R.id.editTodoContent);
        submitBtn = findViewById(R.id.addTodoBtn);

        fritidRBtn = findViewById(R.id.fritid);
        inteIdagRBtn = findViewById(R.id.InteIdag);
        viktigtRBtn = findViewById(R.id.Viktigt);

        titleTextView.setVisibility(View.INVISIBLE);
        contentTextView.setVisibility(View.INVISIBLE);
        submitBtn.setVisibility(View.INVISIBLE);

        fritidRBtn.setVisibility(View.INVISIBLE);
        inteIdagRBtn.setVisibility(View.INVISIBLE);
        viktigtRBtn.setVisibility(View.INVISIBLE);
    }
}
