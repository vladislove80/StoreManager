package storemanager.com.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import storemanager.com.app.R;

public class ShopsFragment extends Fragment {
    public static final String TAG = ShopsFragment.class.getSimpleName();

    private int mPage;

    public static ShopsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(TAG, page);
        ShopsFragment fragment = new ShopsFragment();
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
        View view = inflater.inflate(R.layout.shops_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Fragment #" + mPage);
        return view;
    }
}
