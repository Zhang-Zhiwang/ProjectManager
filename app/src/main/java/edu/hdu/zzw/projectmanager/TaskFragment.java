package edu.hdu.zzw.projectmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Manager manager;
    private Task task;
    private TextView task_name, manager_name, start_time, end_time;
    private EditText description;
    private Button change_description, start, pause, resume, finish, cancel;
    private ListView RecordList;

    private List<String> recordList;
    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_write;


    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
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
        view = inflater.inflate(R.layout.fragment_task, container, false);
        initView(view);

        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_write = projectManagerDB.getWritableDatabase();
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");
        task = (Task) bundle.getSerializable("task");
        recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, task.getRecordList());
        Log.i("TF",task.RecordList_toString());
        Log.i("TF1",recordList.toString());

        task_name.setText(task.getName());
        manager_name.setText(task.getManagerName());
        description.setText(task.getDescription());
        start_time.setText(task.getStartTime());
        end_time.setText(task.getEndTime());

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
                                Record record = projectManagerDB.FindRecordByID(sqL_write, Integer.parseInt(task.getRecordList().get(position)));
                                if(des.equals(record.getDescription()))
                                    Toast.makeText(getActivity(),"备注相同，请勿重复输入",Toast.LENGTH_SHORT).show();
                                else {
                                    record.setDescription(des);
                                    projectManagerDB.update_record(sqL_write, record);
                                    recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, task.getRecordList());
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

        return view;
    }

    void initView(View view) {
        task_name = (TextView)view.findViewById(R.id.task_name);
        manager_name = (TextView)view.findViewById(R.id.manager_name);
        description = (EditText)view.findViewById(R.id.description);
        start_time = (TextView)view.findViewById(R.id.starttime);
        end_time = (TextView)view.findViewById(R.id.endtime);
        change_description = (Button)view.findViewById(R.id.change_description);
        start = (Button)view.findViewById(R.id.start);
        pause = (Button)view.findViewById(R.id.pause);
        resume = (Button)view.findViewById(R.id.resume);
        finish = (Button)view.findViewById(R.id.finish);
        cancel = (Button)view.findViewById(R.id.cancel);
        RecordList = (ListView)view.findViewById(R.id.recordlist);
    }

    @Override
    public void onClick(View v) {
        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_write = projectManagerDB.getWritableDatabase();
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");
        task = (Task) bundle.getSerializable("task");
        recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write, task.getRecordList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Record record;
        int id;
        switch (v.getId()) {
            case R.id.start:
                record = new Record(task.getId(),task.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISTASK,Record.START);
                id = projectManagerDB.add_record(sqL_write, record);
                task.getRecordList().add(id+"");
                projectManagerDB.update_task(sqL_write, task);
                task = projectManagerDB.FindTaskByID(sqL_write, task.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,task.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.pause:
                record = new Record(task.getId(),task.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISTASK,Record.PAUSE);
                id = projectManagerDB.add_record(sqL_write, record);
                task.getRecordList().add(id+"");
                projectManagerDB.update_task(sqL_write, task);
                task = projectManagerDB.FindTaskByID(sqL_write, task.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,task.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.resume:
                record = new Record(task.getId(),task.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISTASK,Record.DOING);
                id = projectManagerDB.add_record(sqL_write, record);
                task.getRecordList().add(id+"");
                projectManagerDB.update_task(sqL_write, task);
                task = projectManagerDB.FindTaskByID(sqL_write, task.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,task.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.finish:
                record = new Record(task.getId(),task.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISTASK,Record.FINISH);
                id = projectManagerDB.add_record(sqL_write, record);
                task.getRecordList().add(id+"");
                projectManagerDB.update_task(sqL_write, task);
                task = projectManagerDB.FindTaskByID(sqL_write, task.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,task.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.cancel:
                record = new Record(task.getId(),task.getName(),sdf.format(System.currentTimeMillis()),"",Record.ISTASK,Record.CANCEL);
                id = projectManagerDB.add_record(sqL_write, record);
                task.getRecordList().add(id+"");
                projectManagerDB.update_task(sqL_write, task);
                task = projectManagerDB.FindTaskByID(sqL_write, task.getId());
                recordList = projectManagerDB.FindRecordStringListByIdList(sqL_write,task.getRecordList());
                RecordList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,recordList));
                break;
            case R.id.change_description:
                String des = description.getText().toString();
                if(des.equals(task.getDescription()))
                    Toast.makeText(getActivity(),"描述内容一致，请勿重复修改",Toast.LENGTH_SHORT).show();
                else {
                    task.setDescription(des);
                    projectManagerDB.update_task(sqL_write, task);
                    Toast.makeText(getActivity(),"修改描述成功",Toast.LENGTH_SHORT).show();
                }
        }
    }

}
