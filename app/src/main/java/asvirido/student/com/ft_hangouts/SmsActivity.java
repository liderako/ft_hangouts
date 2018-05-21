package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SmsActivity extends AppCompatActivity {
    private String phoneNumber;
    private EditText messageText;
    private static boolean SMS_SEND_GRANTED = false;
    private static boolean RECEIVE_SMS_GRANTED = false;
    private static final int REQUEST_CODE_SMS = 1;
    private static final int REQUEST_CODE_SMS_TWO = 2;


    private static String colorBar;
    private static final String defaultColor = "#000099";
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_COLOR = "color";
    private static ActionBar actionBar;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        this.phoneNumber = intent.getStringExtra("PhoneNumber");
        messageText = findViewById(R.id.messageTextView);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permission == PackageManager.PERMISSION_GRANTED){
            SMS_SEND_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
        }

        int permissionTwo = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionTwo == PackageManager.PERMISSION_GRANTED){
            RECEIVE_SMS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS_TWO);
        }
        actionBar = getSupportActionBar();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void onClickSendMessage(View view) {
        if (SMS_SEND_GRANTED) {
            if (messageText.getText().toString().equals("")) {
                return ;
            }
            sendMessage(messageText.getText().toString());
        }
    }

    private void sendMessage(String messageData) {

        if(!TextUtils.isEmpty(messageData) && !TextUtils.isEmpty(this.phoneNumber)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(this.phoneNumber, null, messageData, null, null);
                Toast.makeText(getApplicationContext(), R.string.sms, Toast.LENGTH_LONG).show();
                finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SMS_SEND_GRANTED = true;
                }
            case REQUEST_CODE_SMS_TWO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RECEIVE_SMS_GRANTED = true;
                }
        }
        if (!SMS_SEND_GRANTED) {
            Toast.makeText(this, R.string.permission_one, Toast.LENGTH_LONG).show();
        }
        if (!RECEIVE_SMS_GRANTED) {
            Toast.makeText(this, R.string.permission_one, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_red:
                colorBar = "#990000";
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#990000")));
                return true;
            case R.id.action_settings_blue:
                colorBar = "#000099";
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000099")));
                return true;
            case R.id.action_settings_green:
                colorBar = "#009900";
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_PREFERENCES_COLOR, colorBar);
        editor.apply();
        editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (settings.contains(APP_PREFERENCES_COLOR)) {
            colorBar = settings.getString(APP_PREFERENCES_COLOR, defaultColor);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorBar)));
        }
    }
}
