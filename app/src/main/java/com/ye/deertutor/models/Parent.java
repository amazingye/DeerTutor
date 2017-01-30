package com.ye.deertutor.models;

import cn.bmob.v3.BmobObject;

/**
 * Created by X1 Carbon on 2017/1/27.
 */

public class Parent extends BmobObject {
    private DeerUser userId;
    private String sex;
    private String city;
    private String grade;

    public DeerUser getUserId(){
        return userId;
    }

    public void setUserId(DeerUser userId){
        this.userId = userId;
    }

    public String getSex(){
        return sex;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getGrade(){
        return grade;
    }

    public void setGrade(String grade){
        this.grade = grade;
    }
}
