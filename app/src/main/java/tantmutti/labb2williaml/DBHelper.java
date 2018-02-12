package tantmutti.labb2williaml;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrx on 2018-02-05.
 */

public class DBHelper extends SQLiteOpenHelper{

    private User currentUser = new User();

    private final String TAG = "willeDB";
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;


    //User Table
    private static final String TABLE_NAME_USERS = "Users";
    private static final String COLUMN_USERS_ID = "userID";
    private static final String COLUMN_USERS_USER = "user";
    private static final String COLUMN_USERS_PASSWORD = "password";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_NAME_USERS + " ("
                                                    + COLUMN_USERS_ID + " INTEGER PRIMARY KEY,"
                                                    + COLUMN_USERS_USER + " TEXT,"
                                                    + COLUMN_USERS_PASSWORD + " TEXT"
                                                    + " )";

    private static final String TABLE_NAME_CATEGORY = "Category";
    private static final String COLUMN_CATEGORY_ID = "categoryID";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_NAME_CATEGORY + " ("
                                                        + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY,"
                                                        + COLUMN_CATEGORY_NAME + " TEXT"
                                                        + " )";

    private static final String TABLE_NAME_TODO = "ToDo";
    private static final String COLUMN_TODO_ID = "toDoID";
    private static final String COLUMN_TODO_TITLE = "toDoTitle";
    private static final String COLUMN_TODO_CONTENTS = "toDoContents";
    private static final String COLUMN_TODO_CATEGORY_ID = "toDoCategoryID";
    private static final String COLUMN_TODO_USER_ID = "toDoUserID";


