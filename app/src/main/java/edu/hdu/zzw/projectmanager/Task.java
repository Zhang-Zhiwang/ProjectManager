package edu.hdu.zzw.projectmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Task implements Serializable {
    private int id;
    private String name;
    private int ManagerID;//负责人
    private String ManagerName;
    private int ProjectID;
    private String ProjectName;//任务所属项目
    private String description;
    private String StartTime;
    private String EndTime;
    private String RecordTime;
    private List<String> RecordList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public Task(int id, String name, int managerID, String managerName, int projectID, String projectName, String description, String recordList, String startTime, String endTime, String recordTime) {
        this.id = id;
        this.name = name;
        ManagerID = managerID;
        ManagerName = managerName;
        ProjectID = projectID;
        ProjectName = projectName;
        this.description = description;
        StartTime = startTime;
        EndTime = endTime;
        RecordTime = recordTime;
        if(recordList.equals("")) RecordList = new ArrayList<String>();
        else{
            String [] str = recordList.split(",");
            RecordList = new ArrayList<String> (Arrays.asList(str));
        }
    }

    public Task() {
        this.RecordList = new ArrayList<String>();
    }

    //getter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManagerID() {
        return ManagerID;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public int getProjectID() {
        return ProjectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getDescription() {
        return description;
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

    public void setManagerID(int managerID) {
        ManagerID = managerID;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if(recordList.equals("")) RecordList = new ArrayList<String>();
        else{
            String[] str = recordList.split(",");
            RecordList = new ArrayList<String>(Arrays.asList(str));
        }
    }

    public void setRecordList(List<String> recordList) {
        RecordList = recordList;
    }

    //将list转成String，用于存入sqlite
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String RecordList_toString() {
        String str = String.join(",",RecordList);
        return str;
    }

    public Date toDate(String date) throws ParseException {
        Date d = new Date();
        d = (Date)simpleDateFormat.parse(date);
        return d;
    }
}
