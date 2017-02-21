package com.app.Bigo.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.app.Bigo.Adapter.OfflineAdapter;
import com.app.Bigo.Model.Profile;
import com.app.Bigo.R;
import com.app.Bigo.Utils.UtilConnect;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by nguyennam on 2/15/17.
 */

public class AsyncOffline extends AsyncTask<String, String, ArrayList<Profile>> {
    private RecyclerView recyclerView;
    private OfflineAdapter offlineAdapter;
    private Activity activity;

    public AsyncOffline(Activity activity, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.activity = activity;
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
            offlineAdapter = new OfflineAdapter(arrayList, activity);
            recyclerView.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(offlineAdapter);
            recyclerView.setAdapter(alphaInAnimationAdapter);
        } else {
            Toast.makeText(activity, R.string.check_network, Toast.LENGTH_SHORT).show();
        }
    }
}
