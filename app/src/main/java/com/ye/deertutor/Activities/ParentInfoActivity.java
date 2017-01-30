package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Parent;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ParentInfoActivity extends Activity {

    public TextView parentUsername;
    public TextView parentTel;
    public TextView parentEmail;
    public TextView childSex;
    public TextView childGrade;
    public TextView address;

    public Button toParentModifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
        parentUsername = (TextView)findViewById(R.id.parentusername);
        parentTel = (TextView)findViewById(R.id.parenttel);
        parentEmail = (TextView)findViewById(R.id.parentemail);
        childSex = (TextView)findViewById(R.id.childsex);
        childGrade = (TextView)findViewById(R.id.childgrade);
        address = (TextView)findViewById(R.id.address);

        initView();

        toParentModifyButton = (Button)findViewById(R.id.toparentmodify);
        toParentModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toParentModify = new Intent(ParentInfoActivity.this,
                        ParentModifyActivity.class);
                startActivity(toParentModify);
            }
        });
    }

    public void initView(){
        final DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);

        BmobQuery<Parent> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(new FindListener<Parent>() {
            @Override
            public void done(List<Parent> list, BmobException e) {
                if(e == null){
                    for(Parent parent : list){
                        parentUsername.setText(currentUser.getUsername());
                        parentTel.setText(currentUser.getMobilePhoneNumber());
                        parentEmail.setText(currentUser.getEmail());
                        childSex.setText(parent.getSex());
                        childGrade.setText(parent.getGrade());
                        address.setText(parent.getAddress());
                    }
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
