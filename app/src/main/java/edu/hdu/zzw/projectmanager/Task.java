package edu.hdu.zzw.projectmanager;

import java.util.Date;
import java.util.List;

public class Task {
    private int id;
    private String name;
    private int ManagerID;
    private String ManagerName;
    private int ProjectID;
    private String ProjectName;
    private String description;
    private Date StartTime;
    private Date EndTime;
    private Date RecordTime;
    private List<String> RecordList;
}
