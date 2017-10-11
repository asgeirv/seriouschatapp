package no.aev.seriouschatapp;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LoadConversations extends AsyncTask<URL,Integer,List<Conversation>> {
    public interface OnPostExecute {
        void onPostExecute(List<Conversation> photos);
    }

    private OnPostExecute callback;

    public LoadConversations(OnPostExecute callback) {
        this.callback = callback;
    }

    @Override
    protected List<Conversation> doInBackground(URL... urls) {
        if(urls.length < 1) return Collections.EMPTY_LIST;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");List<Conversation> result = new ArrayList<>();

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext()) {
                Long id = null;

                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "id":
                            id = jr.nextLong();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new Conversation(id));
            }
            jr.endArray();
        } catch (IOException e) {
            Log.e("LoadConversations","Failed to load conversations from " + urls[0],e);
        }


        return result;
    }

    @Override
    protected void onPostExecute(List<Conversation> convs) {
        if(callback != null)
            callback.onPostExecute(convs);
    }
}
