package storemanager.com.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class UserEntryOrCreateTeamActivity extends AppCompatActivity {
    public static final String TAG = UserEntryOrCreateTeamActivity.class.getSimpleName();
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_TEAM = "team";
    public final static int REQ_CODE = 2;

    private EditText teamNameEditText;
    private Button createButton;
    private String userEmail;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user_entry);

        teamNameEditText = (EditText) findViewById(R.id.team_name_editview);
        createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(createButtonListener);

        Intent intent = getIntent();
        userName = intent.getExtras().get(Utils.EXTRA_TAG_NAME).toString();
        userEmail = intent.getExtras().get(Utils.EXTRA_TAG_EMAIL).toString();
        userId = intent.getExtras().get(Utils.EXTRA_TAG_ID).toString();
    }

    View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserEntryOrCreateTeamActivity.this, NewTeamNameActivity.class);
            startActivityForResult(intent, REQ_CODE);
        }
    };

    private void hideEditKeyboard(EditText itemNumberChooserEditView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(itemNumberChooserEditView.getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideEditKeyboard(teamNameEditText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQ_CODE) {
            String newTeamName = data.getExtras().getString(NewTeamNameActivity.TAG);
            Intent intent = new Intent(UserEntryOrCreateTeamActivity.this, AdminActivity.class);
            intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
            intent.putExtra(Utils.EXTRA_TAG_EMAIL, userEmail);
            intent.putExtra(Utils.EXTRA_TAG_ID, userId);
            intent.putExtra(Utils.EXTRA_TAG_TEAM, newTeamName);
            startActivity(intent);
            finish();
        }
    }
}
