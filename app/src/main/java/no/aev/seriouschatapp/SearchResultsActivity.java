package no.aev.seriouschatapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aev on 22.10.17.
 */

public class SearchResultsActivity extends AppCompatActivity
{

    DatabaseTable db = new DatabaseTable(this);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            // Use the search query to search data
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

}