    private static final String CREATE_TABLE_TODO = " CREATE TABLE " + TABLE_NAME_TODO + " ("
                                                    + COLUMN_TODO_ID + " INTEGER PRIMARY KEY,"
                                                    + COLUMN_TODO_TITLE + " TEXT,"
                                                    + COLUMN_TODO_CONTENTS + " TEXT,"
                                                    + COLUMN_TODO_USER_ID + " INTEGER,"
                                                    + COLUMN_TODO_CATEGORY_ID + " INTEGER"
                                                    + " )";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TODO);
        importDummyData(db);
        Log.d(TAG, "onCreate: ");
    }



    private void importDummyData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_NAME_CATEGORY + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Fritid'), ('Inte Idag'), ('Viktigt')");



        //db.execSQL("INSERT INTO todolist (todolistTitle, todolistContent, todolistDate, todolistCategoryId) " +
        //      "VALUES ('Programmera i Java', 'Arbeta med en app i Android Studio', '2018-01-17', 1), " +
        //    "('Köpa skivor', 'Köpa nya skivor', '2018-01-18', 2)");
    }



    public List<String> getAllUserNames(){
        SQLiteDatabase db = getReadableDatabase();

        List<String> userNames = new ArrayList<>();


        Cursor c = db.query(DBHelper.TABLE_NAME_USERS, null,null,null,null,null,null);
        boolean success = c.moveToFirst();
        if (success){
            while (c.moveToNext()){
                String user = c.getString(c.getColumnIndex(DBHelper.COLUMN_USERS_USER));

                userNames.add(user);

            }
        }
        return userNames;
    }

    public boolean checkIfuserExists(String newUser){
        boolean b = false;

        List<String> userNames = getAllUserNames();
        for (int i = 0; i < userNames.size(); i++){
            if (userNames.get(i).equals(newUser)){
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean createUser (String name, String password) {

        boolean userExists = checkIfuserExists(name);
        long success = 0;


        if (!userExists){

            SQLiteDatabase db = getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put(COLUMN_USERS_USER, name);
            content.put(COLUMN_USERS_PASSWORD, password);

            success = db.insert(TABLE_NAME_USERS, null, content);

            Log.d(TAG, String.valueOf(success));

            db.close();
        }

        return success > 0;
    }

    public boolean loginUser (String userName, String userPassword){
        boolean b = false;
        List<User> users = getAllUsers();


        if (!users.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                User tempUser = users.get(i);
                if (tempUser.getUserName().equals(userName)) {
                    if (tempUser.getUserPassword().equals(userPassword)) {
                        b = true;
                        currentUser = tempUser;
                        Log.d(TAG, "loginUser: ");
                        break;
                    }
                }
            }
        }
        return b;
    }


    public List<User> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();

        List<User> users = new ArrayList<>();
        Cursor c = db.query(DBHelper.TABLE_NAME_USERS, null,null,null,null,null,null);

        boolean success = c.moveToFirst();
        if (success){
            while (c.moveToNext()){
                User user = new User();
                user.setUserName(c.getString(c.getColumnIndex(DBHelper.COLUMN_USERS_USER)));
                user.setUserPassword(c.getString(c.getColumnIndex(DBHelper.COLUMN_USERS_PASSWORD)));
                user.setUserID(c.getInt(c.getColumnIndex(DBHelper.COLUMN_USERS_ID)));
                users.add(user);

            }
        }

        db.close();
        return users;
    }

    public User getCurrentUser (){
        return currentUser;
    }


        public boolean createTodo(String todoTitle, String todoContent, int categoryID, int userID){
        String query = "INSERT INTO " + DBHelper.TABLE_NAME_CATEGORY+ " VALUES (0,?,?)";

            SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(COLUMN_TODO_TITLE, todoTitle);
        content.put(COLUMN_TODO_CONTENTS, todoContent);
        content.put(COLUMN_TODO_CATEGORY_ID, categoryID);
        content.put(COLUMN_TODO_USER_ID, userID);
            Log.d(TAG, "viktigtRBtn.setChecked(false); todo: " + userID);
        long success = db.insert(TABLE_NAME_TODO, null, content);

        db.close();
        return success >=0;
    }

    public boolean deleteTodo(int todoID){
        boolean b = false;

        SQLiteDatabase db = getWritableDatabase();
        String quary = "DELETE FROM " + TABLE_NAME_TODO + " WHERE " + COLUMN_TODO_ID + " = " + todoID;
        db.execSQL(quary);
        db.close();
        return b;
    }


    public void updateTodo(Todo todo){
            SQLiteDatabase db = getWritableDatabase();
            //String syster = "UPDATE Todo SET TODO_TITLE = todo.getTitle, contents = todo.getcontentns WHERE Todo.id"


            ContentValues content = new ContentValues();
            content.put(COLUMN_TODO_CONTENTS, "UPDATEDMAADDAAAFAAAAKKKAAAAAA");
            long success = db.update(DBHelper.TABLE_NAME_TODO, content, "toDoID=" + todo.getTodoID(), null);
            Log.d(TAG, "updateTodo: " + success);
            db.close();

    }

    public void logTodo (Todo todo){
        Log.d(TAG, "inside logTodo + categoryID: " + todo.getTodoCategory());
        SQLiteDatabase db = getReadableDatabase();

        String que = "SELECT * FROM " + TABLE_NAME_TODO +
                " JOIN " + TABLE_NAME_CATEGORY + " ON " + COLUMN_TODO_CATEGORY_ID + " = " + COLUMN_CATEGORY_ID +
                " WHERE " + COLUMN_CATEGORY_ID + " = " + todo.getTodoCategory() + ";";
        Cursor c = db.rawQuery(que, null);

        if (c.getCount() > 0){
            Todo tempTodo = new Todo();
            while (c.moveToNext()){
                tempTodo.setCategoryString(c.getString(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME)));
            }
            Log.d(TAG, ":tempTodo in DB " + tempTodo.getCategoryString() + " Count: " +c.getCount());

        }

        db.close();

    }
    //Same as logTodo method but without query
    public void logCategory (Todo todo){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(DBHelper.TABLE_NAME_CATEGORY, null, null, null, null, null, null);

        boolean success = c.moveToFirst();

        if (success) {
            while (c.moveToNext()){
                if (c.getInt(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_ID)) == todo.getTodoCategory()){
                    Log.d(TAG, "logCategory: " + todo.getTodoCategory() + " Cat: " + c.getString(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME)));
                }
            }
        }

        db.close();
    }

    public List<Todo> getAllUserTodos (int userID){
        List<Todo> userTodos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(DBHelper.TABLE_NAME_TODO, null,null,null,null,null,null);

        boolean success = c.moveToFirst();
        if (success){

            while (c.moveToNext()){
                if ((c.getInt(c.getColumnIndex(DBHelper.COLUMN_TODO_USER_ID)) == userID)){

                    Todo todo = new Todo();
                    todo.setTodoID(c.getInt(c.getColumnIndex(DBHelper.COLUMN_TODO_ID)));
                    todo.setTodoTitle(c.getString(c.getColumnIndex(DBHelper.COLUMN_TODO_TITLE)));
                    todo.setTotoContent(c.getString(c.getColumnIndex(DBHelper.COLUMN_TODO_CONTENTS)));
                    todo.setTodoCategory(c.getInt(c.getColumnIndex(DBHelper.COLUMN_TODO_CATEGORY_ID)));
                    todo.setTodoUserID(c.getInt(c.getColumnIndex(DBHelper.COLUMN_TODO_USER_ID)));

                    Log.d(TAG, "getAllUserTodos: userID " + DBHelper.COLUMN_TODO_USER_ID);
                    Log.d(TAG, "Added:");
                    userTodos.add(todo);
                }
            }
        }

        db.close();
        return userTodos;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
