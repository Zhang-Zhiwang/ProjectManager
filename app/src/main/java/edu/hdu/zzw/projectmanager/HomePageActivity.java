package edu.hdu.zzw.projectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        String ManagerJson = getIntent().getStringExtra("manager");
        Manager manager = new Gson().fromJson(ManagerJson,Manager.class);
    }
}
