package edu.hdu.zzw.projectmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

       initView();

       projectManagerDB = ProjectManagerDB.getInstance(getActivity());
       sqL_write = projectManagerDB.getWritableDatabase();
       Bundle bundle = getArguments();
       manager = (Manager) bundle.getSerializable("manager");
       project = (Project) bundle.getSerializable("project");
       taskList = projectManagerDB.FindTaskListByIdList(sqL_write, project.getTaskList());
       recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, project.getRecordList());

       project_name.setText(project.getName());
       manager_name.setText(manager.getName());
       start_time.setText(project.getStartTime());
       end_time.setText(project.getEndTime());
       description.setText(project.getDescription());
       TaskList.setAdapter(new TaskAdapter());
       RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));



       return view;
    }

    @Override
    public void onClick(View v) {

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
            projectName.setText(project.getName());
            managerName.setText(manager.getName());
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
}
