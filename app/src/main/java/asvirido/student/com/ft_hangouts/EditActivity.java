package asvirido.student.com.ft_hangouts;

import android.content.Intent;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;

        id = item.getItemId();
        return (id == R.id.action_settings_red || super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
