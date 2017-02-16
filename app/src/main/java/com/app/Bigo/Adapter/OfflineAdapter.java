package com.app.Bigo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.Bigo.Model.ProfileOffline;
import com.app.Bigo.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by nguyennam on 2/13/17.
 */

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.ViewHolder> {

    private ArrayList<String> mDataSet;
    private ArrayList<ProfileOffline> offlineArrayList = new ArrayList<>();
    private Activity activity;


    public OfflineAdapter(ArrayList<ProfileOffline> arrayList, Activity activity) {
        this.offlineArrayList = arrayList;
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
        public TextView tvView;
        public ImageView imgThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvName);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relaLayoutAdapter);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imgThumbnail);
            imgThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
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
        Glide.with(activity).load(offlineArrayList.get(position).getThumbnail()).into(holder.imgThumbnail);
        holder.tvTitle.setText(offlineArrayList.get(position).getName());
        holder.tvView.setText(String.valueOf(offlineArrayList.get(position).getView()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(offlineArrayList.get(position).getUrl())));
            }
        });
//        holder.imgThumbnail.setImageBitmap(UtilConnect.getBitmapFromURL(src));
    }


    @Override
    public int getItemCount() {
        return offlineArrayList.size();
    }
}
