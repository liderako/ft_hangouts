package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DescriptionActivity extends AppCompatActivity {
    private Intent intent;
    private TextView nameView;
    private TextView phoneNumberView;

    /* For permissions */
    private static final int REQUEST_CODE_CALL = 1;
    private static boolean CALL_CONTACTS_GRANTED = false;
    private static final int REQUEST_CODE_WRITE = 2;
    private static boolean WRITE_CONTACTS_GRANTED = false;

    /* For color */
    private static String colorBar;
    private static final String defaultColor = "#000099";
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_COLOR = "color";
    private static ActionBar actionBar;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        nameView = findViewById(R.id.nameView);
        phoneNumberView = findViewById(R.id.phoneNumberView);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            CALL_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
        }
        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            WRITE_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_WRITE);
        }

        String name = intent.getStringExtra("Name");
        String phoneNumber = intent.getStringExtra("PhoneNumber");
        Log.d("CREATE", name);
        Log.d("CREATE", phoneNumber);
        nameView.setText(name);
        phoneNumberView.setText(phoneNumber);
        actionBar = getSupportActionBar();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void editEvent(View view) {
        intent = new Intent(DescriptionActivity.this, EditActivity.class);
        intent.putExtra("Name", nameView.getText().toString());
        intent.putExtra("PhoneNumber", phoneNumberView.getText().toString());
        startActivity(intent);
    }

    public void smsOnClick(View view) {
        intent = new Intent(DescriptionActivity.this, SmsActivity.class);
        intent.putExtra("Name", nameView.getText().toString());
        intent.putExtra("PhoneNumber", phoneNumberView.getText().toString());
        startActivity(intent);
    }

    public void deleteContactOnClick(View view) {
        deleteContact(nameView.getText().toString());
    }

    public void callOnClick(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumberView.getText().toString()));

        if (ActivityCompat.checkSelfPermission(DescriptionActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private void deleteContact(String name) {
        ContactManager contactManager = new ContactManager(getContentResolver());
        if (!contactManager.checkPermissionWrite(this)) {
            return ;
        }
        contactManager.deleteContact(name);
        finish();
        intent = new Intent(DescriptionActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CALL_CONTACTS_GRANTED = true;
                }
            case REQUEST_CODE_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WRITE_CONTACTS_GRANTED = true;
                }
        }
        if (!CALL_CONTACTS_GRANTED) {
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
