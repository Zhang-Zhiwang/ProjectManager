package edu.hdu.zzw.projectmanager;

import androidx.annotation.IntDef;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Serializable {
    private int id;
    private int host_id;
    private String host_name;
    private String date;
    //Android的SqLite中的数据是弱类型存储的。以String取出，再转化成日期类型的
    //数据库以Date存储，Bean以String存储，输入String sqLite可以转换为Date，输出时又是String，如果需要比较大小，则再将String转成Date
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //替代enum
    public static final int ISPROJECT = 0;
    public static final int ISTASK = 1;
    @IntDef({ISPROJECT,ISTASK})
    @interface HostType{}
    private @HostType int hostType;

    public static final int CREATED = 0, START = 1, DOING = 2, PAUSE = 3, FINISH = 4, CANCEL = 5;
    @IntDef({CREATED,START,DOING,PAUSE,FINISH,CANCEL})
    @interface State{}
    private @State int state;

    private String description;

    public Record() {}

    public Record(int id, int host_id, String host_name, String date, String description, @HostType int hostType, @State int state) {
        this.id = id;
        this.date = date;
        this.host_id = host_id;
        this.host_name = host_name;
        this.hostType = hostType;
        this.state = state;
        this.description = description;
    }

    public Record( int host_id, String host_name, String date, String description, @HostType int hostType, @State int state) {
        //this.id = id;
        this.date = date;
        this.host_id = host_id;
        this.host_name = host_name;
        this.hostType = hostType;
        this.state = state;
        this.description = description;
    }


    //getter
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

    public String getDate() {
        return date;
    }

    //setter
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

    public void setDate(String date) {
        this.date = date;
    }

    //单击任务/项目列表，观察其中记录，其listview所用函数toString
    public String toString() {
        String type = "",s = "";
        switch (hostType){
            case ISPROJECT: type = new String("项目");break;
            case ISTASK: type = new String("任务");break;
            default:break;
        }
        switch (state){
            case CREATED: s = new String("已创建");break;
            case START: s = new String("已开始");break;
            case DOING: s = new String("进行中");break;
            case PAUSE: s = new String("已暂停");break;
            case FINISH: s = new String("已完成");break;
            case CANCEL: s = new String("已取消");break;
            default:break;
        }
        String str = date.toString()+" :" + type + host_name + s + ",备注： " + description;
        return str;
    }

    public Date toDate() throws ParseException {
        Date d = new Date();
        d = (Date)simpleDateFormat.parse(this.date);
        return d;
    }
}

