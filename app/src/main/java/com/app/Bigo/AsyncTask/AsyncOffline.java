package com.app.Bigo.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import com.app.Bigo.Adapter.OfflineAdapter;
import com.app.Bigo.Adapter.SpacesItemDecoration;
import com.app.Bigo.Model.ProfileOffline;
import com.app.Bigo.Utils.UtilConnect;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by nguyennam on 2/15/17.
 */

public class AsyncOffline extends AsyncTask<String, String, ArrayList<ProfileOffline>> {
    private RecyclerView recyclerView;
    private OfflineAdapter offlineAdapter;
    private Activity activity;

    public AsyncOffline(Activity activity, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    protected ArrayList<ProfileOffline> doInBackground(String... strings) {
        ArrayList<ProfileOffline> offlineArrayList = new ArrayList<>();
        try {
            String result = UtilConnect.getAPI(strings[0]);
            offlineArrayList = UtilConnect.ParseJsonOffline(new JSONArray(result));
            return offlineArrayList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ProfileOffline> arrayList) {
        super.onPostExecute(arrayList);
        offlineAdapter = new OfflineAdapter(arrayList,activity);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(offlineAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setAdapter(alphaInAnimationAdapter);
    }
}
