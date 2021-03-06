package no.aev.seriouschatapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private ConvAdapter adapter;
    private static final String URL = "http://158.38.85.139:8080/SeriousChat2000/api/chat";
    private static final String CONV_PATH = "/conversations";
    private static final String NEW_CONV_PATH = "/newconversation";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Setup RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.conv_list);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ConvAdapter(this);
        rv.setAdapter(adapter);

        FloatingActionButton newChatButton = (FloatingActionButton) findViewById(R.id.new_conv_button);

        adapter.setOnClickListener(new ConvAdapter.OnClickListener()
        {
            @Override
            public void onClick(int position)
            {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("convid", adapter.getConvs().get(position).getId());
                startActivity(intent);
            }
        });

        loadConvs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        // Associate searchable configuration with SearchView
        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));

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
     * Called when the New Conversation button is tapped.
     * Creates a new empty conversation.
     *
     * @param view
     */
    public void newConvButtonOnClick(View view)
    {
        HttpURLConnection conn;
        URL newConvURL;

        try
        {
            newConvURL = new URL(URL + NEW_CONV_PATH);
            conn = (HttpURLConnection) newConvURL.openConnection();

            loadConvs();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
        try
        {
            System.out.println("Creating new conversation from " + URL + NEW_CONV_PATH);
            new NewConversation(new NewConversation.OnPostExecute()
            {
                @Override
                public void onPostExecute(List<Conversation> convs)
                {
                    System.out.println("Got: " + convs.toString());
                    adapter.setConvs(convs);
                }
            }).execute(new URL(URL + NEW_CONV_PATH));
            adapter.notifyDataSetChanged();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        */
    }

    /**
     * Loads the list of conversations from the server.
     */
    private void loadConvs()
    {
        try
        {
            System.out.println("Fetching conversation list from " + URL + CONV_PATH);
            new LoadConversations(new LoadConversations.OnPostExecute()
            {
                @Override
                public void onPostExecute(List<Conversation> convs)
                {
                    System.out.println("Got: " + convs.toString());
                    adapter.setConvs(convs);
                }
            }).execute(new URL(URL + CONV_PATH));
            adapter.notifyDataSetChanged();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }
}
