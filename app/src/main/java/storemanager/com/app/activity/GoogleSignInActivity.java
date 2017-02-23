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

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "GoogleActivity";
    private static final String ADMIN_EMAIL = "evgeniyvakulich@gmail.com";
    private static final int RC_SIGN_IN = 9001;

    // auth
    private FirebaseAuth mAuth;


    // auth_listener
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private Button mAddDataButton;
    private Button mViewDataButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        Log.v(Utils.LOG_TAG, "GoogleSignInActivity");

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mAddDataButton = (Button) findViewById(R.id.start_sales_activity);
        mViewDataButton = (Button) findViewById(R.id.start_viewer_activity);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: user = " + user);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START onactivityresult]
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
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

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
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Log.d(TAG, "signIn");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        Log.d(TAG, "signOut");
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
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
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
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
            final String userEmail = user.getEmail();
            final String userName = user.getDisplayName();
            final String userId = user.getUid();
            mStatusTextView.setText(getString(R.string.google_status_fmt, userEmail));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, userName));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            mAddDataButton.setVisibility(View.VISIBLE);
            mAddDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog(user);
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
            }
        } else {
            mAddDataButton.setVisibility(View.GONE);
            mViewDataButton.setVisibility(View.GONE);
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    void dialog(final FirebaseUser user){
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
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
