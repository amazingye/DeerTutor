package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.Context;
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

import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends Activity{

    public EditText nickNameEdit;
    public EditText pswdEdit;
    public EditText telEdit;
    public EditText vercodeEdit;
    public RadioGroup typeRb;
    public RadioButton teacher;
    public RadioButton parent;
    public Button getVerCodeButton;
    public Button registerButton;

    //public TextView userNameShowText;

    public String typeString;

    public DeerUser deerUser = new DeerUser();

    public String tel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //nickNameEdit = (EditText)findViewById(R.id.nicknameedit);
        pswdEdit = (EditText)findViewById(R.id.pswdedit);
        telEdit = (EditText)findViewById(R.id.teledit);
        vercodeEdit = (EditText)findViewById(R.id.vercodeedit);


        typeRb = (RadioGroup)findViewById(R.id.typerg);
        teacher = (RadioButton)findViewById(R.id.teacherrb);
        parent = (RadioButton)findViewById(R.id.parentrb);

        getVerCodeButton = (Button)findViewById(R.id.getvercodebutton);
        registerButton = (Button)findViewById(R.id.registerbutton);

        //userNameShowText = (TextView)findViewById(R.id.usernameshowtext);

        getVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tel = telEdit.getText().toString();
                cn.bmob.sms.BmobSMS.requestSMSCode(RegisterActivity.this, tel, "deerMsg", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                        if(e == null){
                            Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                            registerButton.setVisibility(View.VISIBLE);
                            getVerCodeButton.setText("请稍等...");
                            getVerCodeButton.setClickable(false);
                        }else {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });




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

                        String vercode = vercodeEdit.getText().toString();

                        cn.bmob.sms.BmobSMS.verifySmsCode(RegisterActivity.this, tel,vercode , new VerifySMSCodeListener() {
                            @Override
                            public void done(cn.bmob.sms.exception.BmobException e) {
                                if(e ==null){
                                    //String nickName = nickNameEdit.getText().toString();
                                    String pswd = pswdEdit.getText().toString();
                                    //String email = emailEdit.getText().toString();
                                    tel = telEdit.getText().toString();

                                    deerUser.setUsername("小鹿用户"+tel);
                                    deerUser.setPassword(pswd);
                                    //deerUser.setEmail(email);
                                    deerUser.setType(typeString);
                                    deerUser.setMobilePhoneNumber(tel);

                                    deerUser.signUp(new SaveListener<DeerUser>() {
                                        @Override
                                        public void done(DeerUser s, BmobException e) {
                                            if(e==null){
                                                addByType();
                                            }else{
                                                Toast.makeText(RegisterActivity.this,
                                                        ""+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else {
                                   Toast.makeText(RegisterActivity.this,
                                           e.getMessage(),Toast.LENGTH_LONG).show();
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
        query.addWhereEqualTo("mobilePhoneNumber",deerUser.getMobilePhoneNumber());
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
        teacher.setTeacherDescribe("这位老师很神秘噢");
        teacher.setVerifyStatus("未认证");
        teacher.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    new SweetAlertDialog(RegisterActivity.this).
                            setTitleText("教师注册成功！").show();
                    Log.i("bmob","教师保存成功");
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,
                            ""+e.getMessage(),Toast.LENGTH_LONG).show();
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
                    new SweetAlertDialog(RegisterActivity.this).
                            setTitleText("家长注册成功！").show();
                    Log.i("bmob","家长保存成功");
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,
                            ""+e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.i("bmob","家长保存失败："+e.getMessage());
                }
            }
        });
    }

}
