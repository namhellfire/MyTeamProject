package com.app.Bigo;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.app.Bigo.API.ConstantAPI;
import com.app.Bigo.API.ListManager;
import com.app.Bigo.AsyncTask.AsyncOffline;
import com.app.Bigo.Custom.DialogSendMail;
import com.app.Bigo.Fragments.MainFragment;
import com.app.Bigo.Fragments.Online_fragment;
import com.app.Bigo.Model.ListAPI;
import com.app.Bigo.Model.PreferenceShare;
import com.app.Bigo.Model.Profile;
import com.app.Bigo.Utils.Util;
import com.google.android.exoplayer2.C;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.ArrayList;

import static com.app.Bigo.R.menu.main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Online_fragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private AVLoadingIndicatorView avi;
    private RelativeLayout loading;
    private ArrayList<Profile> dataContent = new ArrayList<>();
    private AsyncOffline asyncOffline;

    public static final String TAG_TOP_FRAGMENT = "TopFragment";
    public static final String TAG_NEW_FRAGMENT = "NewFragment";
    public static final String TAG_ONLINE_FRAGMENT = "OnlineFragment";
    public static final String TAG_HOME_FRAGMENT = "HomeFragment";

    public static final String FIRST_RUN = "FIRST_RUN";
    public static String CURRENT_TAG = TAG_HOME_FRAGMENT;
    public static int navItemSelected = 0;
    public static String ApiUrl = ConstantAPI.API_LIST_ALL;
    public static boolean isOnline = false;

    private String[] ActivityTitle;
    private Handler mHandler;
    private Intent mIntent;

    PreferenceShare share;

    public static ArrayList<ListAPI> listAPIs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable dr = getDrawable(R.drawable.side_nav_bar);
        toolbar.setBackground(dr);
        setSupportActionBar(toolbar);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(R.color.green);

        share = new PreferenceShare(this);

        Log.d(TAG, "oncreate " + getString(R.string.app_id));

        MobileAds.initialize(getApplicationContext(), getString(R.string.app_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mIntent = getIntent();
        listAPIs = mIntent.getParcelableArrayListExtra(ListManager.LIST_API);

        ActivityTitle = getResources().getStringArray(R.array.nav_item_activity_title);
        mHandler = new Handler();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loading = (RelativeLayout) findViewById(R.id.loading);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        stopAnim();
        asyncOffline = new AsyncOffline(this, avi);
//        stopAnim();
        if (share.getPreferenceBooleanValue(FIRST_RUN)) {
            asyncOffline.execute(ApiUrl);
            share.setPreferenceBooleanValue(FIRST_RUN, false);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isOnline) {
            isOnline = false;
            asyncOffline.execute(ApiUrl);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orrentation = newConfig.orientation;
        switch (orrentation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d(TAG, "orrentation : " + orrentation);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d(TAG, "orrentation : " + orrentation);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
//            case R.id.action_settings:
//                break;
            case R.id.action_search:
                return true;
            case R.id.action_reload:
                reLoadPage();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reLoadPage() {

        if (!isOnline && CURRENT_TAG.equalsIgnoreCase(TAG_ONLINE_FRAGMENT)){

        }else {
            loadDataContent();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navItemSelected = 0;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navItemSelected = 0;
            CURRENT_TAG = TAG_HOME_FRAGMENT;

        } else if (id == R.id.nav_new) {
            navItemSelected = 1;
            CURRENT_TAG = TAG_NEW_FRAGMENT;

        } else if (id == R.id.nav_top) {
            navItemSelected = 2;
            CURRENT_TAG = TAG_TOP_FRAGMENT;

        } else if (id == R.id.nav_online) {
            navItemSelected = 3;
            CURRENT_TAG = TAG_ONLINE_FRAGMENT;

        } else if (id == R.id.nav_facebook) {
            Intent intent = Util.getOpenFacebookIntent(getApplicationContext());
            startActivity(intent);
        } else if (id == R.id.nav_youtube) {
            Intent intent = Util.getOpenYoutubeIntent(getApplicationContext());
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_rate) {
            Intent intent = Util.getOpenRateIntent(getApplicationContext());
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        } else if (id == R.id.nav_contact) {
            DialogSendMail dialogSendMail = new DialogSendMail(this);
            dialogSendMail.show();
        }

        if (!CURRENT_TAG.equalsIgnoreCase(TAG_ONLINE_FRAGMENT)) {
            isOnline = false;
        }
        ApiUrl = setAPiUrl(navItemSelected);
        Log.d(TAG, "item select navibar " + CURRENT_TAG + " navibar selected " + navItemSelected);
        if (CURRENT_TAG.equalsIgnoreCase(TAG_ONLINE_FRAGMENT)) {
            loadPageOnlineFragment();
        } else {
            loadDataContent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void loadPageFragment(final ArrayList<Profile> arrayList) {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            Log.d(TAG, "fragment not null");
//            return;
        }
        dataContent = arrayList;
        Runnable mPendingRunable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getPageFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ListManager.DATA_CONTENT, dataContent);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        if (mPendingRunable != null) {
            mHandler.post(mPendingRunable);
        }

        selectNavMenu();
        setTitleToolbar();

    }

    public void loadPageOnlineFragment() {
        Runnable mPendingRunable = new Runnable() {
            @Override
            public void run() {
                Online_fragment onlineFragment = new Online_fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ListManager.LIST_API, listAPIs);
                onlineFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, onlineFragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        if (mPendingRunable != null) {
            mHandler.post(mPendingRunable);
        }

        selectNavMenu();
        setTitleToolbar();

    }


    public Fragment getPageFragment() {
//        switch (navItemSelected) {
//            case 0:
////                Home_fragment homeFragment = new Home_fragment();
////                return homeFragment;
//            case 1:
////                New_fragment newFragment = new New_fragment();
////                return newFragment;
//            case 2:
////                Top_Fragment topFragment = new Top_Fragment();
////                return topFragment;
//                MainFragment mainFragment = new MainFragment();
//                return mainFragment;
//            case 3:
////                Intent intent = new Intent(this, PlayerActivity.class);
////                startActivity(intent);
////                Online_fragment onlineFragment = new Online_fragment();
////                Bundle bundle = new Bundle();
////                bundle.putParcelableArrayList(ListManager.LIST_API, listAPIs);
////                onlineFragment.setArguments(bundle);
////                return onlineFragment;
//                MainFragment mainFragment = new MainFragment();
//                return mainFragment;
//            default:
                return new MainFragment();
//        }
    }

    public void LoadListOnline(String country) {
        Runnable mPendingRunable = new Runnable() {
            @Override
            public void run() {
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, mainFragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        if (mPendingRunable != null) {
            mHandler.post(mPendingRunable);
        }
        getSupportActionBar().setTitle(country);
    }

    public void loadDataContent() {
        Log.d(TAG, "link API : " + ApiUrl);
        if (asyncOffline.isCancelled()) {
            asyncOffline = null;
            asyncOffline = new AsyncOffline(this, avi);
            asyncOffline.execute(MainActivity.ApiUrl);
        } else {
            asyncOffline.cancel(true);
            asyncOffline = null;
            asyncOffline = new AsyncOffline(this, avi);
            asyncOffline.execute(MainActivity.ApiUrl);
        }
    }

    public void selectNavMenu() {
        navigationView.getMenu().getItem(navItemSelected).setChecked(true);
    }

    public void setTitleToolbar() {
        getSupportActionBar().setTitle(ActivityTitle[navItemSelected]);
//        getActionBar().setTitle(ActivityTitle[navItemSelected]);
    }

    public String setAPiUrl(int type) {
        switch (type) {
            case 0:
                return ConstantAPI.API_LIST_ALL;
            case 1:
                return ConstantAPI.API_LIST_NEW;
            case 2:
                return ConstantAPI.API_LIST_TOP;
            case 3:
                return ConstantAPI.API_LISTAPI;
            default:
                return ConstantAPI.API_LIST_TOP;
        }
    }

    void startAnim() {
        avi.show();
//        loading.setVisibility(View.VISIBLE);
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
//        loading.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


}
