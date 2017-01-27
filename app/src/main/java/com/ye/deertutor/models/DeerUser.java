package com.ye.deertutor.models;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by X1 Carbon on 2017/1/27.
 */

public class DeerUser extends BmobUser{
    private String sex;
    private BmobFile headIcon;
    private String type;

    public String getSex(){
        return sex;
    }

    public BmobFile getHeadIcon(){
        return headIcon;
    }

    public String getType(){
        return type;
    }



    public void setSex(String sex){
        this.sex = sex;
    }

    public void setHeadIcon(BmobFile headIcon){
        this.headIcon = headIcon;
    }

    public void setType(String type){
        this.type = type;
    }
}
