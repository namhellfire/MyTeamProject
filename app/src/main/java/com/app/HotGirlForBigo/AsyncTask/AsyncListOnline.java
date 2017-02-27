package com.app.hotgirlforbigo.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.hotgirlforbigo.MainActivity;
import com.app.hotgirlforbigo.Model.ListAPI;
import com.app.hotgirlforbigo.R;
import com.app.hotgirlforbigo.Utils.UtilConnect;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nguyennam on 2/18/17.
 */

public class AsyncListOnline extends AsyncTask<String, String, String> {
    Activity activity;
    private AVLoadingIndicatorView avi;

    public AsyncListOnline(Activity activity, AVLoadingIndicatorView avi) {
        this.activity = activity;
        this.avi = avi;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        avi.show();
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
            MainActivity ac = (MainActivity) activity;
            ac.loadPageOnlineFragment(listAPIs);
        } else {
            Toast.makeText(activity, activity.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
        }
        avi.hide();
    }
}
