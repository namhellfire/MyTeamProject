package com.app.Bigo.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nguyennam on 2/12/17.
 */

public class ListAPI implements Parcelable {
    private String uri;
    private String md5;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uri);
        dest.writeString(this.md5);
    }

    public ListAPI() {
    }

    protected ListAPI(Parcel in) {
        this.uri = in.readString();
        this.md5 = in.readString();
    }

    public static final Parcelable.Creator<ListAPI> CREATOR = new Parcelable.Creator<ListAPI>() {
        @Override
        public ListAPI createFromParcel(Parcel source) {
            return new ListAPI(source);
        }

        @Override
        public ListAPI[] newArray(int size) {
            return new ListAPI[size];
        }
    };
}
