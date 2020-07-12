package com.negociscorp.pottlestore.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.negociscorp.pottlestore.R;
import com.negociscorp.pottlestore.adapters.main.DashboardAdapter;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class UserDashboard extends AppCompatActivity {


    BottomAppBar bottomAppBar;
    TextView textView;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    LinearLayout contentView;

    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Hooks
        contentView = findViewById(R.id.content);
        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("name");
        String[] split_name = fullname.split(" ");
        String name = split_name[0];

        try {
            if (!name.isEmpty()) {
                textView.setText("Pottle Store\nHello "+name);
            }
        }
        catch (Exception e){
            Log.e("Error", ""+e);
        }


        //Menu Hooks
        bottomAppBar = findViewById(R.id.bottomAppBar2);

        //Tab Hooks
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.custom_viewpager);
        viewPager2.setAdapter(new DashboardAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Food Store");
                        break;
                    }
                    case 1: {
                        tab.setText("Hygiene Store");
                        break;
                    }
                    case 2: {
                        tab.setText("Green Store");
                        break;
                    }
                }
            }
                }
                );
        tabLayoutMediator.attach();
    }

    private long backPressedTime = 0;    // used by onBackPressed()
    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press again to exit",
                    Toast.LENGTH_SHORT).show();
        } else {    // this guy is serious
            // clean up
            finishAffinity();       // bye
        }
    }

}