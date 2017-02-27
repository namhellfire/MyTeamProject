package com.app.hotgirlforbigo.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.app.hotgirlforbigo.Adapter.OfflineAdapter;
import com.app.hotgirlforbigo.MainActivity;
import com.app.hotgirlforbigo.Model.Profile;
import com.app.hotgirlforbigo.R;
import com.app.hotgirlforbigo.Utils.UtilConnect;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nguyennam on 2/15/17.
 */

public class AsyncOffline extends AsyncTask<String, String, ArrayList<Profile>> {
    private RecyclerView recyclerView;
    private OfflineAdapter offlineAdapter;
    private Activity activity;
    private AVLoadingIndicatorView avi ;

    public AsyncOffline(Activity activity, AVLoadingIndicatorView avi) {
        this.activity = activity;
        this.avi = avi;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        avi.show();
    }

    @Override
    protected ArrayList<Profile> doInBackground(String... strings) {
        ArrayList<Profile> profileArrayList = new ArrayList<>();
        try {
            String result = UtilConnect.getAPI(strings[0]);
            profileArrayList = UtilConnect.ParseJsonProfile(new JSONArray(result));
            Collections.shuffle(profileArrayList);
            return profileArrayList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Profile> arrayList) {
        super.onPostExecute(arrayList);
        if (arrayList != null && arrayList.size() > 0) {
            MainActivity mainActivity = (MainActivity)activity;
            mainActivity.loadPageFragment(arrayList);
        } else {
            Toast.makeText(activity, R.string.check_network, Toast.LENGTH_SHORT).show();
        }
        avi.hide();
    }
}
