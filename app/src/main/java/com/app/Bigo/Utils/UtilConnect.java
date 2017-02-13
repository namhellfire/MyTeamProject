package com.app.Bigo.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.app.Bigo.API.ConstantAPI;
import com.app.Bigo.API.DataResponse;
import com.app.Bigo.Model.ProfileOffline;
import com.app.Bigo.Model.ProfileOnline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nguyennam on 2/11/17.
 */

public class UtilConnect {

    public static DataResponse connectAPI(String link, HashMap<String, String> data) {
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(ConstantAPI.TIME_OUT);
            conn.setDoOutput(true);


            //get data from hashmap
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            Log.d("SERVER", "get DATA " + result.toString());

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(result.toString());

            writer.flush();
            writer.close();
            os.close();
            Log.d("SERVER", "post data ");


            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append((line));
            }
            bufferedReader.close();
            inputStream.close();
            conn.disconnect();
            Log.d("SERVER", "get sbbbb " + sb.toString());

            DataResponse dataRes = new DataResponse();

            JSONObject jsonObject = new JSONObject(sb.toString());
            try {
                String status = jsonObject.getString("status");
                dataRes.setStatusCode(Integer.parseInt(status));

            } catch (JSONException e) {
                dataRes.setStatusCode(-1); //-1 with error from server
            }

            try {
                String message = jsonObject.getString("message");
                dataRes.setMessage(message);
            } catch (JSONException e) {
                dataRes.setMessage("You have some problem. Please check again");
            }


            try {
                String response = jsonObject.getString("response");
                dataRes.setResponse(response);
            } catch (JSONException e) {
                dataRes.setResponse("");
            }

            //check method
//            if (link.equalsIgnoreCase(ConstantAPI.API_LOGOUT)){
//                dataRes.setMethod(ConstantAPI.METHOD_LOGOUT);
//            }else {
//                dataRes.setMethod("");
//            }
            return dataRes;

        } catch (Exception e) {
            Log.d("CALL", "get error code " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getAPI(String link) throws IOException {

        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(ConstantAPI.TIME_OUT);

        StringBuilder builder = new StringBuilder();

        InputStream content = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        Log.e("TAG", "result : " + builder.toString());
        urlConnection.disconnect();
        //saveData(context, builder.toString());
        return builder.toString();
    }

    public static String getAPI_v1(String link) throws IOException {

        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(ConstantAPI.TIME_OUT);

        StringBuilder builder = new StringBuilder();

        InputStream content = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        Log.e("TAG", "result : " + builder.toString());
        urlConnection.disconnect();

        try {
            JSONArray jsonArray = new JSONArray(builder.toString());
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.getString("live_url") != null) {
                    ParseJsonOnline(jsonArray);
                } else {
                    ParseJsonOffline(jsonArray);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static ArrayList<ProfileOnline> ParseJsonOnline(JSONArray array) {

        ArrayList<ProfileOnline> onlineArrayList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);

                ProfileOnline profileOnline = new ProfileOnline();
                profileOnline.setBig_img(object.getString(ProfileOnline.BIG_IMG));
                profileOnline.setMedium_img(object.getString(ProfileOnline.MEDIUM_IMG));
                profileOnline.setSmall_img(object.getString(ProfileOnline.SMALL_IMG));
                profileOnline.setUser_count(object.getInt(ProfileOnline.USER_COUNT));
                profileOnline.setCountry(object.getString(ProfileOnline.COUNTRY));
                profileOnline.setNick_name(object.getString(ProfileOnline.NICK_NAME));
                profileOnline.setRoom_topic(object.getString(ProfileOnline.ROOM_TOPIC));
                profileOnline.setStatus(object.getString(ProfileOnline.STATUS));
                profileOnline.setLive_url(object.getString(ProfileOnline.LIVE_URL));

                onlineArrayList.add(profileOnline);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (onlineArrayList.size() > 0 && onlineArrayList != null) {
            return onlineArrayList;
        }

        return null;
    }


    public static ArrayList<ProfileOffline> ParseJsonOffline(JSONArray array) {

        ArrayList<ProfileOffline> offlineArrayList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);

                ProfileOffline profileOffline = new ProfileOffline();
                profileOffline.setId(object.getInt(ProfileOffline.ID));
                profileOffline.setName(object.getString(ProfileOffline.NAME));
                profileOffline.setAuthor(object.getString(ProfileOffline.AUTHOR));
                profileOffline.setUrl(object.getString(ProfileOffline.URL));
                profileOffline.setDescription(object.getString(ProfileOffline.DESCRIPTION));
                profileOffline.setThumbnail(object.getString(ProfileOffline.THUMBNAIL));
                profileOffline.setView(object.getInt(ProfileOffline.VIEW));
                profileOffline.setLike(object.getInt(ProfileOffline.LIKE));
                profileOffline.setDislike(object.getInt(ProfileOffline.DISLIKE));
                profileOffline.setComment(object.getInt(ProfileOffline.COMMENT));
                profileOffline.setPublish(object.getInt(ProfileOffline.PUBLISH));
                profileOffline.setTime(object.getString(ProfileOffline.TIME));
                profileOffline.setLength(object.getString(ProfileOffline.LENGTH));
                profileOffline.setType(object.getInt(ProfileOffline.TYPE));
                profileOffline.setTag(object.getString(ProfileOffline.TAG));

                offlineArrayList.add(profileOffline);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (offlineArrayList != null && offlineArrayList.size() > 0) {
            return offlineArrayList;
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Bitmap, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
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
            imageView.setImageBitmap(result);
        }

    }

}
