package com.ye.deertutor.models;

import cn.bmob.v3.BmobObject;

/**
 * Created by X1 Carbon on 2017/1/27.
 */

public class Teacher extends BmobObject {
    private String userId;
    private String sex;
    private String price;
    private String idCardNumber;
    private String experience;
    private String area;

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getSex(){
        return sex;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getIdCardNumber(){
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber){
        this.idCardNumber = idCardNumber;
    }

    public String getExperience(){
        return experience;
    }

    public void setExperience(String experience){
        this.experience = experience;
    }

    public String getArea(){
        return area;
    }

    public void setArea(String area){
        this.area = area;
    }
}
