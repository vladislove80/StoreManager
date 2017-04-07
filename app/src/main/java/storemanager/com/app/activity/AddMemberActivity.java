package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class AddMemberActivity extends AppCompatActivity {
    public static final String TAG = AddMemberActivity.class.getSimpleName();

    private ImageView memberImage;
    private Button addMemberButton;
    private EditText memberName;
    private EditText memberEmail;
    private EditText memberStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        addMemberButton = (Button) findViewById(R.id.add_new_member_button);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, status;
                name = memberName.getText().toString();
                email = memberEmail.getText().toString();
                status = memberStatus.getText().toString();
                if(!TextUtils.isEmpty(name)
                        &&!TextUtils.isEmpty(email)
                        &&!TextUtils.isEmpty(status)){
                    int resultCode = 101;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_NAME, name);
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_EMAIL, email);
                    resultIntent.putExtra(Utils.EXTRA_TAG_ADD_NEW_MEMBER_STATUS, status);
                    setResult(resultCode, resultIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        memberName = (EditText) findViewById(R.id.member_name_editview);
        memberEmail = (EditText) findViewById(R.id.member_email_editview);
        memberStatus = (EditText) findViewById(R.id.member_status_editview);
    }
}
