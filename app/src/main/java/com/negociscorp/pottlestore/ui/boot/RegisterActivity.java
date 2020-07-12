package com.negociscorp.pottlestore.ui.boot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.negociscorp.pottlestore.R;

import com.negociscorp.pottlestore.helperclasses.CustomerHelperClass;
import com.negociscorp.pottlestore.helperclasses.Rumble;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.shadowfax.proswipebutton.ProSwipeButton;
import retrofit2.Call;
import static com.negociscorp.pottlestore.helperclasses.InternetAvailable.hasInternetAccess;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class RegisterActivity extends AppCompatActivity {

    String _NAME, _PHONE, _EMAIL;

    private Handler mHandler = new Handler();

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_email)
    EditText edEmail;
    String codeSelect = "+91";

    CheckBox flag_Box;
    ProSwipeButton btn_register;

    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        ButterKnife.bind(this);

        Thread conn_check = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    if (!hasInternetAccess(RegisterActivity.this)) {
                        startActivity(new Intent(RegisterActivity.this, NoInternet.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        conn_check.start();

        getPhone();

        flag_Box = findViewById(R.id.flag_Box);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setEnabled(false);

        //When User Swipes Right
        btn_register.setOnSwipeListener(() -> {

            Rumble.init(getApplicationContext());
            Rumble.once(500);

            flag_Box.setFocusable(false);
            flag_Box.setClickable(false);
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("customers");

            btn_register.setText("SWIPE TO CONFIRM");

            _NAME = edUsername.getText().toString();
            _EMAIL = edEmail.getText().toString();

            CustomerHelperClass helperClass = new CustomerHelperClass(_NAME, _PHONE, _EMAIL);
            reference.child(_PHONE).setValue(helperClass);
            //Remove this block to input your on success result

            // task success! show TICK icon in ProSwipeButton
            btn_register.showResultIcon(true); // false if task failed
            Rumble.init(getApplicationContext());
            Rumble.makePattern()
                    .beat(300)
                    .rest(250)
                    .beat(720)
                    .playPattern();

            Toast.makeText(RegisterActivity.this, "Synced Successfully", Toast.LENGTH_LONG).show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!hasInternetAccess(RegisterActivity.this)) {
                        btn_register.showResultIcon(false); // false if task failed
                        Toast.makeText(RegisterActivity.this, "Connect to stable internet!\nLet's Try Again...",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, DoInBackground.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(RegisterActivity.this, DoInBackground.class);
                        startActivity(intent);
                        Rumble.makePattern()
                                .beat(300)
                                .playPattern();
                        finish();
                    }
                }
            }, 2000); // 4 seconds

        });
    }

    private boolean validate_NAME () {
        _NAME = edUsername.getText().toString();
        if (_NAME.isEmpty()) {
            edUsername.setError("This is a required field!");
            return false;
        }
        else {
            edUsername.setError(null);
            return true;
        }
    }

    private boolean validate_EMAIL () {
        Pattern pattern;
        Matcher matcher;
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(emailPattern);
        if (!pattern.matcher(edEmail.getText().toString()).matches()) {
            edEmail.setError("Enter a valid E-mail!");
            return false;
        }
        else if (edEmail.getText().toString().isEmpty()){
            edEmail.setError("This is a required field!");
            edEmail.setError(null);
            return false;
        }
        else{
            edEmail.setError(null);
            return true;
        }

    }

    public void validateFields(View view) {
        if (validate_NAME() && validate_EMAIL()) {
            edUsername.setFocusable(false);
            edUsername.setClickable(false);
            edEmail.setFocusable(false);
            edEmail.setClickable(false);
            btn_register.setEnabled(true);
            flag_Box.setChecked(true);
            btn_register.setText("SWIPE");
            Rumble.init(getApplicationContext());
            Rumble.makePattern()
                    .beat(300)
                    .playPattern();
        }
        else {
            Rumble.init(getApplicationContext());
            Rumble.makePattern()
                    .rest(200)
                    .beat(30)
                    .beat(50) // Automatically adds to previous beat.
                    .playPattern(4);
            btn_register.setEnabled(false);
            flag_Box.setChecked(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    if (!hasInternetAccess(RegisterActivity.this)) {
                        startActivity(new Intent(RegisterActivity.this, NoInternet.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void getPhone() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        if (currentUser!=null) {
            _PHONE = currentUser.getPhoneNumber();
        }
        else {
            Toast.makeText(this, "BAD REQUEST", Toast.LENGTH_LONG).show();
            Rumble.init(getApplicationContext());
            Rumble.makePattern()
                    .rest(200)
                    .beat(30)
                    .playPattern(2);
            startActivity(new Intent(RegisterActivity.this, DoInBackground.class));

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
            super.onBackPressed();       // bye
        }
    }
}