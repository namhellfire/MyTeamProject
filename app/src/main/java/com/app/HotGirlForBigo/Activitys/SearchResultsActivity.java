package com.app.hotgirlforbigo.Activitys;

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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.app.hotgirlforbigo.API.ListManager;
import com.app.hotgirlforbigo.Adapter.OfflineAdapter;
import com.app.hotgirlforbigo.Adapter.SpacesItemDecoration;
import com.app.hotgirlforbigo.Model.Profile;
import com.app.hotgirlforbigo.Model.ProfileOffline;
import com.app.hotgirlforbigo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
    private GridLayoutManager gridLayoutManager;

    private ArrayList<ProfileOffline> profileOfflines;
    private ArrayList<Profile> listSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        final ActionBar actionBar = getSupportActionBar();
        Drawable dr = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            dr = getDrawable(R.drawable.side_nav_bar);
        }
        actionBar.setBackgroundDrawable(dr);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.lvOffline);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final int currentFirstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                final int currentLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
//                    getActivity().getSupportActionBar().hide();
                    actionBar.hide();
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
//                    getActivity().getSupportActionBar().show();
                    actionBar.show();
                }

                if (currentLastVisibleItem == (offlineAdapter.getItemCount()-1)){
                    recyclerView.setPadding(0,0,0,150);
                    recyclerView.requestLayout();
                }else if (currentLastVisibleItem == (offlineAdapter.getItemCount()-3)){
                    recyclerView.setPadding(0,0,0,0);
                    recyclerView.requestLayout();
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;

            }
        });

        profileOfflines = new ArrayList<>();


        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(ListManager.LIST_ALL, "");

        Type type = new TypeToken<ArrayList<ProfileOffline>>() {
        }.getType();
        profileOfflines = gson.fromJson(json, type);

        MobileAds.initialize(getApplicationContext(), getString(R.string.app_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setVisibility(View.GONE);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (intent == null || intent.getStringExtra(SearchManager.QUERY) == null || intent.getStringExtra(SearchManager.QUERY).trim().isEmpty()){
            return;
        }

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
            gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(offlineAdapter);
            recyclerView.addItemDecoration(new SpacesItemDecoration(10));
            recyclerView.setAdapter(alphaInAnimationAdapter);
        } else {

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("back", "backsss");
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
