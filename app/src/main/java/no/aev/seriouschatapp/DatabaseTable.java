package no.aev.seriouschatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aev on 22.10.17.
 */

public class DatabaseTable
{

    private static final String TAG = "SearchDB";

    //The columns we'll include in the dictionary table
    public static final String COL_TEXT = "TEXT";

    private static final String DATABASE_NAME = "SEARCH_DATA";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private static final String URL = "http://158.38.85.139:8080/SeriousChat2000/api/chat";
    private static final String CONV_PATH = "/conversations";
    private static final String CHAT_PATH = "?name=";

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseTable(Context context)
    {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper
    {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_TEXT + ")";

        DatabaseOpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        /**
         * Loads chat data in its own thread.
         */
        private void loadData()
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        loadConvs();
                        loadMsgs();
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private long addText(String text)
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_TEXT, text);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }

        private void loadConvs() throws IOException
        {
            try
            {
                new LoadConversations(new LoadConversations.OnPostExecute()
                {
                    @Override
                    public void onPostExecute(List<Conversation> convs)
                    {
                        System.out.println("Got: " + convs.toString());
                        for (Conversation conv : convs)
                        {
                            addText(conv.getId().toString());
                        }
                    }
                }).execute(new URL(URL + CONV_PATH));

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }


        private void loadMsgs()
        {
            try
            {
                Long[] ids = null;

                new LoadMessages(new LoadMessages.OnPostExecute()
                {
                    @Override
                    public void onPostExecute(List<Message> msgs)
                    {
                        System.out.println("Got: " + msgs);

                    }
                }).execute(new URL(URL + CHAT_PATH));
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }
}

