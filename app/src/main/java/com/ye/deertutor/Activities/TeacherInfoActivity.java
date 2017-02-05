package com.ye.deertutor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Teacher;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TeacherInfoActivity extends Activity {

    public ImageView teacherHeadicon;
    public TextView teacherUserName;
    public TextView teacherTel;
    public TextView teacherEmail;
    public TextView major;
    public TextView availableGrade;
    public TextView availableSubject;
    public TextView teacherDescribe;
    public TextView verifyStatus;
    private Button toTeacherModifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        teacherHeadicon = (ImageView)findViewById(R.id.teacherheadicon);
        teacherUserName = (TextView)findViewById(R.id.teacherusername);
        teacherTel = (TextView)findViewById(R.id.teachertel);
        teacherEmail = (TextView)findViewById(R.id.teacheremail);
        major = (TextView)findViewById(R.id.major);
        availableGrade  =(TextView)findViewById(R.id.availablegrade);
        availableSubject = (TextView)findViewById(R.id.availablesubject);
        teacherDescribe = (TextView)findViewById(R.id.teacherdescribe);
        verifyStatus = (TextView)findViewById(R.id.verifystatus);
        verifyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toVerify = new Intent(TeacherInfoActivity.this,
                        VerifyActivity.class);
                startActivity(toVerify);
            }
        });

        initView();

        toTeacherModifyButton = (Button)findViewById(R.id.toteachermodify);
        toTeacherModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTeacherModify = new Intent(
                        TeacherInfoActivity.this,TeacherModifyActivity.class);
                startActivity(toTeacherModify);
            }
        });

    }

    public void initView(){
        final DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);
        final BmobFile headIcon = currentUser.getHeadIcon();
        BmobQuery<Teacher> query = new BmobQuery<Teacher>();
        query.addWhereEqualTo("userId",currentUser.getObjectId());
        query.findObjects(new FindListener<Teacher>() {
            @Override
            public void done(List<Teacher> list, BmobException e) {
                if(e == null){
                    for (Teacher teacher : list){
                        Bitmap headBitmap = getHttpBitmap(headIcon.getFileUrl());
                        teacherHeadicon.setImageBitmap(headBitmap);
                        teacherUserName.setText(currentUser.getUsername());
                        teacherTel.setText(currentUser.getMobilePhoneNumber());
                        teacherEmail.setText(currentUser.getEmail());
                        availableGrade.setText(teacher.getAvailableGrade());
                        availableSubject.setText(teacher.getAvailableSubject());
                        teacherDescribe.setText(teacher.getTeacherDescribe());
                        verifyStatus.setText(teacher.getVerifyStatus());
                    }
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }



    public static Bitmap getHttpBitmap(String url){

        //这是个很不符常规的写法嘤嘤嘤~~
        //允许网络请求动作在主线程中执行
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
}
