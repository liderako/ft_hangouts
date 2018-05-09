package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private EditText contactNameText;
    private EditText contactPhoneNumberText;

    private static final int REQUEST_CODE_WRITE_CONTACTS = 1;
    private static boolean WRITE_CONTACTS_GRANTED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        contactNameText = findViewById(R.id.nameEditText);
        contactPhoneNumberText = findViewById(R.id.phoneEditText);

        /* получаем разрешения */
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        /* если устройство до API 23, устанавливаем разрешение */
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            WRITE_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_WRITE_CONTACTS);
        }
    }

    public void onAddContact(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("onAddContact", "don't have permissions");
            return ;
        }
        String DisplayName = contactNameText.getText().toString();
        String MobileNumber = contactPhoneNumberText.getText().toString();
        if ((MobileNumber.matches("[0-9_+]+")) == false) {
            Toast.makeText(this, R.string.phone_number_error, Toast.LENGTH_LONG).show();
            return ;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName) // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            finish();
            Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
            startActivity(intent);
        }
        catch (RemoteException e) {
            Log.d("add", e.getMessage());
        }
        catch (OperationApplicationException e) {
            Log.d("add", e.getMessage());
        }
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
}
