package edu.hdu.zzw.projectmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private String Manager_Name;
    private int Manager_ID;
    private List<String> TaskList;

    public Project() {}
    public Project(int id, String name,String description,String manager_Name,int manager_ID,String tasklist){
        this.id = id;
        this.name = name;
        this.description = description;
        this.Manager_ID = manager_ID;
        this.Manager_Name = manager_Name;
        String[] str = tasklist.split(",");
        this.TaskList = Arrays.asList(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String TaskList_toString(){
        String str = String.join(",", TaskList);
        return str;
    }

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

    public List<String> getTaskList() {
        return TaskList;
    }

    public void setTaskList(String tasklist) {
        String [] str = tasklist.split(",");
        this.TaskList = Arrays.asList(str);
    }
}
