package com.app.Bigo.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nguyennam on 2/11/17.
 */

public class ProfileOnline implements Parcelable {

    public static final String BIG_IMG = "big_img";
    public static final String MEDIUM_IMG = "medium_img";
    public static final String SMALL_IMG = "small_img";
    public static final String USER_COUNT = "user_count";
    public static final String COUNTRY = "country";
    public static final String NICK_NAME = "nick_name";
    public static final String ROOM_TOPIC = "room_topic";
    public static final String STATUS = "status";
    public static final String LIVE_URL = "live_url";


    private String medium_img;
    private String small_img;
    private int user_count;
    private String country;
    private String nick_name;
    private String room_topic;
    private String status;
    private String live_url;


    private String big_img;

    public String getBig_img() {
        return big_img;
    }

    public void setBig_img(String big_img) {
        this.big_img = big_img;
    }

    public String getMedium_img() {
        return medium_img;
    }

    public void setMedium_img(String medium_img) {
        this.medium_img = medium_img;
    }

    public String getSmall_img() {
        return small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getRoom_topic() {
        return room_topic;
    }

    public void setRoom_topic(String room_topic) {
        this.room_topic = room_topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLive_url() {
        return live_url;
    }

    public void setLive_url(String live_url) {
        this.live_url = live_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.medium_img);
        dest.writeString(this.small_img);
        dest.writeInt(this.user_count);
        dest.writeString(this.country);
        dest.writeString(this.nick_name);
        dest.writeString(this.room_topic);
        dest.writeString(this.status);
        dest.writeString(this.live_url);
        dest.writeString(this.big_img);
    }

    public ProfileOnline() {
    }

    protected ProfileOnline(Parcel in) {
        this.medium_img = in.readString();
        this.small_img = in.readString();
        this.user_count = in.readInt();
        this.country = in.readString();
        this.nick_name = in.readString();
        this.room_topic = in.readString();
        this.status = in.readString();
        this.live_url = in.readString();
        this.big_img = in.readString();
    }

    public static final Parcelable.Creator<ProfileOnline> CREATOR = new Parcelable.Creator<ProfileOnline>() {
        @Override
        public ProfileOnline createFromParcel(Parcel source) {
            return new ProfileOnline(source);
        }

        @Override
        public ProfileOnline[] newArray(int size) {
            return new ProfileOnline[size];
        }
    };
}
