package com.negociscorp.pottlestore.ui.boot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.lets_go_splash.CreateAnim;
import com.app.lets_go_splash.OnAnimationListener;
import com.app.lets_go_splash.StarterAnimation;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.negociscorp.pottlestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView appLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        startSplashAnim();
    }

    private void startSplashAnim() {

        appLogo = findViewById(R.id.imageView);
        new StarterAnimation(getAnimList(), new OnAnimationListener() {
            @Override
            public void onStartAnim() {
                // TODO::
            }

            @Override
            public void onRepeat() {
                // TODO::
            }

            @Override
            public void onEnd() {
                // TODO: Do what you want to do after end of animations
                afterSplash();
            }
        }).startSequentialAnimation(appLogo);

    }

    private void afterSplash() {
        appLogo.setVisibility(View.GONE);
        startActivity(new Intent(SplashActivity.this, DoInBackground.class));
        overridePendingTransition(R.anim.whole_animation, R.anim.no_animaiton);
        finish();
    }

    private ArrayList<Animation> getAnimList() {
        ArrayList<Animation> animList = new ArrayList<>();

        // We need to add INSTANCE when ever we need to access a object file in Kotlin from Java class
        // This denotes that CreateAnim is a singleton file and can able to have only one instance

        animList.add(CreateAnim.INSTANCE.createAnimation(getApplicationContext(), R.anim.no_animaiton));
        animList.add(CreateAnim.INSTANCE.createAnimation(getApplicationContext(), R.anim.rotate));
        animList.add(CreateAnim.INSTANCE.createAnimation(getApplicationContext(), R.anim.zoom_out_fast));
        animList.add(CreateAnim.INSTANCE.createAnimation(getApplicationContext(), R.anim.fade_in));

        return animList;
    }
}