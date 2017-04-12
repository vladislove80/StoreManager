package storemanager.com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import storemanager.com.app.R;
import storemanager.com.app.models.User;
import storemanager.com.app.utils.Utils;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    public final static int REQ_CODE = 2;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private GoogleApiClient mGoogleApiClient;

    private String userEmail;
    private String userName;
    private String userId;

    private String teamName;
    private String newTeamName;
    private List<String> allProjectsEmails;
    boolean userInTeam;

    //private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        Log.v(Utils.LOG_TAG, "GoogleSignInActivity");
        allProjectsEmails = new ArrayList<>();
        findViewById(R.id.signin_label).setOnClickListener(this);

        //statusProgressBar = (ProgressBar) findViewById(R.id.status_progress_bar);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

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

                    checkUserInBD(userEmail);
                    /*Intent intent = new Intent(GoogleSignInActivity.this, UserEntryOrCreateTeamActivity.class);
                    intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
                    intent.putExtra(Utils.EXTRA_TAG_EMAIL, userEmail);
                    intent.putExtra(Utils.EXTRA_TAG_ID, userId);
                    startActivity(intent);
                    finish();*/
                    Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                //updateUI(user);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.d(TAG, "onActivityResult: requestCode = " + requestCode);
        switch (requestCode){
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    Log.d(TAG, "firebaseAuthWithGoogle: result.isSuccess");
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    //updateUI(null);
                }
                break;
            case REQ_CODE:
                if (data != null) {
                    newTeamName = data.getExtras().getString(NewTeamNameActivity.TAG);
                    createTeamInFireBase(userName, userEmail, userId, Utils.userStatus[0], newTeamName);
                } else {
                    revokeAccess();
                }
                break;

        }
    }

    private void checkUserInBD(final String email) {
        userInTeam = false;
        mDatabase = FirebaseDatabase.getInstance().getReference("projects");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> userProjects = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        allProjectsEmails = (List<String>) postSnapshot.getValue();
                        if (allProjectsEmails.contains(email)){
                            teamName = postSnapshot.getKey();
                            userProjects.add(teamName);
                            userInTeam = true;
                        }
                    }
                    if (userProjects.size() == 1){
                        checkUserInTeam(userProjects.get(0));
                    } else if (userProjects.size() == 0){
                        //ToDo There more than one project for this email. Need dialog with project choice
                        Toast.makeText(getApplicationContext(), "Много проэктов!!", Toast.LENGTH_SHORT).show();
                    }
                    if (userInTeam == false){
                        //ToDo Current user is absent in any project
                        Toast.makeText(getApplicationContext(), "Вы не добавленны!", Toast.LENGTH_SHORT).show();
                        createTeam();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void createTeam() {
        Intent intent = new Intent(GoogleSignInActivity.this, NewTeamNameActivity.class);
        startActivityForResult(intent, REQ_CODE);
    }

    private void createTeamInFireBase(final String name, final String email, final String id, final String userStatus, final String teamName) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("projects").child(teamName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    allProjectsEmails = (List<String>) dataSnapshot.getValue();
                    Log.d(TAG, "createTeamInFireBase");
                }
                if (!allProjectsEmails.contains(email)) {
                    allProjectsEmails.add(email);
                    mDatabase.child("projects").child(teamName).setValue(allProjectsEmails);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

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
        Intent intent = new Intent(GoogleSignInActivity.this, AdminActivity.class);
        intent.putExtra(Utils.EXTRA_TAG_NAME, userName);
        intent.putExtra(Utils.EXTRA_TAG_EMAIL, userEmail);
        intent.putExtra(Utils.EXTRA_TAG_ID, userId);
        intent.putExtra(Utils.EXTRA_TAG_TEAM, teamName);
        startActivity(intent);
        finish();
    }

    private void checkUserInTeam(final String teamName) {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    User user;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        user = postSnapshot.getValue(User.class);
                        if (userEmail.equals(user.getEmail())) {
                            if (user.getId() == null) {
                                user.setId(userId);
                                mDatabase.child("users").child(postSnapshot.getKey()).setValue(user);
                            }
                            if (userId.equals(user.getId())) {
                                if (user.getStatus().equals(Utils.userStatus[0])) {
                                    startAdminActivity(teamName);
                                } else if (user.getStatus().equals(Utils.userStatus[1])) {

                                }
                                Log.d(TAG, "user = ");
                            } else {
                                Toast.makeText(getApplicationContext(), "Обратитесь к администратору!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Проверьте имя!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
        if (i == R.id.signin_label) {
            revokeAccess();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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

        } else {

        }
    }
}
