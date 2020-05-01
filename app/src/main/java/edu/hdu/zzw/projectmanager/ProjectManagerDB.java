package edu.hdu.zzw.projectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ProjectManagerDB extends SQLiteOpenHelper {
    public ProjectManagerDB(Context context){super(context,"user_db",null,1);}

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //String sql = "create table manager(id integer primary key autoincrement, username varchar(20), password varchar(20))";
        sqLiteDatabase.execSQL("create table manager(id integer primary key autoincrement, " +
                "username varchar(20), " +
                "password varchar(20), " +
                "projectlist varchar(4000), " +
                "tasklist varchar(4000))");
        sqLiteDatabase.execSQL("create table project(id integer primary key autoincrement," +
                "projectname nvarchar(40), " +
                "manager_id integer, " +
                "manager_name varchar(20), " +
                "description nvarchar(4000), " +
                "tasklist varchar(4000), " +
                "recordlist varchar(4000), " +
                "starttime DATE," +
                "endtime DATE," +
                "recordtime DATE)");
        sqLiteDatabase.execSQL("create table task(id integer primary key autoincrement," +
                "taskname nvarchar(40)," +
                "manager_id integer," +
                "manager_name varchar(20)," +
                "project_id integer," +
                "project_name nvarchar(40)," +
                "description nvarchar(4000)," +
                "recordlist varchar(4000)," +
                "starttime DATE," +
                "endtime DATE," +
                "recordtime DATE)");
        sqLiteDatabase.execSQL("create table record(id integer primary key autoincrement," +
                "host_id integer," +
                "host_name nvarchar(40)," +
                "date DATE," +
                "description nvarchar(4000)," +
                "hosttype integer," +
                "state integer)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){}

    public void adddata(SQLiteDatabase sqLiteDatabase, String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        sqLiteDatabase.insert("manager",null,contentValues);
        //sqLiteDatabase.close();
    }

    /**增**/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_manager(SQLiteDatabase sqLiteDatabase, Manager manager) {
        ContentValues values = new ContentValues();
        //values.put("id",manager.getId());
        values.put("username",manager.getName());
        values.put("password",manager.getPassword());
        values.put("projectlist",manager.ProjectList_toString());
        values.put("tasklist",manager.TaskList_toString());
        sqLiteDatabase.insert("manager",null,values);
        //sqLiteDatabase.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_project(SQLiteDatabase sqLiteDatabase, Project project) {
        ContentValues values = new ContentValues();
        //values.put("id",project.getId());
        values.put("projectname",project.getName());
        values.put("manager_id",project.getManager_ID());
        values.put("manager_name",project.getManager_Name());
        values.put("description",project.getDescription());
        values.put("tasklist",project.TaskList_toString());
        values.put("recordlist",project.RecordList_toString());
        values.put("starttime",project.getStartTime());
        values.put("endtime",project.getEndTime());
        values.put("recordtime",project.getRecordTime());
        sqLiteDatabase.insert("project",null,values);
        //sqLiteDatabase.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void add_task(SQLiteDatabase sqLiteDatabase, Task task) {
        ContentValues values = new ContentValues();
        values.put("taskname",task.getName());
        values.put("manager_id",task.getManagerID());
        values.put("manager_name",task.getManagerName());
        values.put("project_id",task.getProjectID());
        values.put("project_name",task.getProjectName());
        values.put("description", task.getDescription());
        values.put("recordlist",task.RecordList_toString());
        values.put("starttime",task.getStartTime());
        values.put("endtime",task.getEndTime());
        values.put("recordtime",task.getRecordTime());
        sqLiteDatabase.insert("task",null,values);
        //sqLiteDatabase.close();
    }

    public void add_record(SQLiteDatabase sqLiteDatabase, Record record) {
        ContentValues values = new ContentValues();
        values.put("host_id", record.getHost_id());
        values.put("host_name",record.getHost_name());
        values.put("date",record.getDate());
        values.put("description", record.getDescription());
        values.put("hosttype",record.getHostType());
        values.put("state",record.getState());
        sqLiteDatabase.insert("record",null,values);
        //sqLiteDatabase.close();
    }

    /**查**/
    public List<Manager> ManagerList(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query("manager",null,null,null,null,null,"id ASC");
        List<Manager> ManagerList = new ArrayList<Manager>();
        int id;
        String name;
        String password;
        String projectList;
        String taskList;
        //cursor初始位置是在-1,而数据是从0开始的
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            name = cursor.getString(1);
            password = cursor.getString(2);
            projectList = cursor.getString(3);
            taskList = cursor.getColumnName(4);
            ManagerList.add(new Manager(id,name,password,projectList,taskList));
        }
        cursor.close();
        return ManagerList;
    }



    public void delete(SQLiteDatabase sqLiteDatabase,int id){
        sqLiteDatabase.delete("manager","id=?",new String[]{id+""});
        sqLiteDatabase.close();

    }

    public void update(SQLiteDatabase sqLiteDatabase, String username, String password, int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        sqLiteDatabase.update("manager",contentValues,"id=?",new String[]{id+""});
        sqLiteDatabase.close();
    }


}
