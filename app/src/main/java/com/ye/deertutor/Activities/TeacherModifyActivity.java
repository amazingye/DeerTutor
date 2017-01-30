package com.ye.deertutor.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Teacher;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class TeacherModifyActivity extends Activity {
    public EditText availableGradeEdit;
    public EditText availableSubjectEdit;
    public EditText teacherDescribeEdit;
    public EditText priceEdit;

    public String availableGrade;
    public String availableSubject;
    public String teacherDescribe;
    public String price;

    public Button teacherModifySaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_modify);

        availableGradeEdit = (EditText)findViewById(R.id.availablegradeedit);
        availableSubjectEdit = (EditText)findViewById(R.id.availablesubjectedit);
        teacherDescribeEdit = (EditText)findViewById(R.id.teacherdescribeedit);
        priceEdit = (EditText)findViewById(R.id.priceedit);
        teacherModifySaveButton = (Button)findViewById(R.id.teachermodifysave);

        teacherModifySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveModify();
            }
        });

    }


    public void saveModify(){
        availableGrade = availableGradeEdit.getText().toString();
        availableSubject = availableSubjectEdit.getText().toString();
        teacherDescribe = teacherDescribeEdit.getText().toString();
        price = priceEdit.getText().toString();

        DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);

        BmobQuery<Teacher> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> list, BmobException e) {
                if(e == null){
                    for(Teacher teacher2 : list){
                        updateTeacher(teacher2);
                    }
                }else {
                    Log.i("bmob",e.getMessage()+""+e.getErrorCode());
                }
            }
        });

    }

    public void updateTeacher(Teacher teacher){
        teacher.setAvailableGrade(availableGrade);
        teacher.setAvailableSubject(availableSubject);
        teacher.setTeacherDescribe(teacherDescribe);
        teacher.setPrice(price);
        teacher.update(teacher.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                    Toast.makeText(TeacherModifyActivity.this,
                            "修改资料成功",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
