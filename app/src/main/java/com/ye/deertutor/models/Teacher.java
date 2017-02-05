package com.ye.deertutor.models;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


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
    private List<String> IDPics;


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

    public List<String> getIDPics(){
        return IDPics;
    }

    public void setIDPics(List<String> IDPics){
        this.IDPics = IDPics;
    }
}
