package storemanager.com.app.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import storemanager.com.app.R;
import storemanager.com.app.models.User;

public class TeamFragmentAdapter extends RecyclerView.Adapter<TeamFragmentAdapter.ViewHolder> {

    List<User> usersList;

    public TeamFragmentAdapter(List<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public TeamFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_team_card, parent, false);
        TeamFragmentAdapter.ViewHolder vh = new TeamFragmentAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.status.setText(user.getStatus());
        holder.date.setText(user.getRegistrationDate());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        public ImageView avatar;
        public TextView name;
        public TextView email;
        public TextView status;
        public TextView date;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            name = (TextView) v.findViewById(R.id.member_name);
            email = (TextView) v.findViewById(R.id.mamber_email);
            status = (TextView) v.findViewById(R.id.member_status);
            date = (TextView) v.findViewById(R.id.member_create_date);
            avatar = (ImageView) v.findViewById(R.id.member_image);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
