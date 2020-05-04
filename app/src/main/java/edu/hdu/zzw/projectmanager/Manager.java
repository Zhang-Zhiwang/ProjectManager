package edu.hdu.zzw.projectmanager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**使用Serializable接口可以直接使用intent传递对象*/
public class Manager implements Serializable {
    private int id;
    private String name;
    private String password;
    private List<String> ProjectList;
    private List<String> TaskList;

    public Manager() {
        ProjectList = new ArrayList<String>();
        TaskList = new ArrayList<String>();
    }
    /**
     * 为什么要判断两个list字符串为空字符串？
     * 在FixNull函数中已经解决了空值从sqLite中取出变为“null”的问题，但是我们将结果转换成了“”，也就是空字符串
     * 空字符串在split和asList函数中存在一定的问题
     * 就是当这个空字符串在首个的时候，会占据list和字符串数组的第一个元素
     * 也就是会出现“，1，2，3”这样的情况，此时的list会变成[，1，2，3]
     * 而且split不会自动去除首部的分隔符
     * 所以就需要在构造和set的时候判断list字符串是否为空
     * 那么在注册界面实例化Manager的时候，既可以用无参构造器，也可以用带参构造器，只需要将两个list字符串置为空（非null）即可
     * **/
    public Manager(int id, String name, String password, String projectList, String taskList) {
        this.id = id;
        this.name = name;
        this.password = password;
        if(projectList.equals("")) ProjectList = new ArrayList<String>();
        else {
            String[] p = projectList.split(",");
            this.ProjectList = new ArrayList<String>(Arrays.asList(p));
        }
        if(taskList.equals("")) TaskList = new ArrayList<String>();
        else {
            String[] t = taskList.split(",");
            this.TaskList = new ArrayList<String>(Arrays.asList(t));
        }
    }

    public Manager( String name, String password, String projectList, String taskList) {
        //this.id = id;
        this.name = name;
        this.password = password;
        if(projectList.equals("")) ProjectList = new ArrayList<String>();
        else {
            String[] p = projectList.split(",");
            this.ProjectList = new ArrayList<String>(Arrays.asList(p));
        }
        if(taskList.equals("")) TaskList = new ArrayList<String>();
        else {
            String[] t = taskList.split(",");
            this.TaskList = new ArrayList<String>(Arrays.asList(t));
        }
    }

    //getter
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

    //setter
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
        if(projectList.equals("")) ProjectList = new ArrayList<String>();
        else{
            String[] p = projectList.split(",");
            this.ProjectList = new ArrayList<String> (Arrays.asList(p));
        }
    }

    public void setTaskList(String taskList) {
        if(taskList.equals("")) TaskList = new ArrayList<String>();
        else{
            String[] t = taskList.split(",");
            this.TaskList = new ArrayList<String>(Arrays.asList(t));
        }
    }

    public void setProjectList(List<String> projectList) {
        ProjectList = projectList;
    }

    public void setTaskList(List<String> taskList) {
        TaskList = taskList;
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
