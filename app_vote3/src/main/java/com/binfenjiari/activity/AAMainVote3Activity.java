package com.binfenjiari.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.biu.modulebase.binfenjiari.activity.SplashActivity;
import com.binfenjiari.R;

public class AAMainVote3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vote3);
        //        onClickApp_old(null);
        //        finish();
    }

    /**
     * 享去2.5
     * @param view
     */
    public void onClickApp_old(View view){

        Intent intent = new Intent(this,SplashActivity.class);
        startActivity(intent);

    }

    /**
     * 新版享去3.0
     * @param view
     */
    public void onClickApp_new(View view){
        Intent intent = new Intent(this,NavigationActivity.class);
        startActivity(intent);



    }

}
