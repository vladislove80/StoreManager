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

public class FirstTimeUserEntryActivity extends AppCompatActivity {
    public static final String TAG = FirstTimeUserEntryActivity.class.getSimpleName();

    private EditText teamNameEditText;
    private Button createButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user_entry);

        teamNameEditText = (EditText) findViewById(R.id.team_name_editview);
        createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(createButtonListener);

    }

    View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FirstTimeUserEntryActivity.this, TeamActivity.class);
            startActivity(intent);
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
}
