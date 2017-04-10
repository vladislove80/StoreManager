package storemanager.com.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.Shop;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class UserEntryOrCreateTeamActivity extends AppCompatActivity {
    public static final String TAG = UserEntryOrCreateTeamActivity.class.getSimpleName();
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_TEAM = "team";
    public final static int REQ_CODE = 2;

    private EditText teamNameEditText;
    private Button createButton;
    private ProgressBar progressBar;
    private LinearLayout ll;
    private String userEmail;
    private String userName;
    private String userId;
    private String teamName;
    private String newTeamName;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user_entry);

        progressBar = (ProgressBar) findViewById(R.id.first_time_user_entry_progressbar);
        ll = (LinearLayout) findViewById(R.id.ll_layout);

        teamNameEditText = (EditText) findViewById(R.id.team_name_editview);
        //teamNameEditText.setImeActionLabel("actionDone", KeyEvent.KEYCODE_ENTER);
        teamNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    // Handle pressing "Enter" key here
                    teamName = v.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                    //Toast.makeText(getBaseContext(), teamName, Toast.LENGTH_SHORT).show();
                    handled = true;
                    checkUserInTeam(teamName);
                }
                return handled;
            }
        });
        createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(createButtonListener);

        Intent intent = getIntent();
        userName = intent.getExtras().get(Utils.EXTRA_TAG_NAME).toString();
        userEmail = intent.getExtras().get(Utils.EXTRA_TAG_EMAIL).toString();
        userId = intent.getExtras().get(Utils.EXTRA_TAG_ID).toString();
    }

    private void checkUserInTeam(final String teamName) {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    User user;
                    hideEditKeyboard(teamNameEditText);
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        user = postSnapshot.getValue(User.class);
                        if (userId.equals(user.getId())) {
                            if (user.getStatus().equals(Utils.userStatus[0])) {
                                startAdminActivity(teamName);
                            } else if (user.getStatus().equals(Utils.userStatus[1])) {
                                dialogShops();
                            }
                            Log.d(TAG, "user = ");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Проверте имя!", Toast.LENGTH_SHORT).show();
                    ll.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
            newTeamName = data.getExtras().getString(NewTeamNameActivity.TAG);
            createTeamWithAdminInFireBase(userName, userEmail, userId, Utils.userStatus[0], newTeamName);
        }
    }

    private void dialogShops() {
        final List<Shop> shopList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    shop = postSnapshot.getValue(Shop.class);
                    if (shop != null) {shopList.add(shop);
                    }
                }
                showShopsDialog(shopList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void showShopsDialog(List<Shop> shopList){

        final String cShopItem[] = new String[1];
        final String[] shopNamesArray = new String[shopList.size()];
        int i = 0;
        for (Shop shop : shopList) {
            shopNamesArray[i] = shop.getName();
            i++;
        }

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Выберите название торговой точки:");
        alt_bld.setSingleChoiceItems(shopNamesArray, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                cShopItem[0] = shopNamesArray[item];
                Toast.makeText(getApplicationContext(), "Торговая точка \"" + cShopItem[0] + "\"", Toast.LENGTH_SHORT).show();
            }
        });

        alt_bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //mUserEmailTextView.setText(getString(R.string.google_status_fmt, userEmail));
                //mDetailTextView.setText(getString(R.string.firebase_status_fmt, userName));
                if (!TextUtils.isEmpty(cShopItem[0])) {
                    Intent intent = new Intent(UserEntryOrCreateTeamActivity.this, SummaryComposerActivity.class);
                    intent.putExtra(Utils.EXTRA_TAG_EMAIL, userEmail);
                    intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
                    intent.putExtra(Utils.EXTRA_TAG_ID, userId);
                    intent.putExtra(Utils.EXTRA_TAG_SHOP, cShopItem[0]);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Торговые точки не созданы!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alt_bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //mAddDataButton.setEnabled(true);
                dialog.dismiss();
            }
        });
        alt_bld.setCancelable(false);
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void createTeamWithAdminInFireBase(final String name, final String email, final String id, final String userStatus, final String teamName) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(teamName)) {
                    User userNew = new User();
                    userNew.setName(name);
                    userNew.setEmail(email);
                    userNew.setId(id);
                    userNew.setRegistrationDate(Utils.getCurrentDate());
                    userNew.setStatus(userStatus);
                    mDatabase.child(teamName).child("users").push().setValue(userNew);
                    startAdminActivity(teamName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void startAdminActivity(String teamName){
        Intent intent = new Intent(UserEntryOrCreateTeamActivity.this, AdminActivity.class);
        intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
        intent.putExtra(Utils.EXTRA_TAG_EMAIL, userEmail);
        intent.putExtra(Utils.EXTRA_TAG_ID, userId);
        intent.putExtra(Utils.EXTRA_TAG_TEAM, teamName);
        startActivity(intent);
        finish();
    }
}
