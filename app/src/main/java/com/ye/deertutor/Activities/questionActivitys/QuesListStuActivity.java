package com.ye.deertutor.Activities.questionActivitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ye.deertutor.Adapter.QuesStuAdapter;
import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Question;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class QuesListStuActivity extends AppCompatActivity {

    DeerUser currentUser;

    public ListView listView;
    public List<Question> list =new ArrayList<>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                QuesStuAdapter  adapter = new QuesStuAdapter(QuesListStuActivity.this,
                        R.layout.ques_student_item,list);
                listView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_list_stu);
        currentUser = BmobUser.getCurrentUser(this,DeerUser.class);

        listView = (ListView)findViewById(R.id.ques_list_stu_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent quesDetailIntent = new Intent(QuesListStuActivity.this,QuesDetailNoBtActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quesInfo",list.get(i));
                quesDetailIntent.putExtras(bundle);
                startActivity(quesDetailIntent);
            }
        });


        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                BmobQuery<Question> query = new BmobQuery<>();
                query.addWhereEqualTo("questioner",currentUser);
                query.order("-updatedAt");
                query.findObjects(QuesListStuActivity.this, new FindListener<Question>() {
                    @Override
                    public void onSuccess(List<Question> resultList) {
                        for(Question question : resultList){
                            list.add(question);
                        }
                        handler.sendMessage(handler.obtainMessage(0,list));
                        Log.d("initQues","initQues query success");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(QuesListStuActivity.this,"啊哦，出错了。",Toast.LENGTH_LONG).show();
                        Log.d("initQues error",s);
                    }
                });
            }
        };

        try{
            new Thread(runnable).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
