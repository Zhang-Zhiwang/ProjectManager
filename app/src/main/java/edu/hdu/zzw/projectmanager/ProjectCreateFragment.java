package edu.hdu.zzw.projectmanager;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Manager manager;
    private Button create_btn, select_start_btn, select_end_btn;
    EditText project_name, description;
    TextView startTime, endTime;
    ProjectManagerDB projectManagerDB;
    SQLiteDatabase sqLWrite;

    public ProjectCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectCreateFragment newInstance(String param1, String param2) {
        ProjectCreateFragment fragment = new ProjectCreateFragment();
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
        view =  inflater.inflate(R.layout.fragment_project_create, container, false);
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");

        //获得数据库
        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqLWrite = projectManagerDB.getWritableDatabase();
        //绑定view
        initView(view);

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
                        startTime.setText(sdf.format(ca.getTime()));
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
                        endTime.setText(sdf.format(ca.getTime()));
                    }
                },ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        //创建项目
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectName = project_name.getText().toString();
                String start_time = startTime.getText().toString();
                String end_time = endTime.getText().toString();
                String _description = description.getText().toString();
                String recordTime = sdf.format(new Date(System.currentTimeMillis()));
                if(projectName.length() == 0 || start_time.length() == 0 || end_time.length() == 0)
                    Toast.makeText(getActivity(),"请输入项目名或选择时间",Toast.LENGTH_SHORT).show();
                else {
                    Project re = projectManagerDB.FindProjectByName(sqLWrite, projectName);
                    if(re != null) {
                        Toast.makeText(getActivity(),"该项目已存在，请重新输入项目名称",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Project project = new Project(projectName,manager.getId(),manager.getName(),_description,"","",start_time,end_time,recordTime);
                        projectManagerDB.add_project(sqLWrite, project);
                        //写入数据库获得id
                        Project project1 = projectManagerDB.FindProjectByName(sqLWrite, projectName);
                        //有了id才能创建record
                        Record record = new Record(project1.getId(),project1.getName(),recordTime,"",Record.ISPROJECT,Record.CREATED);
                        int id = projectManagerDB.add_record(sqLWrite, record);
                        Record record1 = projectManagerDB.FindRecordByID(sqLWrite, id);
                        //更新project
                        project1.getRecordList().add(record1.getId()+"");
                        projectManagerDB.update_project(sqLWrite, project1);
                        //获得project的id，然后更新manager
                        manager.getProjectList().add(project1.getId()+"");
                        projectManagerDB.update_manager(sqLWrite,manager);
                        Toast.makeText(getActivity(),"项目创建成功",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        return view;
    }

    void initView(View view) {
        project_name = (EditText)view.findViewById(R.id.project_name);
        description = (EditText)view.findViewById(R.id.description);
        startTime = (TextView)view.findViewById(R.id.starttime);
        endTime = (TextView)view.findViewById(R.id.endtime);
        select_start_btn = (Button)view.findViewById(R.id.select_start_time);
        select_end_btn = (Button)view.findViewById(R.id.select_end_time);
        create_btn = (Button)view.findViewById(R.id.create_project);
    }
}
