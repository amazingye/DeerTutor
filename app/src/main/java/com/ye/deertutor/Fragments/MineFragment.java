package com.ye.deertutor.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ye.deertutor.Activities.LoginActivity;
import com.ye.deertutor.R;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{


    private LinearLayout toUserLayout;
    private TextView userNameShowText;
    private RelativeLayout logoutLayout;
    BmobUser currentUser;

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
                if((currentUser = BmobUser.getCurrentUser()) == null){
                    Intent toLoginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(toLoginIntent);
                }
                break;
            case R.id.logoutlayout:
                currentUser = BmobUser.getCurrentUser();
                currentUser.logOut();
                Toast.makeText(getActivity(),"您已退出当前账户",Toast.LENGTH_LONG).show();
                userNameShowText.setText("登陆/注册");
        }
    }


    public void initView(){
        toUserLayout = (LinearLayout)getActivity().findViewById(R.id.touser);
        userNameShowText = (TextView)getActivity().findViewById(R.id.usernameshowtext);
        logoutLayout = (RelativeLayout) getActivity().findViewById(R.id.logoutlayout);
        currentUser = BmobUser.getCurrentUser();
        if(currentUser != null){
            userNameShowText.setText(currentUser.getUsername().toString());
        }
    }
}
