package lwinmoehein.io.myarnetmaung.acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import lwinmoehein.io.myarnetmaung.R;

public class PrivacyActivity extends AppCompatActivity {
TextView privacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        privacy=findViewById(R.id.privacy);

        privacy.setText(getIntent().getStringExtra("rsid"));
    }
}
