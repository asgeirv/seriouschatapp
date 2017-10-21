package no.aev.seriouschatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by aev on 18.10.17.
 */

public class ChatActivity extends AppCompatActivity
{

    private MsgAdapter adapter;
    private static final String URL = "http://192.168.1.33:8080/SeriousChat2000/api/chat";
    private static final String CHAT_PATH = "?name=";
    private static final String ADD_MESSAGE_PATH = "/add?name=";
    private Long convID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);

        RecyclerView rv = (RecyclerView) findViewById(R.id.conv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MsgAdapter(this);
        rv.setAdapter(adapter);

        Intent intent = getIntent();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Chat #" + intent.getLongExtra("convid", 0));
        convID = intent.getLongExtra("convid", 0);
        loadMsgs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the Send button is tapped.
     * Sends the typed chat message.
     *
     * @param v
     */
    public void sendButtonOnClick(View v)
    {
        EditText user = (EditText) findViewById(R.id.user_edit);
        EditText text = (EditText) findViewById(R.id.text_edit);
        String userName = user.getText().toString();
        String msgText = text.getText().toString();
        System.out.println("Username: " + userName);
        System.out.println("Message: " + msgText);
        if (!userName.equals("") && !msgText.equals(""))
            sendMessage(user.getText().toString(), text.getText().toString());
        text.setText("");
    }

    /**
     * Sends a chat message to the server.
     * @param user Username
     * @param text Chat message
     */
    public void sendMessage(String user, String text)
    {
        new PostMessageTask().execute(new PostMessageTask.PostMessage(URL + ADD_MESSAGE_PATH + convID, user, text));
        loadMsgs();
    }

    /**
     * Loads chat messages from the server.
     */
    private void loadMsgs()
    {
        try
        {
            System.out.println("Fetching messages from " + URL + CHAT_PATH + convID);
            new LoadMessages(new LoadMessages.OnPostExecute()
            {
                @Override
                public void onPostExecute(List<Message> msgs)
                {
                    System.out.println("Got: " + msgs);
                    adapter.setMsgs(msgs);
                }
            }).execute(new URL(URL + CHAT_PATH + convID));
            adapter.notifyDataSetChanged();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

}
