package asvirido.student.com.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactList = findViewById(R.id.contactList);
        /* получаем разрешения */
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        /* если устройство до API 23, устанавливаем разрешение */
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED) {
            loadContacts();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;

        id = item.getItemId();
        return (id == R.id.action_settings || super.onOptionsItemSelected(item));
    }

    private void loadContacts() {
        loadNameContact();
        loadPhoneNumberContact();
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

    private void loadNameContact() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contact = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                contactsNameList.add(contact);
            }
            cursor.close();
        }
    }

    private void loadPhoneNumberContact() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsPhoneList.add(phoneNumber);
                    }
                    phones.close();
                }
            }
            cur.close();
        }
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
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }
}


