package com.app.hotgirlforbigo.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.hotgirlforbigo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by hai.tran on 7/6/2016.
 */
public class InternetImageView extends ImageView {

    private Bitmap bitmap;
    private ProgressBar mProgressBar;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public InternetImageView(Context context) {
        super(context);
    }

    public InternetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InternetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public InternetImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * loadImageWithUrl has a Circle ProgressBar
     *
     * @param url
     * @param context
     * @param progressBar
     */
    public void loadImageWithUrl(final String url, Context context, ProgressBar progressBar) {
        if (progressBar != null) {
            this.mProgressBar = progressBar;
            this.mProgressBar.setVisibility(VISIBLE);
        }
        loadImageWithUrl(url, context);
    }

    /**
     * loadImageWithUrl has no Circle ProgressBar
     *
     * @param url
     * @param context
     */
    public void loadImageWithUrl(final String url, Context context) {
        setScaleType(ScaleType.FIT_CENTER);

        Glide.with(context).load(url).centerCrop().crossFade().error(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(GONE);
                }

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(GONE);
                }
                return false;

            }
        }).into(this);
    }
}