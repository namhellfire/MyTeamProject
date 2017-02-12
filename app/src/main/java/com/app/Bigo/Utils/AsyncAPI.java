package com.app.Bigo.Utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by nguyennam on 2/11/17.
 */

public class AsyncAPI extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String, String> data = new HashMap<>();
        try {
            UtilConnect.getAPI(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
