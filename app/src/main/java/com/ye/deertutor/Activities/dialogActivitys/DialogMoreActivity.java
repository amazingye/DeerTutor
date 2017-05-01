package com.ye.deertutor.Activities.dialogActivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ye.deertutor.Activities.QuesDetailNoBtActivity;
import com.ye.deertutor.R;

public class DialogMoreActivity extends AppCompatActivity {


    RelativeLayout detailDialogRl;
    RelativeLayout solvedDialogRl;

    public String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_more);

        Intent getIntent = getIntent();
        questionId = getIntent.getStringExtra("questionId");

        detailDialogRl = (RelativeLayout)findViewById(R.id.quesdetail_dialog_more);
        solvedDialogRl = (RelativeLayout)findViewById(R.id.quessolved_dialog_more);

        detailDialogRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DialogMoreActivity.this,QuesDetailNoBtActivity.class);
                intent.putExtra("questionId",questionId);
                startActivity(intent);
                finish();
            }
        });


        solvedDialogRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }



}
