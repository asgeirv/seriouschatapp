package no.aev.seriouschatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private ChatAdapter adapter;
    private static final String CHAT_URL = "http://192.168.1.34:8080/api/conversation/conversations/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Setup RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.chat_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this);
        rv.setAdapter(adapter);

        try {
            new LoadConversations(new LoadConversations.OnPostExecute() {
                @Override
                public void onPostExecute(List<Conversation> convs) {
                    adapter.setConvs(convs);
                }
            }).execute(new URL(CHAT_URL));
        } catch (MalformedURLException e) {
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
