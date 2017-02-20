package com.app.Bigo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.Bigo.Activitys.PlayerActivity;
import com.app.Bigo.Model.Profile;
import com.app.Bigo.Model.ProfileOnline;
import com.app.Bigo.R;
import com.app.Bigo.Utils.UtilConnect;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nguyennam on 2/18/17.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<ProfileOnline> onlineArrayList;

    public CountryAdapter(Activity activity, ArrayList<ProfileOnline> onlineArrayList) {
        this.activity = activity;
        this.onlineArrayList = onlineArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_video_country, parent, false);
        CountryAdapter.ViewHolder holder = new CountryAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(activity).load(onlineArrayList.get(position).getBig_img()).error(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).into(holder.imgItemCoutry);
        holder.imgItemCoutry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncGetLink asyncGetLink = new AsyncGetLink();
                asyncGetLink.execute(onlineArrayList.get(position).getLive_url());
            }
        });
    }

    @Override
    public int getItemCount() {

        if (onlineArrayList.size() < 7) {
            return onlineArrayList.size();
        }

        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItemCoutry;

        public ViewHolder(View itemView) {
            super(itemView);
            imgItemCoutry = (ImageView) itemView.findViewById(R.id.imgItemCountry);
            imgItemCoutry.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }


    public class AsyncGetLink extends AsyncTask<String, String, String> {

        public AsyncGetLink() {
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
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Profile.LIVE_URL,s);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
