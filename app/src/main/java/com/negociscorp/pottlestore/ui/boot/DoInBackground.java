package com.negociscorp.pottlestore.ui.boot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.negociscorp.pottlestore.R;
import com.negociscorp.pottlestore.ui.dashboard.UserDashboard;

import java.util.Arrays;
import java.util.List;

import static com.negociscorp.pottlestore.helperclasses.InternetAvailable.hasInternetAccess;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class DoInBackground extends AppCompatActivity {

    //Variables
    private static final int MYREQUESTCODE = 401;

    //Variables
    List<AuthUI.IdpConfig> providers;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_do_in_background);

        Thread conn_check = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    if (!hasInternetAccess(DoInBackground.this)) {
                        startActivity(new Intent(DoInBackground.this, NoInternet.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        conn_check.start();

        //Providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        checkUserState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread conn_check = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    if (!hasInternetAccess(DoInBackground.this)) {
                        startActivity(new Intent(DoInBackground.this, NoInternet.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        conn_check.start();
    }

    private void checkUserState() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

        if (currentUser!=null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customers");
            Query checkUser = reference.orderByChild("phone").equalTo(currentUser.getPhoneNumber());
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String namefromDB = dataSnapshot.child(currentUser.getPhoneNumber()).child("name").getValue(String.class);
                        Toast.makeText(getApplicationContext(), "Hey, "+namefromDB, Toast.LENGTH_LONG).show();

                        startActivity(new Intent(DoInBackground.this, UserDashboard.class).putExtra("name", namefromDB));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sign-Up Required", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DoInBackground.this, RegisterActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), ""+databaseError, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DoInBackground.this, RegisterActivity.class).putExtra("mobile", currentUser.getPhoneNumber()));
                    finish();
                }
            });
            Toast.makeText(this, "ID: "+currentUser.getPhoneNumber(), Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(this, "Sign In Required", Toast.LENGTH_LONG).show();
            promptSignIn();
        }
    }

    private void promptSignIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.splash_logo)
                        .setTheme(R.style.FirebaseAuthUI)
                        .build(), MYREQUESTCODE

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MYREQUESTCODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                //Fetch User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Show Number on Toast
                Toast.makeText(this, "Verified : "+user.getPhoneNumber(), Toast.LENGTH_LONG).show();
                checkUserState();


            }
            if (response == null)
            {
                Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "You need to Sign-In", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DoInBackground.class));
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Sign-In Cancelled", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "You need to Sign-In", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DoInBackground.class));
                finish();

            }
        }
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