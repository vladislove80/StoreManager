package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import storemanager.com.app.R;

public class NewTeamNameActivity extends AppCompatActivity {
    public static final String TAG = NewTeamNameActivity.class.getSimpleName();

    private Button buttonAddTeamName;
    private Button buttonCancelTeamName;
    private EditText edittextTeamName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team_name);

        buttonAddTeamName = (Button) findViewById(R.id.ok_team_name_button);
        buttonCancelTeamName = (Button) findViewById(R.id.cancel_team_name_button);
        buttonAddTeamName.setOnClickListener(buttonAddClickListener);
        buttonCancelTeamName.setOnClickListener(buttonCancelClickListener);
        edittextTeamName = (EditText) findViewById(R.id.team_name_edittext);
    }

    View.OnClickListener buttonAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String teamName = edittextTeamName.getText().toString();
            if (!teamName.equals("")) {
                int resultCode = 101;
                Intent resultIntent = new Intent();
                resultIntent.putExtra(TAG, teamName);
                setResult(resultCode, resultIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener buttonCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    };
}
