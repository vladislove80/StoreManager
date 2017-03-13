package storemanager.com.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "GoogleActivity";
    private static final String ADMIN_EMAIL = "evgeniyvakulich@gmail.com";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private Button mAddDataButton;
    private Button mViewDataButton;

    private String userEmail;
    private String userEmailFromDatabase;
    private String userStatusFromDatabase;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        Log.v(Utils.LOG_TAG, "GoogleSignInActivity");

        //mDatabase = FirebaseDatabase.getInstance().getReference().push();

        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mAddDataButton = (Button) findViewById(R.id.start_sales_activity);
        mViewDataButton = (Button) findViewById(R.id.start_viewer_activity);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: user = " + user);
                if (user != null) {
                    userEmail = user.getEmail();
                    userName = user.getDisplayName();
                    userId = user.getUid();
                    boolean isUserInDB = isUserInDatabase(userEmail);
                    if (isUserInDB) {

                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "firebaseAuthWithGoogle: data = " + data);
            Log.d(TAG, "firebaseAuthWithGoogle: data.toString() = " + data.toString());
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "firebaseAuthWithGoogle: result = " + result);
            Log.d(TAG, "firebaseAuthWithGoogle: result.getStatus = " + result.getStatus());
            Log.d(TAG, "firebaseAuthWithGoogle: result.isSuccess = " + result.isSuccess());
            if (result.isSuccess()) {
                Log.d(TAG, "firebaseAuthWithGoogle: result.isSuccess");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Log.d(TAG, "signOut");
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        Log.d(TAG, "revokeAccess");
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(final FirebaseUser user) {
        Log.d(TAG, "updateUI");
        hideProgressDialog();
        if (user != null) {
            userEmail = user.getEmail();
            userName = user.getDisplayName();
            userId = user.getUid();
            mStatusTextView.setText(getString(R.string.google_status_fmt, userEmail));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, userName));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            /*User userNew = new User();
            userNew.setId(userId);
            userNew.setName(userName);
            userNew.setEmail(userEmail);
            userNew.setStatus("user");
            mDatabase.child("User").setValue(userNew);
*/
            /*mAddDataButton.setVisibility(View.VISIBLE);
            mAddDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogShops(user);
                }
            });
            if (userEmail.equals(ADMIN_EMAIL) || userEmail.equals("vladislove.n.u@gmail.com")) {
                mViewDataButton.setVisibility(View.VISIBLE);
                mViewDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GoogleSignInActivity.this, SummaryViewerActivity.class);
                        startActivity(intent);
                    }
                });
            }*/
        } else {
            mAddDataButton.setVisibility(View.GONE);
            mViewDataButton.setVisibility(View.GONE);
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void dialogShops(final FirebaseUser user){
        final String cShopItem[] = new String[1];
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("Выберите название торговой точки:");
        alt_bld.setSingleChoiceItems(Utils.cShops, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                cShopItem[0] = Utils.cShops[item];
                Toast.makeText(getApplicationContext(), "Торговая точка \"" + Utils.cShops[item] + "\"", Toast.LENGTH_SHORT).show();
            }
        });
        alt_bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                final String userEmail = user.getEmail();
                final String userName = user.getDisplayName();
                final String userId = user.getUid();
                mStatusTextView.setText(getString(R.string.google_status_fmt, userEmail));
                mDetailTextView.setText(getString(R.string.firebase_status_fmt, userName));
                Intent intent = new Intent(GoogleSignInActivity.this, SummaryComposerActivity.class);
                intent.putExtra(Utils.EXTRA_TAG_MAIL, userEmail);
                intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
                intent.putExtra(Utils.EXTRA_TAG_ID, userId);
                intent.putExtra(Utils.EXTRA_TAG_SHOP, cShopItem[0]);
                startActivity(intent);
            }
        });
        alt_bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void dialogUser() {

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void addUserToDatabase() {
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setEmail(userEmail);
    }

    private boolean isUserInDatabase(String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    users.add((User) postSnapshot.getValue());
                    Log.d(TAG, "User admin:" + users.get(i));
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        /*Query adminQuery = mDatabase.orderByChild("status").equalTo("admin");
        adminQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    users.add((User) postSnapshot.getValue());
                    Log.d(TAG, "User admin:" + users.get(i));
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

       /* ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.child("User").getValue(User.class);
                    if (user != null) {
                        userEmailFromDatabase = user.getEmail();
                        userStatusFromDatabase = user.getStatus();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
*/
        mDatabase.addListenerForSingleValueEvent(postListener);
        //mDatabase.removeEventListener(postListener);

        if (userEmailFromDatabase == null) {
            return false;
        } else {
            return true;
        }
    }
}
