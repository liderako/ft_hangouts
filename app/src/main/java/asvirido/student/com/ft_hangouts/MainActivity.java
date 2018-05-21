package asvirido.student.com.ft_hangouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    private ListView contactList;

    private ArrayList<String> contactsNameList = new ArrayList<String>();
    private ArrayList<String> contactsPhoneList = new ArrayList<String>();
    private Intent intent;
    private static String colorBar;
    private static final String defaultColor = "#000099";
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_COLOR = "color";
    private static ActionBar actionBar;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        this.contactList = findViewById(R.id.contactList);
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        if (READ_CONTACTS_GRANTED) {
            loadContacts();
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

    private void loadContacts() {
        load();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsNameList);
        contactList.setAdapter(adapter);
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    intent = new Intent(MainActivity.this, DescriptionActivity.class);
                    intent.putExtra("Name", contactsNameList.get(position));
                    intent.putExtra("PhoneNumber", contactsPhoneList.get(position));
                    startActivity(intent);
                    return false;
            }
        });
    }


    private void load() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        try {
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                this.contactsNameList.add(name);
                this.contactsPhoneList.add(phoneNumber);
            }
        }
        catch (NullPointerException e) {
            Log.d("error load", e.getMessage());
        }
        finally {
            phones.close();
        }
    }

    public void changeActivity(View view) {
        intent = new Intent(MainActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    READ_CONTACTS_GRANTED = true;
                }
        }
        if (READ_CONTACTS_GRANTED) {
            loadContacts();
        } else {
            Toast.makeText(this, R.string.permission_one, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(MainActivity.this, MainActivity.class);  //your class
        startActivity(i);
        finish();

    }
}


