package com.app.Bigo.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.Bigo.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nguyennam on 2/13/17.
 */

public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.ViewHolder> {

    private ArrayList<String> mDataSet;

    public OfflineAdapter(ArrayList<String> mDataSet) {
        this.mDataSet = mDataSet;
    }

    public OfflineAdapter() {
        mDataSet = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            mDataSet.add("new title " + i);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvID);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relaLayoutAdapter);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_offline, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String title = mDataSet.get(position);
        final String src = "https://i.ytimg.com/vi/V3e_sq4sGME/hqdefault.jpg";
        ImageLoadTask imageLoadTask = new ImageLoadTask(src, holder.relativeLayout);
        imageLoadTask.execute();
        holder.tvTitle.setText(title);
//        holder.imgThumbnail.setImageBitmap(UtilConnect.getBitmapFromURL(src));
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class ImageLoadTask extends AsyncTask<Void, Bitmap, Bitmap> {

        private String url;
        private RelativeLayout relativeLayout;

        public ImageLoadTask(String url, RelativeLayout relativeLayout) {
            this.url = url;
            this.relativeLayout = relativeLayout;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            BitmapDrawable background = new BitmapDrawable(result);
            relativeLayout.setBackgroundDrawable(background);
        }

    }
}
