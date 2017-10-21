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

/**
 * Created by aev on 21.10.17.
 */

public class NewConversation extends AsyncTask<URL, Integer, List<Conversation>>
{

    public interface OnPostExecute
    {

        void onPostExecute(List<Conversation> convs);
    }

    private OnPostExecute callback;

    public NewConversation(OnPostExecute callback)
    {
        this.callback = callback;
    }

    @Override
    protected List<Conversation> doInBackground(URL... urls)
    {
        if (urls.length < 1) return Collections.EMPTY_LIST;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<Conversation> result = new ArrayList<>();

        HttpURLConnection con;
        try
        {
            con = (HttpURLConnection) urls[0].openConnection();
            JsonReader jr = new JsonReader(new InputStreamReader(con.getInputStream()));
            jr.beginArray();
            while (jr.hasNext())
            {
                Long id = null;

                jr.beginObject();
                while (jr.hasNext())
                {
                    switch (jr.nextName())
                    {
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
        }
        catch (IOException e)
        {
            Log.e("NewConversation", "Failed to create new conversation with " + urls[0], e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Conversation> convs)
    {
        if (callback != null)
            callback.onPostExecute(convs);
    }
}
