package asvirido.student.com.ft_hangouts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    private EditText contactNameText;
    private EditText contactPhoneNumberText;

    private static final int REQUEST_CODE_WRITE_CONTACTS = 1;
    private static boolean WRITE_CONTACTS_GRANTED = false;

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
    }

    public void onAddContact(View v) {
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
}
