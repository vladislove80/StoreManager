package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.activity.AdminActivity;
import storemanager.com.app.adapter.TeamFragmentAdapter;
import storemanager.com.app.models.User;

public class TeamFragment  extends Fragment {
    public static final String TAG = TeamFragment.class.getSimpleName();

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private TextView teamNameTextView;
    private String teamName;
    private List<User> usersList;


    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        teamName = AdminActivity.getTeamName();
        usersList = new ArrayList<>();

        User user = new User();
        user.setName("Test");
        user.setEmail("Emale test");
        user.setStatus("Admin");
        user.setRegistrationDate("Data test");
        usersList.add(user);

        teamNameTextView = (TextView) view.findViewById(R.id.team_name_textview);
        teamNameTextView.setText(teamName);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.team_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TeamFragmentAdapter(usersList);
        mRecyclerView.setAdapter(mAdapter);

        getUsersFromBD();

        return view;
    }

    private void getUsersFromBD() {
        mDatabase = FirebaseDatabase.getInstance().getReference(teamName);
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);
                    usersList.add(user);
                    Log.d(TAG, "user = " + null);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*Query query = mDatabase.child("users").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);
                    usersList.add(user);
                    Log.d(TAG, "user = " + null);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
