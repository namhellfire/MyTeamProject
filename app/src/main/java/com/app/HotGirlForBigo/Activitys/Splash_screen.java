package com.app.hotgirlforbigo.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.hotgirlforbigo.API.ConstantAPI;
import com.app.hotgirlforbigo.API.ListManager;
import com.app.hotgirlforbigo.MainActivity;
import com.app.hotgirlforbigo.Model.ListAPI;
import com.app.hotgirlforbigo.Model.PreferenceShare;
import com.app.hotgirlforbigo.Model.ProfileOffline;
import com.app.hotgirlforbigo.R;
import com.app.hotgirlforbigo.Utils.UtilConnect;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Splash_screen extends AppCompatActivity {

    private final int SPLEEP = 2000;
    private ImageView imgSplashscreen;
    private ArrayList<ProfileOffline> profileOfflines;
    SharedPreferences appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Removes notification bar

        setContentView(R.layout.activity_splash_screen);

        profileOfflines = new ArrayList<>();

        if (isNetworkAvailable()) {
            ListAPIAsync apiAsync = new ListAPIAsync(this);
            apiAsync.execute(ConstantAPI.API_LISTAPI);

            ListAllAsync allAsync = new ListAllAsync(this);
            allAsync.execute(ConstantAPI.API_LIST_ALL);
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class ListAPIAsync extends AsyncTask<String, String, String> {

        Activity activity;

        public ListAPIAsync(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return UtilConnect.getAPI(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<ListAPI> listAPIs = new ArrayList<>();
            if (s != null) {
                listAPIs = UtilConnect.ParseListAPI(s);
            }

            PreferenceShare share = new PreferenceShare(activity);
            share.setPreferenceBooleanValue(MainActivity.FIRST_RUN,true);

            Handler handler = new Handler();
            final ArrayList<ListAPI> finalListAPIs = listAPIs;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putParcelableArrayListExtra(ListManager.LIST_API, finalListAPIs);
                    startActivity(intent);
                    activity.finish();
                }
            }, 2000);


        }
    }


    class ListAllAsync extends AsyncTask<String, String, String> {

        Activity activity;

        public ListAllAsync(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return UtilConnect.getAPI(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                ArrayList<ProfileOffline> profileOfflines = new ArrayList<>();
                try {
                    profileOfflines = UtilConnect.ParseJsonOffline(new JSONArray(s));
                    if (appSharedPrefs == null) {
                        appSharedPrefs = PreferenceManager
                                .getDefaultSharedPreferences(activity);
                    }
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(profileOfflines);
                    prefsEditor.putString(ListManager.LIST_ALL, json);
                    prefsEditor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}

