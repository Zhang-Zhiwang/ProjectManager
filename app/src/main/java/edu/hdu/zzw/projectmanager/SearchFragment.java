package edu.hdu.zzw.projectmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_read;
    private Manager manager;
    private List<Project> projectList, projectListOfManager;
    private List<Task> taskList, taskListOfManager;
    private List<String> manager_nameList;
    private ListView ProjectList, TaskList, ManagerNameList;
    private Button search_btn;
    private EditText search_edit;
    private View view;
    private OnProjectListItemClick onProjectListItemClick;
    private OnTaskListItemClick onTaskListItemClick;

    public SearchFragment() {
        // Required empty public constructor
    }


    //查看项目详情item点击回调
    public interface OnProjectListItemClick {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    public OnProjectListItemClick getOnProjectListItemClick() {return onProjectListItemClick;}
    public void setOnProjectListItemClick(OnProjectListItemClick onProjectListItemClick) {
        this.onProjectListItemClick = onProjectListItemClick;
    }

    //查看任务详情点击回调
    public interface OnTaskListItemClick {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    public OnTaskListItemClick getOnTaskListItemClick() {return onTaskListItemClick;}
    public void setOnTaskListItemClick(OnTaskListItemClick onTaskListItemClick) {
        this.onTaskListItemClick = onTaskListItemClick;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);

        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_read = projectManagerDB.getReadableDatabase();

        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                taskListOfManager = new ArrayList<Task>();
                projectListOfManager = new ArrayList<Project>();
                String str = search_edit.getText().toString();
                manager_nameList = projectManagerDB.FindManagerNameListByName(sqL_read,str);
                taskList = projectManagerDB.FindTaskListByName(sqL_read, str);
                projectList = projectManagerDB.FindProjectListByName(sqL_read, str);
                for(Project p : projectList) {
                    if(p.getManager_ID() == manager.getId()) projectListOfManager.add(p);
                }
                for(Task t : taskList) {
                    if(t.getManagerID() == manager.getId()) taskListOfManager.add(t);
                }
                ManagerNameList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,manager_nameList));
                TaskList.setAdapter(new TaskAdapter());

                TaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onTaskListItemClick != null) {
                            onTaskListItemClick.onItemClick(parent, view, position, id);
                        }
                    }
                });

                ProjectList.setAdapter(new ProjectAdapter());

                ProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(onProjectListItemClick != null) {
                            onProjectListItemClick.onItemClick(parent, view, position, id);
                        }
                    }
                });

                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
            }
        });
        return view;
    }

    void initView (View view) {
        search_edit = (EditText)view.findViewById(R.id.search_edit);
        search_btn = (Button)view.findViewById(R.id.search_btn);
        ManagerNameList = (ListView)view.findViewById(R.id.manager_list);
        ProjectList = (ListView)view.findViewById(R.id.project_list);
        TaskList = (ListView)view.findViewById(R.id.task_list);
    }

    class ProjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return projectListOfManager.size();
        }

        @Override
        public Object getItem(int position) {
            return projectListOfManager.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            view = View.inflate(getActivity(), R.layout.project_item, null);
            TextView project_name = (TextView) view.findViewById(R.id.project_name);
            TextView manager_name = (TextView) view.findViewById(R.id.manager_name);
            project_name.setText(projectListOfManager.get(position).getName());
            manager_name.setText(projectListOfManager.get(position).getManager_Name());
            return view;
        }
    }

    class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return taskListOfManager.size();
        }

        @Override
        public Object getItem(int position) {
            return taskListOfManager.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            view = View.inflate(getActivity(), R.layout.task_item,null);
            TextView taskName = (TextView)view.findViewById(R.id.task_name);
            TextView projectName = (TextView)view.findViewById(R.id.project_name);
            TextView managerName = (TextView)view.findViewById(R.id.manager_name);
            taskName.setText(taskListOfManager.get(position).getName());
            projectName.setText(taskListOfManager.get(position).getProjectName());
            managerName.setText(taskListOfManager.get(position).getManagerName());
            return view;
        }
    }
}
