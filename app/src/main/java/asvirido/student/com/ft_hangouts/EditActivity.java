package asvirido.student.com.ft_hangouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    private EditText contactNameText;
    private EditText contactPhoneNumberText;
    private String  oldName;

    /* For color */
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
        setContentView(R.layout.activity_edit);

        contactNameText = findViewById(R.id.nameEditText);
        contactPhoneNumberText = findViewById(R.id.phoneEditText);

        String name = intent.getStringExtra("Name");
        String phoneNumber = intent.getStringExtra("PhoneNumber");

        oldName = name;
        contactNameText.setText(name);
        contactPhoneNumberText.setText(phoneNumber);
        actionBar = getSupportActionBar();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void edit(View view) {
        ContactManager contactManager = new ContactManager(getContentResolver());
        if (contactManager.checkPermissionWrite(this) == false) {
            return ;
        }
        String displayName = contactNameText.getText().toString();
        String mobileNumber = contactPhoneNumberText.getText().toString();
        contactManager.deleteContact(this.oldName);
        contactManager.addContact(displayName, mobileNumber);
        finish();
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
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
