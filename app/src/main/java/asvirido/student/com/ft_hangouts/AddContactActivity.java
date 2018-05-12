package asvirido.student.com.ft_hangouts;

import android.Manifest;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    private EditText contactNameText;
    private EditText contactPhoneNumberText;

    /* For permissions */
    private static final int REQUEST_CODE_WRITE_CONTACTS = 1;
    private static boolean WRITE_CONTACTS_GRANTED = false;


    /* For color */
    private static String colorBar;
    private static final String defaultColor = "#000099";
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_COLOR = "color";
    private static ActionBar actionBar;
    private SharedPreferences settings;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        contactNameText = findViewById(R.id.nameEditText);
        contactPhoneNumberText = findViewById(R.id.phoneEditText);
        intent = getIntent();
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            WRITE_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_WRITE_CONTACTS);
        }
        actionBar = getSupportActionBar();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void onClickAddContact(View v) {
        String DisplayName = contactNameText.getText().toString();
        String MobileNumber = contactPhoneNumberText.getText().toString();
        addContact(DisplayName, MobileNumber);
    }
    private void addContact(String name, String phoneNumber) {
        ContactManager contactManager = new ContactManager(getContentResolver());
        if (contactManager.checkPermissionWrite(this) == false) {
            return ;
        }
        contactManager.addContact(name, phoneNumber);
        finish();
        Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WRITE_CONTACTS_GRANTED = true;
                }
        }
        if (!WRITE_CONTACTS_GRANTED) {
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
