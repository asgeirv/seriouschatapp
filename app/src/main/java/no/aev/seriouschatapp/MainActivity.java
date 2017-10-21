package no.aev.seriouschatapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

    private ConvAdapter adapter;
    private static final String CONV_URL = "http://192.168.1.33:8080/SeriousChat2000/api/chat/conversations/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Setup RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.conv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConvAdapter(this);
        rv.setAdapter(adapter);

        FloatingActionButton newChatButton = (FloatingActionButton) findViewById(R.id.new_conv_button);

        adapter.setOnClickListener(new ConvAdapter.OnClickListener()
        {
            @Override
            public void onClick(int position)
            {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
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
     * Loads the list of conversations from the server.
     */
    private void loadConvs()
    {
        try {
            new LoadConversations(new LoadConversations.OnPostExecute() {
                @Override
                public void onPostExecute(List<Conversation> convs) {
                    System.out.println("Got: " + convs);
                    adapter.setConvs(convs);
                }
            }).execute(new URL(CONV_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
