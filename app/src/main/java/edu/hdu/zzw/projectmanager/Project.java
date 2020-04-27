package edu.hdu.zzw.projectmanager;

import java.util.Arrays;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private String Manager_Name;
    private int Manager_ID;
    private List<String> taskList;

    public Project() {}
    public Project(int id, String name,String description,String manager_Name,int manager_ID,String tasklist){
        this.id = id;
        this.name = name;
        this.description = description;
        this.Manager_ID = manager_ID;
        this.Manager_Name = manager_Name;
        String[] str = tasklist.split(",");
        this.taskList = Arrays.asList(str);
    }
}
