package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storemanager.com.app.R;
import storemanager.com.app.utils.Utils;

public class GeneralStoreFragment extends Fragment {
    public static final String TAG = GeneralStoreFragment.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ProgressBar progressBar;

    public static GeneralStoreFragment newInstance() {
        GeneralStoreFragment fragment = new GeneralStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(Utils.LOG_TAG, "GeneralStoreFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return view;
    }
}
