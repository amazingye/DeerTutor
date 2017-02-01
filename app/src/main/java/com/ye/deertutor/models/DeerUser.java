package com.ye.deertutor.models;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;



public class DeerUser extends BmobUser{

    private BmobFile headIcon;
    private String type;


    public BmobFile getHeadIcon(){
        return headIcon;
    }

    public String getType(){
        return type;
    }


    public void setHeadIcon(BmobFile headIcon){
        this.headIcon = headIcon;
    }

    public void setType(String type){
        this.type = type;
    }
}
