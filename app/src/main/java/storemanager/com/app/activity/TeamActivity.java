package storemanager.com.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;

import storemanager.com.app.R;

public class TeamActivity extends AppCompatActivity {
    public static final String TAG = TeamActivity.class.getSimpleName();


    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private EditText teamNameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamNameEditText = (EditText) findViewById(R.id.team_name_edittext);
        teamNameEditText.setInputType(InputType.TYPE_NULL);
        teamNameEditText.setOnClickListener(editTextListener);
    }


    private void hideEditKeyboard(EditText itemNumberChooserEditView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(itemNumberChooserEditView.getWindowToken(), 0);
    }


    View.OnClickListener editTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            teamNameEditText.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(teamNameEditText, InputMethodManager.SHOW_FORCED);
        }
    };
}
