package sorry.aldan.ti3a_8_sqlite_lanjutan_tugas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "users";
    private static final String TABLE_DEPARTMENT = "department";
    private static final String TABLE_POSITION = "position";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HOBBY = "hobby";
    private static final String KEY_CITY = "city";

    private static final String KEY_ID_DEPARTMENT = "id_department";
    private static final String KEY_DEPARTMENT = "name_department";

    /*CREATE TABLE students ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT......);*/

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT , "
            + KEY_HOBBY + " TEXT, "
            + KEY_CITY +" TEXT);";

    private static final String CREATE_TABLE_DEPARTMENT = "CREATE TABLE "
            + TABLE_DEPARTMENT + "(" + KEY_ID_DEPARTMENT + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ KEY_DEPARTMENT + " TEXT );";

    private static final String CREATE_TABLE_POSITION = "CREATE TABLE "
            + TABLE_POSITION + "(" + KEY_ID + " INTEGER,"+ KEY_ID_DEPARTMENT + " INTEGER , "
            + "FOREIGN KEY("+KEY_ID+") REFERENCES "+TABLE_USER+"("+KEY_ID+"), "
            + "FOREIGN KEY("+KEY_ID_DEPARTMENT+") REFERENCES "+TABLE_DEPARTMENT+"("+KEY_ID_DEPARTMENT+"));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DEPARTMENT);
        db.execSQL(CREATE_TABLE_POSITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DEPARTMENT + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_POSITION + "'");
        onCreate(db);
    }

    public void addDepartment(String nama){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEPARTMENT, nama);
        // db.insert(TABLE_USER, null, values);
        long id = db.insertWithOnConflict(TABLE_DEPARTMENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
    public String[] getDepartment(){


        String selectQuery = "SELECT  * FROM " + TABLE_DEPARTMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        String[] departmetnList = new String[c.getCount()];
        int i = 0;
        if (c.moveToFirst()) {
            do {
                departmetnList[i] = c.getString(c.getColumnIndex(KEY_DEPARTMENT));
                i++;
            } while (c.moveToNext());
        }
        return departmetnList;
    }
    public void addUser(String name, String hobby, String city,String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        //adding user name in users table
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_HOBBY, hobby);
        values.put(KEY_CITY, city);
        // db.insert(TABLE_USER, null, values);
        long id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //getting user city where id = id from user_city table
        String selectDepartmentQuery = "SELECT  * FROM " + TABLE_DEPARTMENT+" WHERE "+KEY_DEPARTMENT+" = '" + department+"';";
        //SQLiteDatabase dbCity = this.getReadableDatabase();
        Cursor cDepartment = db.rawQuery(selectDepartmentQuery, null);
        Long id_department = (long) 0;
        if (cDepartment.moveToFirst()) {
            do {
                id_department = (long) cDepartment.getInt(cDepartment.getColumnIndex(KEY_ID_DEPARTMENT));
            } while (cDepartment.moveToNext());
        }

        //adding user hobby in users_hobby table
        ContentValues valuesHobby = new ContentValues();
        valuesHobby.put(KEY_ID, id);
        valuesHobby.put(KEY_ID_DEPARTMENT, id_department);
        db.insert(TABLE_POSITION, null, valuesHobby);
    }

    public ArrayList<UserModel> getAllUsers() {
        ArrayList<UserModel> userModelArrayList = new ArrayList<UserModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                userModel.setNama(c.getString(c.getColumnIndex(KEY_NAME)));
                userModel.setHobby(c.getString(c.getColumnIndex(KEY_HOBBY)));
                userModel.setCity(c.getString(c.getColumnIndex(KEY_CITY)));

                //getting user hobby where id = id from user_hobby table
                String selectPositionQuery = "SELECT  * FROM " + TABLE_POSITION +" WHERE "+KEY_ID+" = "+ userModel.getId();
                //SQLiteDatabase dbhobby = this.getReadableDatabase();
                Cursor cPosition = db.rawQuery(selectPositionQuery, null);

                long id_department = 0;
                if (cPosition.moveToFirst()) {
                    do {
                        id_department = Long.valueOf(cPosition.getString(cPosition.getColumnIndex(KEY_ID_DEPARTMENT)));
                    } while (cPosition.moveToNext());
                }

                //getting user city where id = id from user_city table
                String selectDepartmentQuery = "SELECT  * FROM " + TABLE_DEPARTMENT+" WHERE "+KEY_ID_DEPARTMENT+" = " + id_department;;
                //SQLiteDatabase dbCity = this.getReadableDatabase();
                Cursor cDepartment = db.rawQuery(selectDepartmentQuery, null);

                if (cDepartment.moveToFirst()) {
                    do {
                        userModel.setDepartment(cDepartment.getString(cDepartment.getColumnIndex(KEY_DEPARTMENT)));
                    } while (cDepartment.moveToNext());
                }

                // adding to Students list
                userModelArrayList.add(userModel);
            } while (c.moveToNext());
        }
        return userModelArrayList;
    }

    public void updateUser(int id, String name, String hobby, String city, String department) {
        SQLiteDatabase db = this.getWritableDatabase();

        // updating name in users table
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_HOBBY, hobby);
        values.put(KEY_CITY, city);
        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        //getting user city where id = id from user_city table
        String selectDepartmentQuery = "SELECT  * FROM " + TABLE_DEPARTMENT+" WHERE "+KEY_DEPARTMENT+" = '" + department+"';";
        //SQLiteDatabase dbCity = this.getReadableDatabase();
        Cursor cDepartment = db.rawQuery(selectDepartmentQuery, null);
        Long id_department = (long) 0;
        if (cDepartment.moveToFirst()) {
            do {
                id_department = (long) cDepartment.getInt(cDepartment.getColumnIndex(KEY_ID_DEPARTMENT));
            } while (cDepartment.moveToNext());
        }

        // updating hobby in users_hobby table
        ContentValues valuesHobby = new ContentValues();
        valuesHobby.put(KEY_ID_DEPARTMENT, id_department);
        db.update(TABLE_POSITION, valuesHobby, KEY_ID + " = ?", new String[]{String.valueOf(id)});
//
//        // updating city in users_city table
//        ContentValues valuesCity = new ContentValues();
//        valuesCity.put(KEY_CITY, city);
//        db.update(TABLE_USER_CITY, valuesCity, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteUSer(int id) {

        // delete row in students table based on id
        SQLiteDatabase db = this.getWritableDatabase();


        //deleting from users_hobby table
        db.delete(TABLE_POSITION, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        //deleting from users table
        db.delete(TABLE_USER, KEY_ID + " = ?",new String[]{String.valueOf(id)});


    }

}