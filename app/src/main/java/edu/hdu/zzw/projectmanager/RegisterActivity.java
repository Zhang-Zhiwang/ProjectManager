package edu.hdu.zzw.projectmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    public ProjectManagerDB projectManagerDB;
    public SQLiteDatabase sql_write;
    private EditText username_reg, password_reg, password_again;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_reg = (EditText)findViewById(R.id.username_register);
        password_reg = (EditText)findViewById(R.id.password_register);
        password_again = (EditText)findViewById(R.id.password_again);
        button = (Button)findViewById(R.id.register);

        projectManagerDB = ProjectManagerDB.getInstance(this);
        sql_write = projectManagerDB.getWritableDatabase();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_reg.getText().toString().trim();
                String password = password_reg.getText().toString().trim();
                String pwd_again = password_again.getText().toString().trim();
                //用户名和密码不得为空
                if(username.length()==0 || password.length() ==0)
                    Toast.makeText(RegisterActivity.this,"用户名或密码不得为空",Toast.LENGTH_SHORT).show();
                else {
                    //重复输入密码要相等
                    if(!password.equals(pwd_again))
                        Toast.makeText(RegisterActivity.this,"请重复输入相同的密码",Toast.LENGTH_SHORT).show();
                    else {
                        Manager manager = projectManagerDB.FindOneManagerByName(sql_write,username);
                        //不允许存在相同的id
                        if(manager != null)
                            Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                        else {
                            Manager manager1 = new Manager(username,password,"","");
                            projectManagerDB.add_manager(sql_write,manager1);
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
