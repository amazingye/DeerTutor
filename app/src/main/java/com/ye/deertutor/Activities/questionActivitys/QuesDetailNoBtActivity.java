package com.ye.deertutor.Activities.questionActivitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ye.deertutor.Activities.ChatActivity;
import com.ye.deertutor.Activities.PicDetailActivity;
import com.ye.deertutor.R;
import com.ye.deertutor.models.Question;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.GetListener;
//import cn.bmob.v3.listener.QueryListener;

public class QuesDetailNoBtActivity extends AppCompatActivity {

    public String quesId;
    public Question quesInfo;

    Question question;
    TextView quesText;
    ImageView quesPic;
    Button chatHistoryBt;
    BmobFile quesPicBmob;

    BmobIMUserInfo imUserInfo;

    Handler handler = new Handler();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_detail_no_bt);
        Intent getIntent = getIntent();
        quesId = getIntent.getStringExtra("questionId");
        quesInfo = (Question) getIntent.getSerializableExtra("quesInfo");


        quesText = (TextView)findViewById(R.id.question_text_detailNoBt_tv);
        quesPic = (ImageView)findViewById(R.id.quespic_detailNobt);
        quesPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesPic.getDrawable() != null){
                    Intent picDetailIntent = new Intent(QuesDetailNoBtActivity.this,PicDetailActivity.class);
                    picDetailIntent.putExtra("picUrl",quesPicBmob.getUrl());
                    startActivity(picDetailIntent);
                }
            }
        });

        chatHistoryBt = (Button)findViewById(R.id.chat_history_button);
        chatHistoryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesInfo.getStatus().equals("unsolved")){
                    Toast.makeText(QuesDetailNoBtActivity.this,"还没有人解答此问题噢~",Toast.LENGTH_SHORT).show();
                }else {
                    imUserInfo = new BmobIMUserInfo();
                    imUserInfo.setUserId(quesInfo.getAnswerer().getObjectId());
                    imUserInfo.setName("null");
                    BmobIM.getInstance().startPrivateConversation(imUserInfo, new ConversationListener() {
                        @Override
                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                            if(e==null){
                                //跳转到聊天页面
                                Intent chatIntent = new Intent(QuesDetailNoBtActivity.this,ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("question",question);
                                bundle.putSerializable("conversation",bmobIMConversation);
                                chatIntent.putExtras(bundle);
                                startActivity(chatIntent);
                                finish();
                            }else{
                                Log.d("startConversation error",e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        showQues();
    }


    public void showQues(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(quesId != null){
                    BmobQuery<Question> query = new BmobQuery<>();
                    query.getObject(QuesDetailNoBtActivity.this, quesId, new GetListener<Question>() {
                        @Override
                        public void onSuccess(Question questionResult) {
                            question = questionResult;
                            handler.sendMessage(handler.obtainMessage(0,question));
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d("bmob query","失败："+s+","+i);
                        }
                    });
                }else if(quesInfo != null){
                    quesText.setText(quesInfo.getQuesText());
                    quesPicBmob = quesInfo.getQuesPic();
                    if(quesPicBmob != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageLoader.getInstance().displayImage(quesPicBmob.getUrl(),quesPic,options);
                            }
                        });
                    }
                }

            }
        };


        try{
            new Thread(runnable).start();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    quesText.setText(question.getQuesText());
                    quesPicBmob = question.getQuesPic();
                    if(quesPicBmob != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageLoader.getInstance().displayImage(quesPicBmob.getUrl(),quesPic,options);
                            }
                        });
                    }
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
