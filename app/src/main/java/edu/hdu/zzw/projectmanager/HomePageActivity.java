package edu.hdu.zzw.projectmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//import android.app.FragmentManager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

public class HomePageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup menuBar;
    private RadioButton menuBtn;

    private ProjectListFragment pl;
    private TaskListFragment tl;
    private SearchFragment se;
    private ProjectCreateFragment pc;
    private TaskCreateFragment tc;
    private ProjectFragment p;
    private TaskFragment t;
    private FragmentManager fragmentManager;
    private ProjectManagerDB projectManagerDB;
    private SQLiteDatabase sqL_read;

    private Manager manager;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //获得数据库
        projectManagerDB = ProjectManagerDB.getInstance(this);
        sqL_read = projectManagerDB.getReadableDatabase();

        //获得从LoginActivity传递的manager
        String ManagerJson = getIntent().getStringExtra("manager");
        manager = new Gson().fromJson(ManagerJson,Manager.class);
        //Intent intent = getIntent();
        //manager = (Manager) intent.getSerializableExtra("manager");

        fragmentManager = getSupportFragmentManager();

        menuBar = (RadioGroup)findViewById(R.id.menu_bar);
        menuBar.setOnCheckedChangeListener(this);

        //创建三个fragment实例，否则监听会报错
        menuBtn = (RadioButton)findViewById(R.id.task_btn);
        menuBtn.setChecked(true);
        menuBtn = (RadioButton)findViewById(R.id.search);
        menuBtn.setChecked(true);
        menuBar.clearCheck();
        menuBtn = (RadioButton)findViewById(R.id.project_btn);
        menuBtn.setChecked(true);

        //项目列表回调按钮监听，实现切换fragment
        pl.setOnButtonClick(new ProjectListFragment.OnButtonClick() {
            @Override
            public void onClick(View view) {
                menuBar.clearCheck();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                Bundle bundle = new Bundle();
                manager = projectManagerDB.FindManagerByID(sqL_read, manager.getId());
                bundle.putSerializable("manager",manager);
                if(pc == null) {
                    pc = new ProjectCreateFragment();
                    pc.setArguments(bundle);
                    fragmentTransaction.add(R.id.fg_content,pc);
                }
                else {
                    fragmentTransaction.show(pc);
                }
                fragmentTransaction.commit();
            }
        });

        //项目列表实现item点击监听的回调，实现查看项目详情的fragment切换
        pl.setOnListItemClick(new ProjectListFragment.OnListItemClick() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectListFragment.MyAdapter myAdapter = (ProjectListFragment.MyAdapter) parent.getAdapter();
                project = (Project) myAdapter.getItem(position);
                menuBar.clearCheck();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                Bundle bundle = new Bundle();
                manager = projectManagerDB.FindManagerByID(sqL_read, manager.getId());
                bundle.putSerializable("manager",manager);
                bundle.putSerializable("project",project);
                //每一个listitem都要不同的fragment，每次都需要new
                p = new ProjectFragment();
                p.setArguments(bundle);

                //因为p是内部类中赋值的对象，到外部会丢失引用（大概），外部类（HomePageActivity）无法引用内部类的赋值，所以嵌套回调
                p.setOnButtonClick(new ProjectFragment.OnButtonClick() {
                    @Override
                    public void onClick(View view) {
                        menuBar.clearCheck();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        hideAllFragment(fragmentTransaction);
                        Bundle bundle = new Bundle();
                        manager = projectManagerDB.FindManagerByID(sqL_read, manager.getId());
                        project = projectManagerDB.FindProjectByID(sqL_read, project.getId());
                        bundle.putSerializable("manager",manager);
                        bundle.putSerializable("project",project);
                        if(tc == null) {
                            tc = new TaskCreateFragment();
                            tc.setArguments(bundle);
                            fragmentTransaction.add(R.id.fg_content,tc);
                        }
                        else {
                            fragmentTransaction.show(tc);
                        }
                        fragmentTransaction.commit();
                    }
                });

                p.setOnListItemClick(new ProjectFragment.OnListItemClick() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ProjectFragment.TaskAdapter taskAdapter = (ProjectFragment.TaskAdapter) parent.getAdapter();
                        Task task = (Task) taskAdapter.getItem(position);
                        menuBar.clearCheck();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        hideAllFragment(fragmentTransaction);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("manager",manager);
                        bundle.putSerializable("task",task);

                        t = new TaskFragment();
                        t.setArguments(bundle);
                        fragmentTransaction.add(R.id.fg_content, t);
                        fragmentTransaction.commit();
                    }
                });

                fragmentTransaction.add(R.id.fg_content,p);
                fragmentTransaction.commit();
            }
        });

        tl.setOnListItemClick(new TaskListFragment.OnListItemClick() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskListFragment.TaskAdapter taskAdapter = (TaskListFragment.TaskAdapter) parent.getAdapter();
                Task task = (Task) taskAdapter.getItem(position);
                menuBar.clearCheck();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                Bundle bundle = new Bundle();
                bundle.putSerializable("manager",manager);
                bundle.putSerializable("task",task);

                t = new TaskFragment();
                t.setArguments(bundle);
                fragmentTransaction.add(R.id.fg_content, t);
                fragmentTransaction.commit();
            }
        });

        //项目详情页回调创建任务按钮
        /*if(p == null) p = new ProjectFragment();
        p.setOnButtonClick(new ProjectFragment.OnButtonClick() {
            @Override
            public void onClick(View view) {
                menuBar.clearCheck();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction);
                Bundle bundle = new Bundle();
                manager = projectManagerDB.FindManagerByID(sqL_read, manager.getId());
                project = projectManagerDB.FindProjectByID(sqL_read, project.getId());
                bundle.putSerializable("manager",manager);
                bundle.putSerializable("project",project);
                if(tc == null) {
                    tc = new TaskCreateFragment();
                    tc.setArguments(bundle);
                    fragmentTransaction.add(R.id.fg_content,tc);
                }
                else {
                    fragmentTransaction.show(tc);
                }
                fragmentTransaction.commit();
            }
        });*/
    }



    //底部导航栏切换fragment
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        Bundle bundle = new Bundle();

        String ManagerJson = getIntent().getStringExtra("manager");
        manager = new Gson().fromJson(ManagerJson,Manager.class);
        bundle.putSerializable("manager",manager);

        switch (checkedId) {
            case R.id.project_btn:
                if(pl == null) {
                    pl = new ProjectListFragment();
                    pl.setArguments(bundle);
                    fragmentTransaction.add(R.id.fg_content,pl);
                }
                else
                    fragmentTransaction.show(pl);
                break;
            case R.id.task_btn:
                if(tl == null) {
                    tl = new TaskListFragment();
                    tl.setArguments(bundle);
                    fragmentTransaction.add(R.id.fg_content,tl);
                }
                else
                    fragmentTransaction.show(tl);
                break;
            case R.id.search:
                if(se == null) {
                    se = new SearchFragment();
                    se.setArguments(bundle);
                    fragmentTransaction.add(R.id.fg_content,se);
                }
                else
                    fragmentTransaction.show(se);
                break;
        }
        fragmentTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(pl != null)fragmentTransaction.hide(pl);
        if(tl != null)fragmentTransaction.hide(tl);
        if(se != null)fragmentTransaction.hide(se);
        if(pc != null)fragmentTransaction.hide(pc);
        if(tc != null)fragmentTransaction.hide(tc);
        if(p != null)fragmentTransaction.hide(p);
        if(t != null)fragmentTransaction.hide(t);
    }
}
