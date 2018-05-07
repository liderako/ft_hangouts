package asvirido.student.com.ft_hangouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
}
