package com.app.Bigo.Adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.app.Bigo.MainActivity;
import com.app.Bigo.Model.CountryApi;
import com.app.Bigo.Model.ProfileOnline;
import com.app.Bigo.R;
import com.app.Bigo.Utils.Icon_Manager;
import com.app.Bigo.Utils.UtilConnect;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by nguyennam on 2/13/17.
 */

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {

    private final String TAG = "OnlineAdapter";

    private Icon_Manager icon_manager;
    private LinearLayoutManager linearLayoutManager;
    private Activity activity;
    private ArrayList<CountryApi> mData = new ArrayList<>();

    public OnlineAdapter(Activity activity, ArrayList<CountryApi> mData) {
        this.activity = activity;
        icon_manager = new Icon_Manager();
        this.mData = mData;
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_online, parent, false);
        OnlineAdapter.ViewHolder holder = new OnlineAdapter.ViewHolder(v);
        AsyncOnline asyncOnline = new AsyncOnline(holder);
        asyncOnline.execute(mData.get(viewType).getAPIUrl());
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvCountry.setText((mData.get(position).getCountry()));
        holder.tvArrowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isOnline = true;
                MainActivity.ApiUrl = mData.get(position).getAPIUrl();
                MainActivity ac = (MainActivity) activity;
                ac.LoadListOnline(mData.get(position).getCountry());
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCountry;
        public TextView tvArrowMore;
        public RecyclerView lvVideoCountry;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvArrowMore = (TextView) itemView.findViewById(R.id.tvArrowMore);
            lvVideoCountry = (RecyclerView) itemView.findViewById(R.id.lvVideoCountry);
            tvArrowMore.setTypeface(icon_manager.get_icons("fonts/ionicons.ttf", activity));
        }
    }

    public class AsyncOnline extends AsyncTask<String, String, String> {
        private ViewHolder holder;

        public AsyncOnline(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = UtilConnect.getAPI(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                ArrayList<ProfileOnline> arrayList = new ArrayList<>();
                try {
                    arrayList = UtilConnect.ParseJsonOnline(new JSONArray(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CountryAdapter countryAdapter = new CountryAdapter(activity, arrayList);
                linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                holder.lvVideoCountry.setLayoutManager(linearLayoutManager);
                holder.lvVideoCountry.setItemAnimator(new SlideInDownAnimator(new OvershootInterpolator(1f)));
                AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(countryAdapter);
                holder.lvVideoCountry.addItemDecoration(new SpacesItemDecoration(10));
                holder.lvVideoCountry.setAdapter(alphaInAnimationAdapter);
            } else {
                Toast.makeText(activity, activity.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
