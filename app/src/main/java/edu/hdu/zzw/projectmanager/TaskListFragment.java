package edu.hdu.zzw.projectmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Manager manager;
    private ListView TaskList;
    private List<Task> taskList;

    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_read;

    private OnListItemClick onListItemClick;

    //查看任务详情item点击回调
    public interface OnListItemClick {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    public OnListItemClick getOnListItemClick() {return onListItemClick;}
    public void setOnListItemClick(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }

    public TaskListFragment() {
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
    public static TaskListFragment newInstance(String param1, String param2) {
        TaskListFragment fragment = new TaskListFragment();
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
        view = inflater.inflate(R.layout.fragment_task_list, container, false);
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");

        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqL_read = projectManagerDB.getReadableDatabase();

        taskList = projectManagerDB.FindTaskListByIdList(sqL_read, manager.getTaskList());
        TaskList = (ListView)view.findViewById(R.id.task_list);

        TaskList.setAdapter(new TaskAdapter());
        TaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onListItemClick != null) {
                    onListItemClick.onItemClick(parent,view,position,id);
                }
            }
        });
        return view;
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
            managerName.setText(manager.getName());
            return view;
        }
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {}
        else {
            /**切换到顶部，获得焦点，需要刷新数据*/
            manager = projectManagerDB.FindManagerByID(sqL_read,manager.getId());
            taskList = projectManagerDB.FindTaskListByIdList(sqL_read,manager.getTaskList());
            TaskList.setAdapter(new TaskAdapter());
        }
    }
}
