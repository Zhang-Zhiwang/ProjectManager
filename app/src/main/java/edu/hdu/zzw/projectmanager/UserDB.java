package edu.hdu.zzw.projectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {
    public UserDB(Context context){super(context,"user_db",null,1);}

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String sql = "create table user(id integer primary key autoincrement, username varchar(20), password varchar(20))";
        sqLiteDatabase.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){}

    public void adddata(SQLiteDatabase sqLiteDatabase, String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        sqLiteDatabase.insert("user",null,contentValues);
        sqLiteDatabase.close();
    }

    public void delete(SQLiteDatabase sqLiteDatabase,int id){
        sqLiteDatabase.delete("user","id=?",new String[]{id+""});
        sqLiteDatabase.close();
    }

    public void update(SQLiteDatabase sqLiteDatabase, String username, String password, int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        sqLiteDatabase.update("user",contentValues,"id=?",new String[]{id+""});
        sqLiteDatabase.close();
    }


}
