package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.EditText;

import java.time.Instant;

public class EditActivity extends AppCompatActivity {
    private EditText contactNameText;
    private EditText contactPhoneNumberText;
    private String  oldName;
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
        if (cursor != null) {
            int queryResultCount = cursor.getCount();
            if (queryResultCount > 0) {
                cursor.moveToFirst();
                rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            }
        }
        return (rawContactId);
    }

    public void edit(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("onAddContact", "don't have permissions");
        } else {
            updateContactPhoneByName(contactNameText.getText().toString(), contactPhoneNumberText.getText().toString());
        }
    }

    /*
     * Update contact phone number by contact name.
     * Return update contact number, commonly there should has one contact be updated.
     */
    private int updateContactPhoneByName(String givenName, String phoneNumber) {
        int ret = 0;
        ContentResolver contentResolver = getContentResolver();
        long rawContactId = getRawContactIdByName(givenName);
        if (rawContactId > -1) {
            updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, phoneNumber);
            ret = 1;
        } else {
            ret = 0;
        }
        return ret;
    }

    /* Update phone number with raw contact id and phone type.*/
    private void updatePhoneNumber(ContentResolver contentResolver, long rawContactId, int phoneType, String newPhoneNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber);
        StringBuffer whereClauseBuf = new StringBuffer();
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(phoneType);
        Uri dataUri = ContactsContract.Data.CONTENT_URI;
        int updateCount = contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
    }
}
