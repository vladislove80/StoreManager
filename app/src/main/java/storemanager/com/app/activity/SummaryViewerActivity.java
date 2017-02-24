package storemanager.com.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import storemanager.com.app.R;
import storemanager.com.app.models.Summary;
import storemanager.com.app.utils.Utils;

public class SummaryViewerActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mListView;
    private List<Summary> summaryList;
    private List<Summary> summaryListWithFilter;
    private TextView label;
    private SummaryViewerAdapter adapter;
    private ProgressBar progressBar;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_summary_viewer);
        Log.v(Utils.LOG_TAG, "SummaryViewerActivity");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // полная очистка настроек
        // sp.edit().clear().commit();

        mListView = (ListView) findViewById(R.id.full_summury);
        label = (TextView) findViewById(R.id.viewer_label);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        summaryList = new ArrayList<>();
        summaryListWithFilter = new ArrayList<>();
        adapter = new SummaryViewerAdapter(getBaseContext(), summaryList);
        mListView.setAdapter(adapter);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(Utils.LOG_TAG ,"dataSnapshot.getChildrenCount: " + dataSnapshot.getChildrenCount());
                for (String shop : Utils.cShops) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.hasChild(shop)) {
                            Summary summary = postSnapshot.child(shop).getValue(Summary.class);
                            if (summary != null) {
                                summary.setShop(shop);
                                summaryList.add(summary);
                            }
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(Utils.LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getBaseContext(), "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        };
        //mDatabase.addValueEventListener(postListener);
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

    @Override
    protected void onResume() {
        String filter = sp.getString("filter", "all");
        summaryListWithFilter = new ArrayList<>();
        summaryListWithFilter.addAll(applyFilterToList(summaryList, filter));
        //summaryListWithFilter.addAll(summaryList);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.filter_settings:
                Toast.makeText(this, "FILTER", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, FilterPreferenceActivity.class);
                startActivity(intent);
                return true;
            case R.id.export_settings:
                Toast.makeText(this, "EXPORT", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Summary> applyFilterToList(List<Summary> summaryListWithoutFilter, String filter) {
        List<Summary> summaryListWithFilter = new ArrayList<>();
        //String[] filterValues = getResources().getStringArray(R.array.filter_values);
        switch (filter) {
            case "today" : //applyTodayFilter(summaryListWithoutFilter);
                break;
            /*case "tomorrow" : applyTomorrowFilter(summaryListWithoutFilter);
                break;*/
            case "all" : summaryListWithFilter.addAll(summaryListWithoutFilter);
                break;
        }
        return summaryListWithFilter;
    }

    private List<Summary> applyTomorrowFilter(List<Summary> summaryListWithoutFilter) {
        List<Summary> summaryListWithFilter = new ArrayList<>();
        for (Summary summary : summaryListWithoutFilter) {

        }
        return summaryListWithFilter;
    }

    private List<Summary> applyTodayFilter(List<Summary> summaryListWithoutFilter) {
        List<Summary> summaryListWithFilter = new ArrayList<>();
        String date = Utils.getCurrentDate();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //cal1.setTime(date);
        //cal2.setTime(date2);
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy");

        for (Summary summary : summaryListWithoutFilter) {
            Date d = null;
            try {
                d = format.parse(summary.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal2.setTime(d);
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (summary.getDate().equals(date)) {
                summaryListWithFilter.add(summary);
            }
        }
        return summaryListWithFilter;
    }
}
