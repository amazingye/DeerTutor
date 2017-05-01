package com.ye.deertutor.Activities.questionActivitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ye.deertutor.Activities.ChatActivity;
import com.ye.deertutor.Activities.PicDetailActivity;
import com.ye.deertutor.R;
import com.ye.deertutor.models.DeerUser;
import com.ye.deertutor.models.Question;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class QuestionDetailActivity extends AppCompatActivity implements View.OnClickListener{

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    TextView quesTextview;
    ImageView quesPic;
    Button answerButton;

    public Question question;
    public DeerUser questioner;
    public String questionerName;
    public DeerUser currentUser;
    public String quesText;
    public BmobFile quesPicBmob;
    public BmobIMUserInfo imUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        Intent getIntent = getIntent();
        question = (Question)getIntent.getSerializableExtra("quesInfo");
        questioner = question.getQuestioner();
        quesText = question.getQuesText();



        currentUser = BmobUser.getCurrentUser(QuestionDetailActivity.this,DeerUser.class);

        imUserInfo = new BmobIMUserInfo();
        imUserInfo.setUserId(questioner.getObjectId());
        imUserInfo.setName("null");



        quesTextview = (TextView)findViewById(R.id.question_text_detail_tv);
        quesTextview.setText(quesText);
        quesPic = (ImageView)findViewById(R.id.quespic_detail);
        quesPic.setOnClickListener(this);

        answerButton = (Button)findViewById(R.id.answer_button);
        answerButton.setOnClickListener(this);

        showQues();

    }


    public void showQues(){
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.answer_button:

                BmobIM.getInstance().startPrivateConversation(imUserInfo, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                        if(e==null){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    question.setAnswerer(currentUser);
                                    question.setStatus("solving");
                                    question.update(QuestionDetailActivity.this, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d("ques update","success");
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Log.d("ques update error",s);
                                        }
                                    });

                                }
                            }).start();
                            //跳转到聊天页面
                            Intent chatIntent = new Intent(QuestionDetailActivity.this,ChatActivity.class);
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
                break;

            case R.id.quespic_detail:
                if(quesPic.getDrawable() != null){
                    Intent picDetailIntent = new Intent(QuestionDetailActivity.this,PicDetailActivity.class);
                    picDetailIntent.putExtra("picUrl",quesPicBmob.getUrl());
                    startActivity(picDetailIntent);
                }

        }
    }

    


}

