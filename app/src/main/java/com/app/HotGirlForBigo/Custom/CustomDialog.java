/**
 * Copyright (C) 2014 Orgit - All Rights Reserved
 */
package com.app.hotgirlforbigo.Custom;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.hotgirlforbigo.Utils.Util;

public abstract class CustomDialog extends Dialog {
    protected LayoutInflater mInflater;
    protected Context mContext;

    public CustomDialog(Context context) {
        super(context);
        mContext = context;
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setCanceledOnTouchOutside(true);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupDialogSize();
        setupAnimation();
    }

    /**
     * setup dialog size
     **/
    protected void setupDialogSize() {
        int screenW = Util.getScreenWidth(getContext());
        int screenH = Util.getScreenHeight(getContext());
        int width = screenW < screenH ? screenW : screenH;
        int height = screenW < screenH ? screenH : WindowManager.LayoutParams.WRAP_CONTENT;
        width *= 0.9;
        if (isLarge(getContext())) {
            width *= 0.8;
        } else if (isXLarge(getContext())) {
            width *= 0.75;
        }
        setDialogWidth(width, width);
    }

    private boolean isLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private boolean isXLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4;
    }

    protected void setupAnimation() {

    }

    /**
     * setup dialog width
     *
     * @param width
     */
    protected void setDialogWidth(int height, int width) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        getWindow().setAttributes(params);
    }

    /**
     * setup dialog height
     */
    protected void setDialogHeight(int height) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = height;
        getWindow().setAttributes(params);
    }

    protected void showToast(String msg) {
        if (null != msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
