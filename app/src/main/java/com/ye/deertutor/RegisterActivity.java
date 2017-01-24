package com.ye.deertutor;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity implements View.OnClickListener{

    private EditText nickNameEdit;
    private EditText pswdEdit;
    private EditText emailEdit;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nickNameEdit = (EditText)findViewById(R.id.nicknameedit);
        pswdEdit = (EditText)findViewById(R.id.pswdedit);
        emailEdit = (EditText)findViewById(R.id.emailedit);
        registerButton = (Button)findViewById(R.id.registerbutton);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerbutton:
                String nickName = nickNameEdit.getText().toString();
                String pswd = pswdEdit.getText().toString();
                String email = emailEdit.getText().toString();

                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(nickName);
                bmobUser.setPassword(pswd);
                bmobUser.setEmail(email);
                bmobUser.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser s, BmobException e) {
                        if(e==null){
                            Toast.makeText(RegisterActivity.this,
                                    "注册成功",Toast.LENGTH_LONG).show();
                        }else{
                            e.printStackTrace();
                        }
                    }
                });
        }
    }
}
