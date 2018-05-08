package asvirido.student.com.ft_hangouts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    private EditText contactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
//        contactText = findViewById(R.id.newContact);
    }

    public void onAddContact(View v) {
//        ContentValues contactValues = new ContentValues();
//        String newContact = contactText.getText().toString();
//        contactText.setText("");
//        contactValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, newContact);
//        contactValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, newContact);
//        Uri newUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contactValues);
//        long rawContactsId = ContentUris.parseId(newUri);
//        contactValues.clear();
//        contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactsId);
//        contactValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//        contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newContact);
//        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contactValues);
        Toast.makeText(getApplicationContext(),  " добавлен в список контактов", Toast.LENGTH_LONG).show();
    }
}
