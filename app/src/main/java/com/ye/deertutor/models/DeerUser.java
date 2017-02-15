package com.ye.deertutor.models;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;



public class DeerUser extends BmobUser{

    private BmobFile headIcon;
    private String type;
    private String appointtedTeacherId;


    public BmobFile getHeadIcon(){
        return headIcon;
    }

    public String getType(){
        return type;
    }

    public String getAppointtedTeacherId(){
        return appointtedTeacherId;
    }


    public void setHeadIcon(BmobFile headIcon){
        this.headIcon = headIcon;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setAppointtedTeacherId(String appointtedTeacherId){
        this.appointtedTeacherId = appointtedTeacherId;
    }

}
