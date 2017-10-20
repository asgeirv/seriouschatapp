package no.aev.seriouschatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by aev on 18.10.17.
 */

public class ChatActivity extends AppCompatActivity
{

    private MsgAdapter adapter;
    private static final String CHAT_URL = "http://192.168.1.33:8080/SeriousChat2000/api/chat?name=";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.send_button);

        RecyclerView rv = (RecyclerView) findViewById(R.id.conv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MsgAdapter(this);
        rv.setAdapter(adapter);

        Intent intent = getIntent();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Chat #" + intent.getLongExtra("convid", 0));
        Long convID = intent.getLongExtra("convid", 0);

        try
        {
            new LoadMessages(new LoadMessages.OnPostExecute()
            {
                @Override
                public void onPostExecute(List<Message> msgs)
                {
                    System.out.println("Got: " + msgs);
                    adapter.setMsgs(msgs);
                }
            }).execute(new URL(CHAT_URL + convID));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
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

}
