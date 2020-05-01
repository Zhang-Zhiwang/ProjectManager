package edu.hdu.zzw.projectmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;//项目描述
    private String Manager_Name;
    private int Manager_ID;
    private String StartTime;
    private String EndTime;
    private String RecordTime;
    private List<String> RecordList;
    private List<String> TaskList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Project() {
        this.RecordList = new ArrayList<String>();
        this.TaskList = new ArrayList<String>();
    }
    public Project(int id, String name,String description,String manager_Name,int manager_ID,String tasklist){
        this.id = id;
        this.name = name;
        this.description = description;
        this.Manager_ID = manager_ID;
        this.Manager_Name = manager_Name;
        String[] str = tasklist.split(",");
        this.TaskList = Arrays.asList(str);
    }

    public Project(int id, String name, String description, String manager_Name, int manager_ID, String startTime, String endTime, String recordTime, String recordList, String taskList) {
        this.id = id;
        this.name = name;
        this.description = description;
        Manager_Name = manager_Name;
        Manager_ID = manager_ID;
        StartTime = startTime;
        EndTime = endTime;
        RecordTime = recordTime;
        String [] str1 = recordList.split(",");
        RecordList = Arrays.asList(str1);
        String [] str2 = taskList.split(",");
        TaskList = Arrays.asList(str2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String TaskList_toString(){
        String str = String.join(",", TaskList);
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String RecordList_toString() {
        String str = String.join(",",TaskList);
        return str;
    }

    //getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getManager_Name() {
        return Manager_Name;
    }

    public int getManager_ID() {
        return Manager_ID;
    }

    public List<String> getTaskList() {
        return TaskList;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getRecordTime() {
        return RecordTime;
    }

    public List<String> getRecordList() {
        return RecordList;
    }

    //setter
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManager_Name(String manager_Name) {
        Manager_Name = manager_Name;
    }

    public void setManager_ID(int manager_ID) {
        Manager_ID = manager_ID;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }

    public void setRecordList(String recordList) {
        String [] str = recordList.split(",");
        RecordList = Arrays.asList(str);
    }

    public void setTaskList(String tasklist) {
        String [] str = tasklist.split(",");
        this.TaskList = Arrays.asList(str);
    }

    public void setRecordList(List<String> recordList) {
        RecordList = recordList;
    }

    public void setTaskList(List<String> taskList) {
        TaskList = taskList;
    }

    public Date toDate(String date) throws ParseException {
        Date d = new Date();
        d = (Date)simpleDateFormat.parse(date);
        return d;
    }
}
