package edu.hdu.zzw.projectmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Manager manager;
    private Project[] projects;
    private ListView projectList;
    private Button button;
    private OnButtonClick onButtonClick;
    private OnListItemClick onListItemClick;
    private List<Project> list;
    //ProjectCreateFragment projectCreateFragment;

    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqLWrite;

    public ProjectListFragment() {
        // Required empty public constructor
    }

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

    public interface OnListItemClick {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    public OnListItemClick getOnListItemClick() {return onListItemClick;}
    public void setOnListItemClick(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
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
    public static ProjectListFragment newInstance(String param1, String param2) {
        ProjectListFragment fragment = new ProjectListFragment();
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
        view = inflater.inflate(R.layout.fragment_project_list, container, false);
        Bundle bundle = getArguments();
        manager = (Manager) bundle.getSerializable("manager");
        //manager = projectManagerDB.FindManagerByID(sqLWrite,manager.getId());
        projectList = (ListView) view.findViewById(R.id.project_list);
        projectManagerDB = ProjectManagerDB.getInstance(getActivity());
        sqLWrite = projectManagerDB.getWritableDatabase();
        list = projectManagerDB.FindProjectListByIdList(sqLWrite,manager.getProjectList());
        projects = list.toArray(new Project[list.size()]);
        projectList.setAdapter(new MyAdapter());
        button = view.findViewById(R.id.create_project);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onButtonClick != null) {
                    onButtonClick.onClick(button);
                }
            }
        });

        projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onListItemClick != null) {
                    onListItemClick.onItemClick(parent,view,position,id);
                }

            }
        });
        return view;
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {return projects.length;}
        public Object getItem(int i) {return projects[i];}
        public long getItemId(int i) {return i;}
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(getActivity(), R.layout.project_item, null);
            TextView project_name = (TextView) view.findViewById(R.id.project_name);
            TextView manager_name = (TextView) view.findViewById(R.id.manager_name);
            project_name.setText(projects[i].getName());
            manager_name.setText(projects[i].getManager_Name());
            return view;
        }
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {}
        else {
            /**切换到顶部，获得焦点，需要刷新数据*/
            manager = projectManagerDB.FindManagerByID(sqLWrite, manager.getId());
            list = projectManagerDB.FindProjectListByIdList(sqLWrite, manager.getProjectList());
            projects = list.toArray(new Project[list.size()]);
            projectList.setAdapter(new MyAdapter());
        }
    }
}
