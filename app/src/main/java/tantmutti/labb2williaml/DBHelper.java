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
    private final String TAG = "willeDB";
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;


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
                                                    + COLUMN_TODO_CATEGORY_ID + " INTEGER,"
                                                    + COLUMN_TODO_USER_ID + " INTEGER"
                                                    + " )";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TODO);
        Log.d(TAG, "onCreate: ");
    }



    private void importDummyData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_NAME_CATEGORY +" (" + COLUMN_CATEGORY_NAME + ") VALUES ('Fritid'), ('Inte Idag'), ('Viktigt')");



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

    private boolean checkIfuserExists(String newUser){
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
                        Log.d(TAG, "loginUser: ");
                        break;
                    }
                }
            }
        }

        return b;
    }

    private List<User> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();

        List<User> users = new ArrayList<>();
        Cursor c = db.query(DBHelper.TABLE_NAME_USERS, null,null,null,null,null,null);

        boolean success = c.moveToFirst();
        if (success){
            while (c.moveToNext()){
                User user = new User();
                user.setUserName(c.getString(c.getColumnIndex(DBHelper.COLUMN_USERS_USER)));
                user.setUserPassword(c.getString(c.getColumnIndex(DBHelper.COLUMN_USERS_PASSWORD)));
                users.add(user);

            }
        }

        db.close();
        return users;
    }

    private List<Todo> getAllTodos(){
        List<Todo> todos = new ArrayList<>();


        return todos;
    }

        public boolean createTodo(String todoTitle, String todoContent, int categoryID){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(COLUMN_TODO_TITLE, todoTitle);
        content.put(COLUMN_TODO_CONTENTS, todoContent);
        content.put(COLUMN_TODO_ID, categoryID);
        long success = db.insert(TABLE_NAME_TODO, null, content);

        db.close();
        return success >=0;

    }

    public boolean deleteTodo(){
        boolean b = false;

        return b;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
