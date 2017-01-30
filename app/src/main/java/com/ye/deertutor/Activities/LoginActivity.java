package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText usernameInput;
    private EditText pswdInput;
    private Button loginButton;
    private TextView gotoRegisterText;

    private TextView userNameShowText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameInput = (EditText)findViewById(R.id.usernameinput);
        pswdInput = (EditText)findViewById(R.id.pswdinput);

        loginButton = (Button)findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(this);

        gotoRegisterText = (TextView)findViewById(R.id.gotoregister);
        gotoRegisterText.setOnClickListener(this);

        userNameShowText = (TextView)findViewById(R.id.usernameshowtext);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginbutton:
                String userName = usernameInput.getText().toString();
                String pswd = pswdInput.getText().toString();
                DeerUser user = new DeerUser();
                user.setUsername(userName);
                user.setPassword(pswd);
                user.login(new SaveListener<DeerUser>() {
                    @Override
                    public void done(DeerUser deerUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(LoginActivity.this,"登陆成功",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            e.printStackTrace();
                        }
                    }
                });


                break;
            case R.id.gotoregister:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
