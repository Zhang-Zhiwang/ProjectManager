package edu.hdu.zzw.projectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ProjectManagerDB extends SQLiteOpenHelper {

    /**
     * 单例模式
     * 避免多线程出错
     * */
    private static ProjectManagerDB mInstance = null;

    public static ProjectManagerDB getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ProjectManagerDB(context);
        }
        return mInstance;
    }

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

    /**修复null值**/
    public String FixNull(String string) {
        if(string.equals("null")) return "";
        else return string;
    }

    /**增**/
    //向数据库添加一行manager数据，一般用于注册界面，使用前需要检测username是否重复
    public int add_manager(SQLiteDatabase sqLiteDatabase, Manager manager) {
        ContentValues values = new ContentValues();
        //values.put("id",manager.getId());
        values.put("username",manager.getName());
        values.put("password",manager.getPassword());
        values.put("projectlist",manager.ProjectList_toString());
        values.put("tasklist",manager.TaskList_toString());
        return (int) sqLiteDatabase.insert("manager",null,values);
        //sqLiteDatabase.close();
    }

    //向数据库添加一行project数据，一般用于单击button添加project，检测重复
    public int add_project(SQLiteDatabase sqLiteDatabase, Project project) {
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
        return (int) sqLiteDatabase.insert("project",null,values);
        //sqLiteDatabase.close();
    }

    //向数据库添加一行task数据，一般用于单击button添加task，检测重复
    public int add_task(SQLiteDatabase sqLiteDatabase, Task task) {
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
        return (int) sqLiteDatabase.insert("task",null,values);
        //sqLiteDatabase.close();
    }

    //自动获取时间，无name字段，无需检测
    public int add_record(SQLiteDatabase sqLiteDatabase, Record record) {
        ContentValues values = new ContentValues();
        values.put("host_id", record.getHost_id());
        values.put("host_name",record.getHost_name());
        values.put("date",record.getDate());
        values.put("description", record.getDescription());
        values.put("hosttype",record.getHostType());
        values.put("state",record.getState());
        return (int)sqLiteDatabase.insert("record",null,values);

        //sqLiteDatabase.close();
    }

    /**查**/
    //返回所有用户信息
    public List<Manager> ManagerList(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query("manager",null,null,null,null,null,"id ASC");
        if(cursor.getCount() > 0) {
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
                projectList = FixNull(cursor.getString(3));
                taskList = FixNull(cursor.getString(4));
                ManagerList.add(new Manager(id,name,password,projectList,taskList));
            }
            cursor.close();
            return ManagerList;
        }
        else {
            cursor.close();
            return new ArrayList<Manager>();
        }
    }

    //根据用户id返回单个用户信息
    public Manager FindManagerByID ( SQLiteDatabase sqLiteDatabase, int id) {
        Cursor cursor = sqLiteDatabase.query("manager",null,"id=?",new String[]{id+""},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            //int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            String projectList = FixNull(cursor.getString(3));
            String taskList = FixNull(cursor.getString(4));
            Manager manager = new Manager(id,name,password,projectList,taskList);
            Log.i("DBS",manager.TaskList_toString());
            cursor.close();
            return manager;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //根据用户名准确匹配，返回单个用户，用于检测重名以及匹配登录密码
    public Manager FindOneManagerByName (SQLiteDatabase sqLiteDatabase, String name) {
        Cursor cursor = sqLiteDatabase.query("manager",null,"username=?",new String[]{name},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            //String name = cursor.getString(1);
            String password = cursor.getString(2);
            String projectList = FixNull(cursor.getString(3));
            String taskList = FixNull(cursor.getString(4));
            Manager manager = new Manager(id,name,password,projectList,taskList);
            cursor.close();
            return manager;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //模糊匹配用户名，用于搜索用户
    public List<Manager> FindManagerListByName (SQLiteDatabase sqLiteDatabase, String name) {
        String sql = "select * from manager where username like '%" + name + "%'" + "order by id asc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if(cursor.getCount() > 0) {
            List<Manager> list = new ArrayList<Manager>();
            int id;
            String _name;
            String password;
            String projectList;
            String taskList;
            while(cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                _name = cursor.getString(1);
                password = cursor.getString(2);
                projectList = FixNull(cursor.getString(3));
                taskList = FixNull(cursor.getString(4));
                list.add(new Manager(id,_name,password,projectList,taskList));
            }
            cursor.close();
            return list;
        }
        else {
            cursor.close();
            return  new ArrayList<Manager>();
        }
    }

    //用getProjectList获得list，用where in从数据库取得数据
    public List<Project> FindProjectListByIdList (SQLiteDatabase sqLiteDatabase, List<String> projectList) {
        if(projectList.size() < 1) return new ArrayList<Project>();
        else{
            String [] arr = projectList.toArray(new String[projectList.size()]);
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for(int i = 1; i < arr.length; i++) {
                sb.append(",?");
            }
            String str = sb.toString();
            String sql = "select * from project where id in (" + str + ")";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,arr);
            if(cursor.getCount() > 0) {
                List<Project> list = new ArrayList<Project>();
                int id;
                String projectName;
                int manager_id;
                String manager_name;
                String description;
                String taskList;
                String recordList;
                String startTime;
                String endTime;
                String recordTime;
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    projectName = cursor.getString(1);
                    manager_id = cursor.getInt(2);
                    manager_name = cursor.getString(3);
                    description = FixNull(cursor.getString(4));
                    taskList = FixNull(cursor.getString(5));
                    recordList = FixNull(cursor.getString(6));
                    startTime = cursor.getString(7);
                    endTime = cursor.getString(8);
                    recordTime = cursor.getString(9);
                    list.add(new Project(id,projectName,manager_id,manager_name,description,taskList,recordList,startTime,endTime,recordTime));
                }
                cursor.close();
                return list;
            }
            else {
                cursor.close();
                return new ArrayList<Project>();
            }
        }

    }

    //使用Manager实体单例查询其所拥有的项目列表
    public List<Project> FindProjectListByManager (SQLiteDatabase sqLiteDatabase, Manager manager) {
        return FindProjectListByIdList(sqLiteDatabase, manager.getProjectList());
    }

    //ProjectName 模糊匹配
    public List<Project> FindProjectListByName (SQLiteDatabase sqLiteDatabase, String name) {
        String sql = "select * from project where projectname like '%" + name + "%'" + "order by id asc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if(cursor.getCount() > 0) {
            List<Project> list = new ArrayList<Project>();
            int id;
            String projectName;
            int manager_id;
            String manager_name;
            String description;
            String taskList;
            String recordList;
            String startTime;
            String endTime;
            String recordTime;
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                projectName = cursor.getString(1);
                manager_id = cursor.getInt(2);
                manager_name = cursor.getString(3);
                description = FixNull(cursor.getString(4));
                taskList = FixNull(cursor.getString(5));
                recordList = FixNull(cursor.getString(6));
                startTime = cursor.getString(7);
                endTime = cursor.getString(8);
                recordTime = cursor.getString(9);
                list.add(new Project(id,projectName,manager_id,manager_name,description,taskList,recordList,startTime,endTime,recordTime));
            }
            cursor.close();
            return list;
        }
        else {
            cursor.close();
            return new ArrayList<Project>();
        }
    }

    //ProjectName 准确匹配
    public Project FindProjectByName (SQLiteDatabase sqLiteDatabase, String name) {
        Cursor cursor = sqLiteDatabase.query("project",null,"projectname=?",new String[]{name},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            //String projectName;
            int manager_id = cursor.getInt(2);
            String manager_name = cursor.getString(3);
            String description = FixNull(cursor.getString(4));
            String taskList = FixNull(cursor.getString(5));
            String recordList = FixNull(cursor.getString(6));
            String startTime = cursor.getString(7);
            String endTime = cursor.getString(8);
            String recordTime = cursor.getString(9);
            Project project = new Project(id,name,manager_id,manager_name,description,taskList,recordList,startTime,endTime,recordTime);
            cursor.close();
            return project;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //ProjectId 准确匹配
    public Project FindProjectByID (SQLiteDatabase sqLiteDatabase, int id) {
        Cursor cursor = sqLiteDatabase.query("project",null,"id=?",new String[]{id+""},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            //int id = cursor.getInt(cursor.getColumnIndex("id"));
            String projectName = cursor.getString(1);
            int manager_id = cursor.getInt(2);
            String manager_name = cursor.getString(3);
            String description = FixNull(cursor.getString(4));
            String taskList = FixNull(cursor.getString(5));
            String recordList = FixNull(cursor.getString(6));
            String startTime = cursor.getString(7);
            String endTime = cursor.getString(8);
            String recordTime = cursor.getString(9);
            Project project = new Project(id,projectName,manager_id,manager_name,description,taskList,recordList,startTime,endTime,recordTime);
            cursor.close();
            return project;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //TaskList where in
    public List<Task> FindTaskListByIdList(SQLiteDatabase sqLiteDatabase, List<String> taskList) {
        if(taskList.size() < 1) return new ArrayList<Task>();
        else{
            String [] arr = taskList.toArray(new String[taskList.size()]);
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for(int i = 1; i < arr.length; i++) {
                sb.append(",?");
            }
            String str = sb.toString();
            String sql = "select * from task where id in (" + str + ")";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,arr);
            if(cursor.getCount() > 0) {
                List<Task> list = new ArrayList<Task>();
                int id;
                String taskName;
                int manager_id;
                String manager_name;
                int projectId;
                String projectName;
                String description;
                String recordList;
                String startTime;
                String endTime;
                String recordTime;
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    taskName = cursor.getString(1);
                    manager_id = cursor.getInt(2);
                    manager_name = cursor.getString(3);
                    projectId = cursor.getInt(4);
                    projectName = cursor.getString(5);
                    description = FixNull(cursor.getString(6));
                    recordList = FixNull(cursor.getString(7));
                    startTime = cursor.getString(8);
                    endTime = cursor.getString(9);
                    recordTime = cursor.getString(10);
                    list.add(new Task(id,taskName,manager_id,manager_name,projectId,projectName,description,recordList,startTime,endTime,recordTime));
                }
                cursor.close();
                return list;
            }
            else {
                cursor.close();
                return new ArrayList<Task>();
            }
        }
    }

    //使用Project实例查询其所拥有的任务列表
    public List<Task> FindTaskListByProject(SQLiteDatabase sqLiteDatabase, Project project) {
        return FindTaskListByIdList(sqLiteDatabase, project.getTaskList());
    }

    //使用Manager实例查询其所管理的任务
    public List<Task> FindTaskListByManager(SQLiteDatabase sqLiteDatabase, Manager manager) {
        return FindTaskListByIdList(sqLiteDatabase, manager.getTaskList());
    }

    //TaskName模糊匹配
    public List<Task> FindTaskListByName(SQLiteDatabase sqLiteDatabase, String name) {
        String sql = "select * from task where taskname like '%" + name + "%'" + "order by id asc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if(cursor.getCount() > 0) {
            List<Task> list = new ArrayList<Task>();
            int id;
            String taskName;
            int manager_id;
            String manager_name;
            int projectId;
            String projectName;
            String description;
            String recordList;
            String startTime;
            String endTime;
            String recordTime;
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                taskName = cursor.getString(1);
                manager_id = cursor.getInt(2);
                manager_name = cursor.getString(3);
                projectId = cursor.getInt(4);
                projectName = cursor.getString(5);
                description = FixNull(cursor.getString(6));
                recordList = FixNull(cursor.getString(7));
                startTime = cursor.getString(8);
                endTime = cursor.getString(9);
                recordTime = cursor.getString(10);
                list.add(new Task(id,taskName,manager_id,manager_name,projectId,projectName,description,recordList,startTime,endTime,recordTime));
            }
            cursor.close();
            return list;
        }
        else {
            cursor.close();
            return new ArrayList<Task>();
        }
    }

    //TaskName准确匹配
    public Task FindTaskByName(SQLiteDatabase sqLiteDatabase, String name) {
        Cursor cursor = sqLiteDatabase.query("task",null,"taskname=?",new String[]{name},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String taskName = cursor.getString(1);
            int manager_id = cursor.getInt(2);
            String manager_name = cursor.getString(3);
            int projectId = cursor.getInt(4);
            String projectName = cursor.getString(5);
            String description = FixNull(cursor.getString(6));
            String recordList = FixNull(cursor.getString(7));
            String startTime = cursor.getString(8);
            String endTime = cursor.getString(8);
            String recordTime = cursor.getString(10);
            Task task = new Task(id,taskName,manager_id,manager_name,projectId,projectName,description,recordList,startTime,endTime,recordTime);
            cursor.close();
            return task;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //TaskId准确匹配
    public Task FindTaskByID(SQLiteDatabase sqLiteDatabase, int id) {
        Cursor cursor = sqLiteDatabase.query("task",null,"id=?",new String[]{id+""},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            //int id = cursor.getInt(cursor.getColumnIndex("id"));
            String taskName = cursor.getString(1);
            int manager_id = cursor.getInt(2);
            String manager_name = cursor.getString(3);
            int projectId = cursor.getInt(4);
            String projectName = cursor.getString(5);
            String description = FixNull(cursor.getString(6));
            String recordList = FixNull(cursor.getString(7));
            String startTime = cursor.getString(8);
            String endTime = cursor.getString(8);
            String recordTime = cursor.getString(10);
            Task task = new Task(id,taskName,manager_id,manager_name,projectId,projectName,description,recordList,startTime,endTime,recordTime);
            cursor.close();
            return task;
        }
        else {
            cursor.close();
            return  null;
        }
    }

    //RecordList where in
    public List<Record> FindRecordListByIdList(SQLiteDatabase sqLiteDatabase, List<String> recordList) {
        if(recordList.size() < 1) return new ArrayList<Record>();
        else{
            String [] arr = recordList.toArray(new String[recordList.size()]);
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for(int i = 1; i < arr.length; i++) {
                sb.append(",?");
            }
            String str = sb.toString();
            String sql = "select * from record where id in (" + str + ")";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,arr);
            if(cursor.getCount() > 0) {
                List<Record> list = new ArrayList<Record>();
                int id;
                int host_id;
                String host_name;
                String date;
                String description;
                int hostType;
                int state;
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    host_id = cursor.getInt(1);
                    host_name = cursor.getString(2);
                    date = cursor.getString(3);
                    description = FixNull(cursor.getString(4));
                    hostType = cursor.getInt(5);
                    state = cursor.getInt(6);
                    list.add(new Record(id,host_id,host_name,date,description,hostType,state));
                }
                cursor.close();
                return list;
            }
            else {
                cursor.close();
                return new ArrayList<Record>();
            }
        }
    }

    public Record FindRecordByID(SQLiteDatabase sqLiteDatabase, int id) {
        Cursor cursor = sqLiteDatabase.query("record",null,"id=?",new String[]{id+""},null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int host_id = cursor.getInt(1);
            String host_name = cursor.getString(2);
            String date = cursor.getString(3);
            String description = cursor.getString(4);
            int host_type = cursor.getInt(5);
            int state = cursor.getInt(6);
            Record record = new Record(id,host_id,host_name,date,description,host_type,state);
            cursor.close();
            return record;
        }
        else {
            cursor.close();
            return null;
        }
    }

    //返回记录的文本
    public List<String> FindRecordStringListByIdList(SQLiteDatabase sqLiteDatabase, List<String> recordList) {
        if(recordList.size() < 1) return new ArrayList<String>();
        else{
            String [] arr = recordList.toArray(new String[recordList.size()]);
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for(int i = 1; i < arr.length; i++) {
                sb.append(",?");
            }
            String str = sb.toString();
            String sql = "select * from record where id in (" + str + ")";
            Cursor cursor = sqLiteDatabase.rawQuery(sql,arr);
            if(cursor.getCount() > 0) {
                List<String> list = new ArrayList<String>();
                int id;
                int host_id;
                String host_name;
                String date;
                String description;
                int hostType;
                int state;
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    host_id = cursor.getInt(1);
                    host_name = cursor.getString(2);
                    date = cursor.getString(3);
                    description = FixNull(cursor.getString(4));
                    hostType = cursor.getInt(5);
                    state = cursor.getInt(6);
                    list.add(new Record(id,host_id,host_name,date,description,hostType,state).toString());
                }
                cursor.close();
                return list;
            }
            else {
                cursor.close();
                return new ArrayList<String>();
            }
        }
    }

    /**删除**/
    //暂未开发删除功能，敬请期待
    public void delete(SQLiteDatabase sqLiteDatabase,int id){
        sqLiteDatabase.delete("manager","id=?",new String[]{id+""});
        sqLiteDatabase.close();

    }

    /**修改**/
    //暂时仅提供改名功能
    public void update(SQLiteDatabase sqLiteDatabase, String username, String password, int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        sqLiteDatabase.update("manager",contentValues,"id=?",new String[]{id+""});
        sqLiteDatabase.close();
    }

    /**今天新建project才想起来要去修改manager的list，errr，传入对象，用getter获取id，再进行修改*/
    public void update_manager(SQLiteDatabase sqLiteDatabase, Manager manager) {
        ContentValues values = new ContentValues();
        values.put("username",manager.getName());
        values.put("password",manager.getPassword());
        values.put("projectlist",manager.ProjectList_toString());
        values.put("tasklist",manager.TaskList_toString());
        sqLiteDatabase.update("manager",values,"id=?",new String[]{manager.getId()+""});
    }

    public void update_project(SQLiteDatabase sqLiteDatabase, Project project) {
        ContentValues values = new ContentValues();
        values.put("projectname",project.getName());
        values.put("manager_id",project.getManager_ID());
        values.put("manager_name",project.getManager_Name());
        values.put("description",project.getDescription());
        values.put("tasklist",project.TaskList_toString());
        values.put("recordlist",project.RecordList_toString());
        values.put("starttime",project.getStartTime());
        values.put("endtime",project.getEndTime());
        values.put("recordtime",project.getRecordTime());
        sqLiteDatabase.update("project",values,"id=?",new String[]{project.getId()+""});
    }

    public void update_task(SQLiteDatabase sqLiteDatabase, Task task) {
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
        sqLiteDatabase.update("task",values,"id=?",new String[] {task.getId()+""});
    }

    public void update_record(SQLiteDatabase sqLiteDatabase, Record record) {
        ContentValues values = new ContentValues();
        values.put("host_id",record.getHost_id());
        values.put("host_name",record.getHost_name());
        values.put("date",record.getDate());
        values.put("description",record.getDescription());
        values.put("hosttype",record.getHostType());
        values.put("state",record.getState());
        sqLiteDatabase.update("record",values,"id=?",new String[]{record.getId()+""});
    }

}
