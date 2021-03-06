package no.aev.seriouschatapp;

import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Post a message to REST service
 * <p>
 * Created by mikael on 08.10.2017.
 */
public class PostMessageTask extends AbstractAsyncTask<PostMessageTask.PostMessage, Void, Boolean>
{

    public PostMessageTask()
    {
        super();
    }

    /**
     * Constructor with callback
     *
     * @param onPostExecute a callback that will run on the UI thread after data is posted
     */
    public PostMessageTask(OnPostExecute<Boolean> onPostExecute)
    {
        super(onPostExecute);
    }

    /**
     * This method will run as a background thread
     *
     * @param messages
     * @return
     */
    @Override
    protected Boolean doInBackground(PostMessage... messages)
    {
        for (PostMessage message : messages)
        {
            try
            {
                // Setup request
                URL url = new URL(message.getUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);    // We are going to write to the server
                con.setUseCaches(false);  // Not interested in browser/http cache
                con.setRequestMethod("POST"); // Using POST command
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  // JSON

                // Write Message object
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                JsonWriter jw = new JsonWriter(bw); // Android JSON support library
                jw.beginObject()
                        .name("user")
                        .value(message.getUser())
                        .name("text")
                        .value(message.getText())
                        .endObject()
                        .close();
                //System.out.println(jw.value("user").toString());

                // Get response from server
                StringBuilder result = new StringBuilder();
                int len;
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    Reader br = new InputStreamReader(new BufferedInputStream(con.getInputStream()), "UTF-8");
                    char[] cbuff = new char[1024];
                    while ((len = br.read(cbuff)) != -1)
                    {
                        result.append(cbuff, 0, len);
                    }
                    br.close();
                }
                else
                {
                    Log.e("PostMessage", "ResponseCode: " + con.getResponseCode());
                }
                con.disconnect();
            }
            catch (IOException e)
            {
                Log.e("PostMessage", "doInBackground: ", e);
            }
        }

        return true;
    }

    public static class PostMessage
    {

        private String url;
        private String user;
        private String text;

        public PostMessage(String url, String user, String text)
        {
            this.url = url;
            this.user = user;
            this.text = text;
        }

        public String getUrl()
        {
            return url;
        }

        public String getUser()
        {
            return user;
        }

        public String getText()
        {
            return text;
        }
    }
}