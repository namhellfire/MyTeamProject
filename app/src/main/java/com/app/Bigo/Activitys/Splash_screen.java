package com.app.Bigo.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.Bigo.API.ConstantAPI;
import com.app.Bigo.API.ListManager;
import com.app.Bigo.MainActivity;
import com.app.Bigo.Model.ListAPI;
import com.app.Bigo.R;
import com.app.Bigo.Utils.UtilConnect;

import java.io.IOException;
import java.util.ArrayList;

public class Splash_screen extends AppCompatActivity {

    private final int SPLEEP = 2000;
    private ImageView imgSplashscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Removes notification bar

        setContentView(R.layout.activity_splash_screen);

//        imgSplashscreen = (ImageView) findViewById(R.id.imgSplashscreen);
//        imgSplashscreen.setScaleType(ImageView.ScaleType.FIT_XY);

//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imgSplashscreen);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, SPLEEP);

        if (isNetworkAvailable()) {
            ListAPIAsync apiAsync = new ListAPIAsync(this);
            apiAsync.execute(ConstantAPI.API_LISTAPI);
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

}

