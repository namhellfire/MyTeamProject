package com.app.Bigo.Model;

/**
 * Created by nguyennam on 2/19/17.
 */

public class Profile {
    private String name;
    private String thumbnail;
    private String url;
    private String status;
    private String view;
    public static String LIVE_URL = "live_url";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }


}
