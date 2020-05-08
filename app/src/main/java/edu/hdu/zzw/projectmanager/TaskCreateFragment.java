package edu.hdu.zzw.projectmanager;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.egl.EGLDisplay;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_write;
    private Button select_start_btn, select_end_btn, create_btn;
    private EditText taskName, description;
    private TextView start_time, end_time;

    private Manager manager;
    private Project project;

    public TaskCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskCreateFragment newInstance(String param1, String param2) {
        TaskCreateFragment fragment = new TaskCreateFragment();
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
        view = inflater.inflate(R.layout.fragment_task_create, container, false);
        initView(view);

        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_write = projectManagerDB.getWritableDatabase();

        Bundle bundle = getArguments();
        manager = (Manager)bundle.getSerializable("manager");
        project = (Project)bundle.getSerializable("project");

        final Calendar ca = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //选择开始时间
        select_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ca.set(Calendar.YEAR,year);
                        ca.set(Calendar.MONTH,month);
                        ca.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        start_time.setText(sdf.format(ca.getTime()));
                    }
                },ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

        //选择结束时间
        select_end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ca.set(Calendar.YEAR,year);
                        ca.set(Calendar.MONTH,month);
                        ca.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        end_time.setText(sdf.format(ca.getTime()));
                    }
                },ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        //创建任务
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task_name = taskName.getText().toString();
                String _description = description.getText().toString();
                String startTime = start_time.getText().toString();
                String endTime = end_time.getText().toString();
                String recordTime = sdf.format(new Date(System.currentTimeMillis()));
                if(task_name.length() == 0 || startTime.length() == 0 || endTime.length() == 0)
                    Toast.makeText(getActivity(),"请输入任务名或选择时间", Toast.LENGTH_SHORT).show();
                else {
                    Task re = projectManagerDB.FindTaskByName(sqL_write, task_name);
                    if(re != null)
                        Toast.makeText(getActivity(),"该任务已存在，请重新输入任务名称",Toast.LENGTH_SHORT).show();
                    else {
                        Task task = new Task(task_name,manager.getId(),manager.getName(),project.getId(),project.getName(),_description,"",startTime,endTime,recordTime);
                        projectManagerDB.add_task(sqL_write, task);
                        Task task1 = projectManagerDB.FindTaskByName(sqL_write, task_name);

                        Record record = new Record(task1.getId(),task1.getName(),recordTime,"",Record.ISTASK,Record.CREATED);
                        int id = projectManagerDB.add_record(sqL_write,record);
                        Record record1 = projectManagerDB.FindRecordByID(sqL_write, id);
                        //更新task
                        task1.getRecordList().add(record1.getId()+"");
                        projectManagerDB.update_task(sqL_write, task1);
                        //更新project和manager
                        project.getTaskList().add(task1.getId()+"");
                        projectManagerDB.update_project(sqL_write,project);
                        manager.getTaskList().add(task1.getId()+"");
                        projectManagerDB.update_manager(sqL_write,manager);
                        Toast.makeText(getActivity(),"任务创建成功",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    void initView(View view) {
        taskName = (EditText)view.findViewById(R.id.task_name);
        description = (EditText)view.findViewById(R.id.description);
        start_time = (TextView)view.findViewById(R.id.starttime);
        end_time = (TextView)view.findViewById(R.id.endtime);
        select_start_btn = (Button)view.findViewById(R.id.select_start_time);
        select_end_btn = (Button)view.findViewById(R.id.select_end_time);
        create_btn = (Button)view.findViewById(R.id.create_task);
    }
}
