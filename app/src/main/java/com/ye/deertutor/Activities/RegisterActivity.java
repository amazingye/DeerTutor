package com.ye.deertutor.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Parent;
import com.ye.deertutor.models.Teacher;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity{

    public EditText nickNameEdit;
    public EditText pswdEdit;
    public EditText emailEdit;
    public RadioGroup typeRb;
    public RadioButton teacher;
    public RadioButton parent;
    public Button registerButton;

    public String typeString;

    public DeerUser deerUser = new DeerUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nickNameEdit = (EditText)findViewById(R.id.nicknameedit);
        pswdEdit = (EditText)findViewById(R.id.pswdedit);
        emailEdit = (EditText)findViewById(R.id.emailedit);

        typeRb = (RadioGroup)findViewById(R.id.typerg);
        teacher = (RadioButton)findViewById(R.id.teacherrb);
        parent = (RadioButton)findViewById(R.id.parentrb);

        registerButton = (Button)findViewById(R.id.registerbutton);



        typeRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == teacher.getId())
                    typeString ="teacher";
                if(i == parent.getId())
                    typeString = "parent";
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.registerbutton:

                        String nickName = nickNameEdit.getText().toString();
                        String pswd = pswdEdit.getText().toString();
                        String email = emailEdit.getText().toString();

                        deerUser.setUsername(nickName);
                        deerUser.setPassword(pswd);
                        deerUser.setEmail(email);
                        deerUser.setType(typeString);

                        deerUser.signUp(new SaveListener<DeerUser>() {
                            @Override
                            public void done(DeerUser s, BmobException e) {
                                if(e==null){
                                    Toast.makeText(RegisterActivity.this,
                                            "注册成功",Toast.LENGTH_LONG).show();
                                    addByType();
                                }else{
                                    e.printStackTrace();
                                }
                            }
                        });

                        break;
                }

            }
        });
    }

    public void addByType(){
        BmobQuery<DeerUser> query = new BmobQuery<DeerUser>();
        query.addWhereEqualTo("email",deerUser.getEmail());
        query.findObjects(new FindListener<DeerUser>() {
            @Override
            public void done(List<DeerUser> list, BmobException e) {
                if(e==null){
                    for(DeerUser deerUser2 : list){
                        if(deerUser2.getType().equals("teacher")){
                            addToTeacher(deerUser2);
                        }else if(deerUser2.getType().equals("parent")){
                            addToParent(deerUser2);
                        }
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    public void addToTeacher(DeerUser deerUser){
        Teacher teacher = new Teacher();
        teacher.setUserId(deerUser);
        teacher.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.i("bmob","教师保存成功");
                    finish();
                }else{
                    Log.i("bmob","教师保存失败："+e.getMessage());
                }
            }
        });
    }

    public void addToParent(DeerUser deerUser){
        Parent parent = new Parent();
        parent.setUserId(deerUser);
        parent.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.i("bmob","家长保存成功");
                    finish();
                }else{
                    Log.i("bmob","家长保存失败："+e.getMessage());
                }
            }
        });
    }

}
