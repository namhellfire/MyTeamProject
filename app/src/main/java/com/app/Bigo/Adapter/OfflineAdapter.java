package com.app.Bigo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.Bigo.API.ConstantAPI;
import com.app.Bigo.Activitys.PlayerActivity;
import com.app.Bigo.Model.Profile;
import com.app.Bigo.R;
import com.app.Bigo.Utils.Icon_Manager;
import com.app.Bigo.Utils.UtilConnect;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nguyennam on 2/13/17.
 */

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.ViewHolder> {

    private ArrayList<String> mDataSet;
    private ArrayList<Profile> profileArrayList = new ArrayList<>();
    private Activity activity;
    private Icon_Manager icon_manager = new Icon_Manager();


    public OfflineAdapter(ArrayList<Profile> arrayList, Activity activity) {
        this.profileArrayList = arrayList;
        this.activity = activity;
    }


    public OfflineAdapter() {
        mDataSet = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            mDataSet.add("new title " + i);
        }

    }

    public void remove(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String text, int position) {
        mDataSet.add(position, text);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public RelativeLayout relativeLayout;
        public CardView cardView;
        public TextView tvView, iconView;
        public ImageView imgThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvName);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relaLayoutAdapter);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imgThumbnail);
            iconView = (TextView) itemView.findViewById(R.id.iconView);
            iconView.setTypeface(icon_manager.get_icons("fonts/ionicons.ttf", activity));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_offline, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        final String title = mDataSet.get(position);
//        final String src = "https://i.ytimg.com/vi/V3e_sq4sGME/hqdefault.jpg";
//        ImageLoadTask imageLoadTask = new ImageLoadTask(offlineArrayList.get(position).getThumbnail(), holder.cardView);
//        imageLoadTask.execute();
        Glide.with(activity).load(profileArrayList.get(position).getThumbnail()).error(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).into(holder.imgThumbnail);
        holder.tvTitle.setText(Html.fromHtml(profileArrayList.get(position).getName()));
        holder.tvView.setText(String.valueOf(profileArrayList.get(position).getView()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = profileArrayList.get(position).getUrl();
                boolean isLive = !url.contains(ConstantAPI.YOUTUBE);
                if (isLive) {
                    AsyncGetLink asyncGetLink = new AsyncGetLink();
                    asyncGetLink.execute(profileArrayList.get(position).getUrl());
                } else {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(profileArrayList.get(position).getUrl())));
                }
            }
        });
//        holder.imgThumbnail.setImageBitmap(UtilConnect.getBitmapFromURL(src));
    }


    @Override
    public int getItemCount() {
        if (profileArrayList.size() > 50){
            return 50;
        }
        return profileArrayList.size();
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
                intent.putExtra(Profile.LIVE_URL, s);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, activity.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
