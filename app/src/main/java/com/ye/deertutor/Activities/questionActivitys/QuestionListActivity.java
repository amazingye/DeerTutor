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

import com.ye.deertutor.Adapter.QuesAdapter;
import com.ye.deertutor.R;
import com.ye.deertutor.models.Question;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class QuestionListActivity extends AppCompatActivity {

    public String subjectType;
    public List<Question> questionList = new ArrayList<>();
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        final SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("加载中").setContentText("稍等一会儿噢~");

        Intent getIntent = getIntent();
        subjectType = getIntent.getStringExtra("subjectType");


        final ListView listView = (ListView)findViewById(R.id.ques_list_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent quesDetailIntent = new Intent(QuestionListActivity.this,QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("quesInfo",questionList.get(i));
                quesDetailIntent.putExtras(bundle);
                startActivity(quesDetailIntent);
            }
        });


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                BmobQuery<Question> query = new BmobQuery<>();
                query.setLimit(10);
                query.addWhereEqualTo("subjectType",subjectType);
                query.order("-createdAt");
                query.findObjects(QuestionListActivity.this, new FindListener<Question>() {
                    @Override
                    public void onSuccess(List<Question> list) {

                        for(Question question : list){
                            dialog.cancel();
                            questionList.add(question);
                        }
                        handler.sendMessage(handler.obtainMessage(0,questionList));
                        Log.d("initQues","initQues query success");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(QuestionListActivity.this,"啊哦，出错了。",Toast.LENGTH_LONG).show();
                        Log.d("initQues error",s);
                    }
                });

            }
        };


        try{
            new Thread(runnable).start();
            dialog.show();
            handler = new Handler(){
              public void handleMessage(Message msg){
                  if(msg.what == 0){
                      QuesAdapter quesAdapter = new QuesAdapter(QuestionListActivity.this,
                              R.layout.ques_item,questionList);
                      listView.setAdapter(quesAdapter);
                  }
              }
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
