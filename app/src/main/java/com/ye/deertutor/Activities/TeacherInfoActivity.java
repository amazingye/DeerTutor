package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Teacher;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TeacherInfoActivity extends Activity {

    public TextView teacherUserName;
    public TextView teacherTel;
    public TextView teacherEmail;
    public TextView major;
    public TextView availableGrade;
    public TextView availableSubject;
    public TextView teacherDescribe;
    public TextView verifyStatus;
    private Button toTeacherModifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        teacherUserName = (TextView)findViewById(R.id.teacherusername);
        teacherTel = (TextView)findViewById(R.id.teachertel);
        teacherEmail = (TextView)findViewById(R.id.teacheremail);
        major = (TextView)findViewById(R.id.major);
        availableGrade  =(TextView)findViewById(R.id.availablegrade);
        availableSubject = (TextView)findViewById(R.id.availablesubject);
        teacherDescribe = (TextView)findViewById(R.id.teacherdescribe);
        verifyStatus = (TextView)findViewById(R.id.verifystatus);

        initView();

        toTeacherModifyButton = (Button)findViewById(R.id.toteachermodify);
        toTeacherModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTeacherModify = new Intent(
                        TeacherInfoActivity.this,TeacherModifyActivity.class);
                startActivity(toTeacherModify);
            }
        });

    }

    public void initView(){
        final DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);

        BmobQuery<Teacher> query = new BmobQuery<Teacher>();
        query.addWhereEqualTo("userId",currentUser.getObjectId());
        query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> list, BmobException e) {
                if(e == null){
                    for (Teacher teacher : list){
                        teacherUserName.setText(currentUser.getUsername());
                        teacherTel.setText(currentUser.getMobilePhoneNumber());
                        teacherEmail.setText(currentUser.getEmail());
                        availableGrade.setText(teacher.getAvailableGrade());
                        availableSubject.setText(teacher.getAvailableSubject());
                        teacherDescribe.setText(teacher.getTeacherDescribe());
                    }
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
