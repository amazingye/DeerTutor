package com.ye.deertutor.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ye.deertutor.Activities.LoginActivity;
import com.ye.deertutor.Activities.ParentInfoActivity;
import com.ye.deertutor.Activities.TeacherInfoActivity;
import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{


    private LinearLayout toUserLayout;
    public TextView userNameShowText;
    private RelativeLayout logoutLayout;
    public DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fg_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initView();
        toUserLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.touser:
                if((currentUser) == null){
                    Intent toLoginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(toLoginIntent);
                }else {
                    if(currentUser.getType().equals("teacher")){
                        Intent toTeacherInfo = new Intent(getActivity(),
                                TeacherInfoActivity.class);
                        startActivity(toTeacherInfo);
                    }else if(currentUser.getType().equals("parent")){
                        Intent toParentInfo = new Intent(getActivity(),
                                ParentInfoActivity.class);
                        startActivity(toParentInfo);
                    }
                }

                break;
            case R.id.logoutlayout:
                currentUser = BmobUser.getCurrentUser(DeerUser.class);
                currentUser.logOut();
                Toast.makeText(getActivity(),"您已退出当前账户",Toast.LENGTH_LONG).show();
                userNameShowText.setText("登陆/注册");
                currentUser = BmobUser.getCurrentUser(DeerUser.class);
        }
    }


    public void initView(){
        toUserLayout = (LinearLayout)getActivity().findViewById(R.id.touser);
        userNameShowText = (TextView)getActivity().findViewById(R.id.usernameshowtext);
        logoutLayout = (RelativeLayout) getActivity().findViewById(R.id.logoutlayout);
        currentUser = BmobUser.getCurrentUser(DeerUser.class);
        if(currentUser != null){
            userNameShowText.setText(currentUser.getUsername());
        }
    }
}
