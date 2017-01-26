package com.ye.deertutor.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ye.deertutor.Fragments.CourseFragment;
import com.ye.deertutor.Fragments.HomepageFragment;
import com.ye.deertutor.Fragments.MineFragment;
import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;

import cn.bmob.v3.Bmob;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private TextView titleText;

    private HomepageFragment homepageFragment;
    private CourseFragment courseFragment;
    private MineFragment mineFragment;

    private FrameLayout frameLayout;

    private RelativeLayout homePageLayout,courseLayout,mineLayout;

    private ImageView homepageImage,courseImage,mineImage;

    private TextView homepageText,courseText,mineText;

    private FragmentManager fragmentManager;

    // 定义几个颜色
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int dark = 0xff000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        fragmentManager = getSupportFragmentManager();
        initView();
        setChoiceItem(0);
        Bmob.initialize(this, "cfe590c37becfa3b8042d7cadefaa0be");
    }


    private void initView(){
        titleText = (TextView)findViewById(R.id.title_text_tv);
        titleText.setText("首 页");


        //初始化底部导航栏控件
        homepageImage = (ImageView)findViewById(R.id.homepage_image);
        courseImage = (ImageView)findViewById(R.id.course_image);
        mineImage = (ImageView)findViewById(R.id.mine_image);
        homepageText = (TextView)findViewById(R.id.homepage_text);
        courseText = (TextView)findViewById(R.id.course_text);
        mineText = (TextView)findViewById(R.id.mine_text);
        homePageLayout =(RelativeLayout)findViewById(R.id.first_layout);
        courseLayout = (RelativeLayout)findViewById(R.id.second_layout);
        mineLayout = (RelativeLayout)findViewById(R.id.third_layout);
        homePageLayout.setOnClickListener(MainActivity.this);
        courseLayout.setOnClickListener(MainActivity.this);
        mineLayout.setOnClickListener(MainActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_layout:
                setChoiceItem(0);
                break;
            case R.id.second_layout:
                setChoiceItem(1);
                break;
            case R.id.third_layout:
                setChoiceItem(2);
                break;
            default:
                break;
        }

    }


    /**
     * 设置点击选项卡事件的处理*/
    private void setChoiceItem(int index){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChoice();  // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);
        switch (index){
            case 0:
                titleText.setText("首 页");
                homepageText.setTextColor(dark);
                homePageLayout.setBackgroundColor(gray);
                if(homepageFragment == null){
                    homepageFragment = new HomepageFragment();
                    fragmentTransaction.add(R.id.content,homepageFragment);
                }else {
                    fragmentTransaction.show(homepageFragment);
                }
                break;
            case 1:
                titleText.setText("课 程");
                courseText.setTextColor(dark);
                courseLayout.setBackgroundColor(gray);
                if(courseFragment == null){
                    courseFragment = new CourseFragment();
                    fragmentTransaction.add(R.id.content,courseFragment);
                }else {
                    fragmentTransaction.show(courseFragment);
                }
                break;
            case 2:
                titleText.setText("我 的");
                mineText.setTextColor(dark);
                mineLayout.setBackgroundColor(gray);
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content,mineFragment);
                }else {
                    fragmentTransaction.show(mineFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 当选中其中一个选项卡时其他的置为默认*/
    private void clearChoice(){
        homepageText.setTextColor(gray);
        homePageLayout.setBackgroundColor(whirt);
        courseText.setTextColor(gray);
        courseLayout.setBackgroundColor(whirt);
        mineText.setTextColor(gray);
        mineLayout.setBackgroundColor(whirt);
    }


    private void hideFragments(FragmentTransaction fragmentTransaction){
        if(homepageFragment != null){
            fragmentTransaction.hide(homepageFragment);
        }
        if(courseFragment != null){
            fragmentTransaction.hide(courseFragment);
        }
        if(mineFragment != null){
            fragmentTransaction.hide(mineFragment);
        }
    }
}
