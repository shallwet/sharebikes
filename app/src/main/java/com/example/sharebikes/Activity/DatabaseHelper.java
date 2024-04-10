package com.example.sharebikes.Activity;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 数据库名称
    private static final String DATABASE_NAME = "MyDatabase.db";

    // 数据库版本号
    private static final int DATABASE_VERSION = 1;

    // 表名称和字段名称
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // 创建表的 SQL 语句
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + "users" + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username" + " TEXT," +
                    "password" + " TEXT," +
                    "phonenumber" + " TEXT," +
                    "sex" + " TEXT," +
                    "balance" + " REAL," +
                    "createdate" + " TEXT," +
                    "updatedate" + " TEXT" + ")";

    private Context mContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库表
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("DatabaseHelper", "Database table created successfully");
        //Toast.makeText(mContext, "Create table successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 更新数据库表结构
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // 添加用户
    public boolean addUser(String username, String password, String phonenumber, String sex, double balance, String createdate, String updatedate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put("phonenumber", phonenumber);
        values.put("sex", sex);
        values.put("balance", balance);
        values.put("createdate", createdate);
        values.put("updatedate", updatedate);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        Log.d("DatabaseHelper", "添加用户成功");
        return result != -1; // 返回true表示添加成功，false表示失败
    }
    // 更新用户
    public boolean updateUser(String id, String username, String password, String phonenumber, String sex, double balance, String updatedate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put("phonenumber", phonenumber);
        values.put("sex", sex);
        values.put("balance", balance);
        values.put("updatedate", updatedate);
        int rowsAffected = db.update(TABLE_NAME, values, "id = ?", new String[]{id});
        db.close();
        return rowsAffected > 0;
    }
    // 删除用户
    public boolean deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, "id = ?", new String[]{id});
        db.close();
        return rowsAffected > 0;
    }
    // 查询用户
    public boolean queryUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // 查询用户
    public boolean checkLogin(String phonenumber, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = "phonenumber = ? AND password = ?";
        String[] selectionArgs = {phonenumber, password};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "1";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
}
