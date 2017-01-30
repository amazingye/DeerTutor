package com.ye.deertutor.models;

import cn.bmob.v3.BmobObject;


public class Teacher extends BmobObject {

    private DeerUser userId;
    private String sex;
    private String price;
    private String idCardNumber;
    private String teacherDescribe;
    private String area;
    private String realName;
    private String availableGrade;
    private String availableSubject;
    private String verifyStatus;


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

    public String getTeacherDescribe(){
        return teacherDescribe;
    }

    public void setTeacherDescribe(String teacherDesecribe){
        this.teacherDescribe = teacherDesecribe;
    }

    public String getArea(){
        return area;
    }

    public void setArea(String area){
        this.area = area;
    }

    public String getRealName(){
        return realName;
    }

    public void setRealName(String realName){
        this.realName = realName;
    }

    public String getVerifyStatus(){
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus){
        this.verifyStatus = verifyStatus;
    }

    public String getAvailableGrade(){
        return availableGrade;
    }

    public void setAvailableGrade(String availableGrade){
        this.availableGrade = availableGrade;
    }

    public String getAvailableSubject(){
        return availableSubject;
    }

    public void setAvailableSubject(String availableSubject){
        this.availableSubject = availableSubject;
    }
}
