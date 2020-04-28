package edu.hdu.zzw.projectmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

public class Manager {
    private int id;
    private String name;
    private String password;
    private List<String> ProjectList;
    private List<String> TaskList;

    public Manager() {}
    public Manager(int id, String name, String password, String projectList, String taskList) {
        this.id = id;
        this.name = name;
        this.password = password;
        String[] p = projectList.split(",");
        this.ProjectList = Arrays.asList(p);
        String[] t = taskList.split(",");
        this.TaskList = Arrays.asList(t);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getProjectList() {
        return ProjectList;
    }

    public List<String> getTaskList() {
        return TaskList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProjectList(String projectList) {
        String[] p = projectList.split(",");
        this.ProjectList = Arrays.asList(p);
    }

    public void setTaskList(String taskList) {
        String[] t = taskList.split(",");
        this.TaskList = Arrays.asList(t);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String ProjectList_toString() {
        String str = String.join(",",ProjectList);
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String TaskList_toString() {
        String str = String.join(",",TaskList);
        return str;
    }
}
