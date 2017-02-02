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
import com.ye.deertutor.models.Parent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ParentInfoActivity extends Activity {

    public ImageView parentHeadicon;
    public TextView parentUsername;
    public TextView parentTel;
    public TextView parentEmail;
    public TextView childSex;
    public TextView childGrade;
    public TextView address;

    public Button toParentModifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
        parentHeadicon = (ImageView)findViewById(R.id.parentheadicon);
        parentUsername = (TextView)findViewById(R.id.parentusername);
        parentTel = (TextView)findViewById(R.id.parenttel);
        parentEmail = (TextView)findViewById(R.id.parentemail);
        childSex = (TextView)findViewById(R.id.childsex);
        childGrade = (TextView)findViewById(R.id.childgrade);
        address = (TextView)findViewById(R.id.address);

        initView();

        toParentModifyButton = (Button)findViewById(R.id.toparentmodify);
        toParentModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toParentModify = new Intent(ParentInfoActivity.this,
                        ParentModifyActivity.class);
                startActivity(toParentModify);
            }
        });
    }

    public void initView(){
        final DeerUser currentUser = BmobUser.getCurrentUser(DeerUser.class);
        final BmobFile headIcon = currentUser.getHeadIcon();
        BmobQuery<Parent> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",currentUser);
        query.findObjects(new FindListener<Parent>() {
            @Override
            public void done(List<Parent> list, BmobException e) {
                if(e == null){
                    for(Parent parent : list){
                        Bitmap headBitmap = getHttpBitmap(headIcon.getFileUrl());
                        parentHeadicon.setImageBitmap(headBitmap);
                        parentUsername.setText(currentUser.getUsername());
                        parentTel.setText(currentUser.getMobilePhoneNumber());
                        parentEmail.setText(currentUser.getEmail());
                        childSex.setText(parent.getSex());
                        childGrade.setText(parent.getGrade());
                        address.setText(parent.getAddress());
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
