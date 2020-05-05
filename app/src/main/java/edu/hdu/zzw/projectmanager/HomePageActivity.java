package edu.hdu.zzw.projectmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//import android.app.FragmentManager;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;

public class HomePageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup menuBar;
    private RadioButton menuBtn;

    private ProjectListFragment pl;
    private TaskListFragment tl;
    private SearchFragment se;
    private FragmentManager fragmentManager;

    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        fragmentManager = getSupportFragmentManager();
        menuBar = (RadioGroup)findViewById(R.id.menu_bar);
        menuBar.setOnCheckedChangeListener(this);
        menuBtn = (RadioButton)findViewById(R.id.project_btn);
        menuBtn.setChecked(true);
    }


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
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(pl != null)fragmentTransaction.hide(pl);
        if(tl != null)fragmentTransaction.hide(tl);
        if(se != null)fragmentTransaction.hide(se);
    }
}
