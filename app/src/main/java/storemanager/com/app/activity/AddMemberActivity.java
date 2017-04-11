package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class AddMemberActivity extends AppCompatActivity {
    public static final String TAG = AddMemberActivity.class.getSimpleName();

    private ImageView memberImage;
    private Button addMemberButton;
    private EditText memberName;
    private EditText memberEmail;
    private Spinner statusSpinner;
    private ArrayAdapter<String> memberStatusSpinnerAdapter;
    private String memberStatusFromSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        addMemberButton = (Button) findViewById(R.id.add_new_member_button);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email;
                name = memberName.getText().toString();
                email = memberEmail.getText().toString();
                if(!TextUtils.isEmpty(name)
                        &&!TextUtils.isEmpty(email)
                        &&!TextUtils.isEmpty(memberStatusFromSpinner)){
                    int resultCode = 101;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_NAME, name);
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_EMAIL, email);
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_STATUS, memberStatusFromSpinner );
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        memberName = (EditText) findViewById(R.id.member_name_editview);
        memberEmail = (EditText) findViewById(R.id.member_email_editview);
        statusSpinner = (Spinner) findViewById(R.id.member_status_spinner);

        final String[] roles = new String[Utils.userStatus.length];
        for (int i = 0; i <= Utils.userStatus.length - 1; i++) {
            roles[i] = Utils.userStatus[Utils.userStatus.length - 1 - i];
        }

        memberStatusSpinnerAdapter = new ArrayAdapter<>(this, R.layout.layout_add_member_status_spinner, roles);
        statusSpinner.setAdapter(memberStatusSpinnerAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                memberStatusFromSpinner = roles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
