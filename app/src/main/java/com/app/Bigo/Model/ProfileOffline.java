package com.app.Bigo.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nguyennam on 2/11/17.
 */

public class ProfileOffline implements Parcelable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AUTHOR = "author";
    public static final String URL = "url";
    public static final String DESCRIPTION = "description";
    public static final String THUMBNAIL = "thumbnail";
    public static final String VIEW = "view";
    public static final String LIKE = "like";
    public static final String DISLIKE = "dislike";
    public static final String COMMENT = "comment";
    public static final String PUBLISH = "publish";
    public static final String TIME = "time";
    public static final String LENGTH = "length";
    public static final String TYPE = "type";
    public static final String TAG = "tag";

    private int id;
    private String name;
    private String author;
    private String url;
    private String description;
    private String thumbnail;
    private String view;
    private int like;
    private int dislike;
    private int comment;
    private int publish;
    private String time;
    private String length;
    private int type;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getPublish() {
        return publish;
    }

    public void setPublish(int publish) {
        this.publish = publish;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeString(this.thumbnail);
        dest.writeString(this.view);
        dest.writeInt(this.like);
        dest.writeInt(this.dislike);
        dest.writeInt(this.comment);
        dest.writeInt(this.publish);
        dest.writeString(this.time);
        dest.writeString(this.length);
        dest.writeInt(this.type);
        dest.writeString(this.tag);
    }

    public ProfileOffline() {
    }

    protected ProfileOffline(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.author = in.readString();
        this.url = in.readString();
        this.description = in.readString();
        this.thumbnail = in.readString();
        this.view = in.readString();
        this.like = in.readInt();
        this.dislike = in.readInt();
        this.comment = in.readInt();
        this.publish = in.readInt();
        this.time = in.readString();
        this.length = in.readString();
        this.type = in.readInt();
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<ProfileOffline> CREATOR = new Parcelable.Creator<ProfileOffline>() {
        @Override
        public ProfileOffline createFromParcel(Parcel source) {
            return new ProfileOffline(source);
        }

        @Override
        public ProfileOffline[] newArray(int size) {
            return new ProfileOffline[size];
        }
    };
}
