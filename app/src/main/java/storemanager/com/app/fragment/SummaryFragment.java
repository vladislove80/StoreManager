package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import storemanager.com.app.R;

public class SummaryFragment extends Fragment {
    public static final String TAG = SummaryFragment.class.getSimpleName();

    private int mPage;

    public static SummaryFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary_fragment, container, false);
        return view;
    }
}
