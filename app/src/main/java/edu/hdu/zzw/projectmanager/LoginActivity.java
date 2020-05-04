package edu.hdu.zzw.projectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    public ProjectManagerDB projectManagerDB;
    public SQLiteDatabase sqL_read;
    private EditText username, password;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***
         * sqLite需要设计成单例模式
         * 以避免频繁地实例化
         * */
        projectManagerDB = ProjectManagerDB.getInstance(this);
        sqL_read = projectManagerDB.getReadableDatabase();
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_str = username.getText().toString().trim();
                String password_str = password.getText().toString().trim();
                if(username_str.length() == 0 || password_str.length() == 0) {
                    Toast.makeText(LoginActivity.this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
                }
                else {
                    Manager manager = projectManagerDB.FindOneManagerByName(sqL_read,username_str);
                    Intent intent = new Intent(LoginActivity.this,HomePageActivity.class);
                    if(manager == null) {
                        Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(!password_str.equals(manager.getPassword())) {
                            Toast.makeText(LoginActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                        }
                        else  {
                            intent.putExtra("manager", new Gson().toJson(manager));
                            //第二种方法，使用接口
                            //intent.putExtra("m",manager);
                            startActivity(intent);
                        }
                    }
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
