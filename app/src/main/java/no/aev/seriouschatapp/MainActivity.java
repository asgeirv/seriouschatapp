package no.aev.seriouschatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{

    private RecyclerView chatView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager chatLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        chatView = (RecyclerView) findViewById(R.id.chat_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        chatView.setHasFixedSize(true);

        // use a linear layout manager
        chatLayoutManager = new LinearLayoutManager(this);
        chatView.setLayoutManager(chatLayoutManager);

        // specify an adapter (see also next example)
        chatAdapter = new ChatAdapter(this);
        chatView.setAdapter(chatAdapter);

    }
}
