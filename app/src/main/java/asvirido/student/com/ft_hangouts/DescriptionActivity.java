package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {
    private Intent intent;
    private TextView nameView;
    private TextView phoneNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        nameView = findViewById(R.id.nameView);
        phoneNumberView = findViewById(R.id.phoneNumberView);

        String name = intent.getStringExtra("Name");
        String phoneNumber = intent.getStringExtra("PhoneNumber");
        Log.d("CREATE", name);
        Log.d("CREATE", phoneNumber);
        nameView.setText(name);
        phoneNumberView.setText(phoneNumber);
    }

    /* Get raw contact id by contact given name and family name.
    *  Return raw contact id.
    **/
    private long getRawContactIdByName(String givenName) {
        ContentResolver contentResolver = getContentResolver();
        String queryColumnArr[] = {ContactsContract.RawContacts._ID};
        String displayName = givenName;
        String whereClause = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + displayName + "'";
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        Cursor cursor = contentResolver.query(rawContactUri, queryColumnArr, whereClause, null, null);
        long rawContactId = -1;
        if(cursor!=null) {
            int queryResultCount = cursor.getCount();
            if(queryResultCount > 0) {
                cursor.moveToFirst();
                rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            }
        }
        return (rawContactId);
    }

    public void editEvent(View view) {
//        finish();
//        intent = new Intent(DescriptionActivity.this, DescriptionActivity.class);
//        intent.putExtra("Name", contactsNameList.get(position));
//        intent.putExtra("PhoneNumber", contactsPhoneList.get(position));
//        startActivity(intent);
    }

    public void deleteContact(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("onAddContact", "don't have permissions");
            return ;
        }
        Log.d("delete", nameView.getText().toString());

        long rawContactId = getRawContactIdByName(nameView.getText().toString());
        ContentResolver contentResolver = getContentResolver();
        Uri dataContentUri = ContactsContract.Data.CONTENT_URI;
        StringBuffer dataWhereClauseBuf = new StringBuffer();
        dataWhereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        dataWhereClauseBuf.append(" = ");
        dataWhereClauseBuf.append(rawContactId);
        contentResolver.delete(dataContentUri, dataWhereClauseBuf.toString(), null);
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
        StringBuffer rawContactWhereClause = new StringBuffer();
        rawContactWhereClause.append(ContactsContract.RawContacts._ID);
        rawContactWhereClause.append(" = ");
        rawContactWhereClause.append(rawContactId);
        contentResolver.delete(rawContactUri, rawContactWhereClause.toString(), null);
        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        StringBuffer contactWhereClause = new StringBuffer();
        contactWhereClause.append(ContactsContract.Contacts._ID);
        contactWhereClause.append(" = ");
        contactWhereClause.append(rawContactId);
        contentResolver.delete(contactUri, contactWhereClause.toString(), null);

        finish();
        intent = new Intent(DescriptionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
