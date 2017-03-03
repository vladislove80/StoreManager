package storemanager.com.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.CoffeItem;

public class MenuFragmentAdapter extends RecyclerView.Adapter<MenuFragmentAdapter.ViewHolder> {

    private static Context context;
    private List<CoffeItem> mDataset;

    public MenuFragmentAdapter(Context context, List<CoffeItem> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
