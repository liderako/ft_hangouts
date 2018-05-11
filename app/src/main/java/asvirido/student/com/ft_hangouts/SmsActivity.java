package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
        Intent intent = new Intent(getApplicationContext(), SmsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(this.phoneNumber, null, messageData, pi,null);
        Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
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
}
