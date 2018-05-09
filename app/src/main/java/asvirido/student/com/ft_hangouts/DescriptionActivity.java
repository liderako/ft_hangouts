package asvirido.student.com.ft_hangouts;

import android.content.Intent;
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

    public void editEvent(View view) {
        intent = new Intent(DescriptionActivity.this, EditActivity.class);
        intent.putExtra("Name", nameView.getText().toString());
        intent.putExtra("PhoneNumber", phoneNumberView.getText().toString());
        startActivity(intent);
    }

    public void deleteContact(View view) {
        deleteContact(nameView.getText().toString());
    }

    private void deleteContact(String name) {
        ContactManager contactManager = new ContactManager(getContentResolver());
        if (contactManager.checkPermissionWrite(this) == false) {
            return ;
        }
        contactManager.deleteContact(name);
        finish();
        intent = new Intent(DescriptionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
