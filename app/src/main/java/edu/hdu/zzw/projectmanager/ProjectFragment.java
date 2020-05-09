package edu.hdu.zzw.projectmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Manager manager;
    private Project project;
    private TextView project_name, manager_name, start_time, end_time;
    private EditText description;
    private Button create_task, change_description, start, pause, resume, finish, cancel;
    private ListView TaskList, RecordList;

    private List<Task> taskList;
    private List<String> recordList;
    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_write;

    private OnButtonClick onButtonClick;
    private OnListItemClick onListItemClick;

    //创建项目按钮回调
    public interface OnButtonClick {
        public void onClick(View view) ;
    }
    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }
    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    //查看项目详情item点击回调
    public interface OnListItemClick {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    public OnListItemClick getOnListItemClick() {return onListItemClick;}
    public void setOnListItemClick(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }


    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_project, container, false);

        //绑定view
        initView();

        //获得数据库
        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_write = projectManagerDB.getWritableDatabase();
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");
        project = (Project) bundle.getSerializable("project");
        taskList = projectManagerDB.FindTaskListByIdList(sqL_write, project.getTaskList());
        recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());

        //为view设置数据
        project_name.setText(project.getName());
        manager_name.setText(manager.getName());
        start_time.setText(project.getStartTime());
        end_time.setText(project.getEndTime());
        description.setText(project.getDescription());

        //填充listview
        TaskList.setAdapter(new TaskAdapter());
        RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));

        //点击item修改record的描述
        RecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            EditText editText;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder((HomePageActivity)getActivity()).setTitle("系统提示").setMessage("请添加备注").setView(editText = new EditText((HomePageActivity)getActivity()))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String des = editText.getText().toString();
                                Record record = projectManagerDB.FindRecordByID(sqL_write, Integer.parseInt(project.getRecordList().get(position)));
                                if(des.equals(record.getDescription()))
                                    Toast.makeText(getActivity(),"备注相同，请勿重复输入",Toast.LENGTH_SHORT).show();
                                else {
                                    record.setDescription(des);
                                    projectManagerDB.update_record(sqL_write, record);
                                    recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                                    RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                                    Toast.makeText(getActivity(),"修改备注成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);
        finish.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //修改project的描述
        change_description.setOnClickListener(this);

        //创建任务回调
        create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onButtonClick != null) {
                    onButtonClick.onClick(create_task);
                }
            }
        });

        //任务详情回调
        TaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onListItemClick != null)
                    onListItemClick.onItemClick(parent,view,position,id);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        //initView();
        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_write = projectManagerDB.getWritableDatabase();
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");
        project = (Project) bundle.getSerializable("project");
        taskList = projectManagerDB.FindTaskListByIdList(sqL_write, project.getTaskList());
        recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Record record;
        int id;
        switch (v.getId()) {
            case R.id.start :
                record = new Record(project.getId(),project.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISPROJECT,Record.START);
                id = projectManagerDB.add_record(sqL_write, record);
                project.getRecordList().add(id+"");
                projectManagerDB.update_project(sqL_write, project);
                project = projectManagerDB.FindProjectByID(sqL_write, project.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.pause :
                record = new Record(project.getId(),project.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISPROJECT,Record.PAUSE);
                id = projectManagerDB.add_record(sqL_write, record);
                project.getRecordList().add(id+"");
                projectManagerDB.update_project(sqL_write, project);
                project = projectManagerDB.FindProjectByID(sqL_write, project.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.resume :
                record = new Record(project.getId(),project.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISPROJECT,Record.DOING);
                id = projectManagerDB.add_record(sqL_write, record);
                project.getRecordList().add(id+"");
                projectManagerDB.update_project(sqL_write, project);
                project = projectManagerDB.FindProjectByID(sqL_write, project.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.finish :
                record = new Record(project.getId(),project.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISPROJECT,Record.FINISH);
                id = projectManagerDB.add_record(sqL_write, record);
                project.getRecordList().add(id+"");
                projectManagerDB.update_project(sqL_write, project);
                project = projectManagerDB.FindProjectByID(sqL_write, project.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.cancel :
                record = new Record(project.getId(),project.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISPROJECT,Record.CANCEL);
                id = projectManagerDB.add_record(sqL_write, record);
                project.getRecordList().add(id+"");
                projectManagerDB.update_project(sqL_write, project);
                project = projectManagerDB.FindProjectByID(sqL_write, project.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.change_description :
                String des = description.getText().toString();
                if(des.equals(project.getDescription()))
                    Toast.makeText(getActivity(),"描述内容一致，请勿重复修改",Toast.LENGTH_SHORT).show();
                else {
                    project.setDescription(des);
                    projectManagerDB.update_project(sqL_write, project);
                    Toast.makeText(getActivity(),"修改描述成功",Toast.LENGTH_SHORT).show();
                }
        }
    }

    class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = View.inflate(getActivity(), R.layout.task_item,null);
            TextView taskName = (TextView)view.findViewById(R.id.task_name);
            TextView projectName = (TextView)view.findViewById(R.id.project_name);
            TextView managerName = (TextView)view.findViewById(R.id.manager_name);
            taskName.setText(taskList.get(position).getName());
            projectName.setText(taskList.get(position).getProjectName());
            managerName.setText(taskList.get(position).getManagerName());
            return view;
        }
    }

    private void initView() {
        project_name = (TextView)view.findViewById(R.id.project_name);
        manager_name = (TextView)view.findViewById(R.id.manager_name);
        start_time = (TextView)view.findViewById(R.id.starttime);
        end_time = (TextView)view.findViewById(R.id.endtime);
        description = (EditText)view.findViewById(R.id.description);
        create_task = (Button)view.findViewById(R.id.create_task);
        change_description = (Button)view.findViewById(R.id.change_description);
        start = (Button)view.findViewById(R.id.start);
        pause = (Button)view.findViewById(R.id.pause);
        resume = (Button)view.findViewById(R.id.resume);
        finish = (Button)view.findViewById(R.id.finish);
        cancel = (Button)view.findViewById(R.id.cancel);
        TaskList = (ListView)view.findViewById(R.id.tasklist);
        RecordList = (ListView)view.findViewById(R.id.recordlist);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {}
        else {
            /**切换到顶部，获得焦点，需要刷新数据*/
            manager = projectManagerDB.FindManagerByID(sqL_write,manager.getId());
            project = projectManagerDB.FindProjectByID(sqL_write,project.getId());
            taskList = projectManagerDB.FindTaskListByIdList(sqL_write,project.getTaskList());
            recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,project.getRecordList());
            TaskList.setAdapter(new TaskAdapter());
            RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
        }
    }
}
