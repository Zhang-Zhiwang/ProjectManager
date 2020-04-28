package edu.hdu.zzw.projectmanager;

import androidx.annotation.IntDef;

import java.util.Date;

public class Record {
    private int id;
    private int host_id;
    private String host_name;
    private Date date;

    //替代enum
    public static final int ISPROJECT = 0;
    public static final int ISTASK = 1;
    @IntDef({ISPROJECT,ISTASK})
    @interface HostType{}
    private @HostType int hostType;

    public static final int CREATED = 0, DOING = 1, PAUSE = 2, FINISH = 3, CANCEL = 4;
    @IntDef({CREATED,DOING,PAUSE,FINISH,CANCEL})
    @interface State{}
    private @State int state;

    private String description;

    public Record() {}

    public Record(int id, Date date, int host_id, String host_name, @HostType int hostType, @State int state, String description) {
        this.id = id;
        this.date = date;
        this.host_id = host_id;
        this.host_name = host_name;
        this.hostType = hostType;
        this.state = state;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getHost_id() {
        return host_id;
    }

    public String getHost_name() {
        return host_name;
    }

    @HostType
    public int getHostType() {
        return hostType;
    }

    @State
    public int getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHost_id(int host_id) {
        this.host_id = host_id;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public void setHostType(@HostType int hostType) {
        this.hostType = hostType;
    }

    public void setState(@State int state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){
        String type = "",s = "";
        switch (hostType){
            case ISPROJECT: type = new String("项目");break;
            case ISTASK: type = new String("任务");break;
            default:break;
        }
        switch (state){
            case CREATED: s = new String("已创建");break;
            case DOING: s = new String("进行中");break;
            case PAUSE: s = new String("已暂停");break;
            case FINISH: s = new String("已完成");break;
            case CANCEL: s = new String("已取消");break;
            default:break;
        }
        String str = date.toString()+" :" + type + host_name + s + ",备注： " + description;
        return str;
    }
}

