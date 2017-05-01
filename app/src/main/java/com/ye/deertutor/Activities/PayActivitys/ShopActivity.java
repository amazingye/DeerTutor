package com.ye.deertutor.Activities.PayActivitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ye.deertutor.R;

public class ShopActivity extends AppCompatActivity {

    TextView cashOutTv;
    TextView diamondNumTv;
    RelativeLayout diamondBuy50Rl;
    RelativeLayout diamondBuy100Rl;
    RelativeLayout diamondBuy300Rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        cashOutTv = (TextView)findViewById(R.id.cashOut_tv);
        diamondNumTv = (TextView)findViewById(R.id.diaNum_tv_shop);
        diamondBuy50Rl = (RelativeLayout)findViewById(R.id.diamond_buy_50_rl);
        diamondBuy100Rl = (RelativeLayout)findViewById(R.id.diamond_buy_100_rl);
        diamondBuy300Rl = (RelativeLayout)findViewById(R.id.diamond_buy_300_rl);

        diamondBuy50Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
