package no.aev.seriouschatapp;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadMessages extends AsyncTask<URL, Integer, List<Message>>
{

    public interface OnPostExecute
    {

        void onPostExecute(List<Message> convs);
    }

    private OnPostExecute callback;

    public LoadMessages(OnPostExecute callback)
    {
        this.callback = callback;
    }

    @Override
    protected List<Message> doInBackground(URL... urls)
    {
        if (urls.length < 1) return Collections.EMPTY_LIST;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<Message> result = new ArrayList<>();

        HttpURLConnection con;
        try
        {
            con = (HttpURLConnection) urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext())
            {
                Long id = null;
                String text = null;
                String user = null;

                jr.beginObject();
                while (jr.hasNext())
                {
                    switch (jr.nextName())
                    {
                        case "id":
                            id = jr.nextLong();
                            break;
                        case "text":
                            text = jr.nextString();
                            break;
                        case "user":
                            user = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                    }
                }
                jr.endObject();
                result.add(new Message(id, text, user));
            }
            jr.endArray();
        }
        catch (IOException e)
        {
            Log.e("LoadMessages", "Failed to load messages from " + urls[0], e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Message> msgs)
    {
        if (callback != null)
            callback.onPostExecute(msgs);
    }
}
