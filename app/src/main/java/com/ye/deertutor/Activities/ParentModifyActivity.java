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
import com.ye.deertutor.models.Parent;
import com.ye.deertutor.models.Teacher;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ParentModifyActivity extends Activity {

    public EditText childSexEdit;
    public EditText childGradeEdit;
    public EditText addressEdit;

    public String childSex;
    public String childGrade;
    public String address;

    public Button parentModifySaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_modify);

        childSexEdit = (EditText)findViewById(R.id.childsexeedit);
        childGradeEdit = (EditText)findViewById(R.id.childgradeedit);
        addressEdit = (EditText)findViewById(R.id.addressedit);

        parentModifySaveButton = (Button)findViewById(R.id.parentmodifysave);
        parentModifySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveModify();
            }
        });
    }

    public void saveModify(){
        childSex = childSexEdit.getText().toString();
        childGrade = childGradeEdit.getText().toString();
        address = addressEdit.getText().toString();

        DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);
        BmobQuery<Parent> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(new FindListener<Parent>() {
            @Override
            public void done(List<Parent> list, BmobException e) {
                if(e == null){
                    for(Parent parent2 : list){
                        updateParent(parent2);
                    }
                }else {
                    Log.i("bmob",e.getMessage()+""+e.getErrorCode());
                }
            }
        });
    }

    public void updateParent(Parent parent){
        parent.setSex(childSex);
        parent.setGrade(childGrade);
        parent.setAddress(address);
        parent.update(parent.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","更新成功");
                    Toast.makeText(ParentModifyActivity.this,
                            "修改资料成功",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
