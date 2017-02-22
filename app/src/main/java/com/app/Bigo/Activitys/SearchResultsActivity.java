package com.app.Bigo.Activitys;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.OvershootInterpolator;

import com.app.Bigo.API.ListManager;
import com.app.Bigo.Adapter.OfflineAdapter;
import com.app.Bigo.Adapter.SpacesItemDecoration;
import com.app.Bigo.Model.Profile;
import com.app.Bigo.Model.ProfileOffline;
import com.app.Bigo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

public class SearchResultsActivity extends AppCompatActivity {

    private final String TAG = "SearchResultsActivity";

    private RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private OfflineAdapter offlineAdapter;

    private ArrayList<ProfileOffline> profileOfflines;
    private ArrayList<Profile> listSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ActionBar actionBar = getSupportActionBar();
        Drawable dr = getDrawable(R.drawable.side_nav_bar);
        actionBar.setBackgroundDrawable(dr);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.lvOffline);

        profileOfflines = new ArrayList<>();


        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(ListManager.LIST_ALL, "");

        Type type = new TypeToken<ArrayList<ProfileOffline>>() {
        }.getType();
        profileOfflines = gson.fromJson(json, type);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "query search = " + query);
            listSearch = new ArrayList<>();

            for (int i = 0; i < profileOfflines.size(); i++) {
                ProfileOffline profileOffline = profileOfflines.get(i);
                String name = profileOffline.getName();
                String desc = profileOffline.getDescription();
                String tag = profileOffline.getTag();
                String view = profileOffline.getView();
                String thumbnail = profileOffline.getThumbnail();
                String url = profileOffline.getUrl();
                if (name.contains(query) || desc.contains(query) || tag.contains(query)) {
                    Profile profile = new Profile();
                    profile.setView(view);
                    profile.setThumbnail(thumbnail);
                    profile.setStatus(desc);
                    profile.setName(name);
                    profile.setUrl(url);

                    listSearch.add(profile);
                }
            }

            if (listSearch != null && listSearch.size() > 0) {
                offlineAdapter = new OfflineAdapter(listSearch, this);
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
                AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(offlineAdapter);
                recyclerView.addItemDecoration(new SpacesItemDecoration(10));
                recyclerView.setAdapter(alphaInAnimationAdapter);
            } else {

            }

        }

    }

}
