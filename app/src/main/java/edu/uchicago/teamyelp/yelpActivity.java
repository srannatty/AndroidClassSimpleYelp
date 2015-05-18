package edu.uchicago.teamyelp;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class yelpActivity extends AppCompatActivity {

    //Yelp API Keys
    private String mConsumerKey;
    private String mConsumerSecret;
    private String mToken;
    private String mTokenSecret;
    private String mKey;
    private Yelp mYelp;

    //Jennifer Edit: List View things
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);

        //Get Yelp API Keys from keys.properties
        mConsumerKey = getKey("Consumer_Key");
        mConsumerSecret = getKey("Consumer_Secret");
        mToken = getKey("Token");
        mTokenSecret = getKey("Token_Secret");
        mKey = getKey("Key");

        mListView = (ListView) findViewById(R.id.yelp_list_view);
        mListView.setDivider(null);
        //Register Context Menu
        registerForContextMenu(mListView);

        String[] fromColumns = new String[]{
                //Todo Way to initiate this?
                //RemindersDbAdapter.COL_CONTENT
                ""
        };

        int[] toView = new int[]{
                R.id.row_text
        };

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        //API 11 required to use SimpleCursorAdapter
        mAdapter = new SimpleCursorAdapter(
                //context
                this,
                //layout
                R.layout.yelp_row,
                //cursor
                null,
                //from columns? defined
                fromColumns,
                //to the ids of view in layout
                toView,
                //flag, not used
                0);
        //the cursorAdapter (controller) is now updating the listView (view) with data from the db (model)
        mListView.setAdapter(mAdapter);
    }

    //todo: Create cursor using data given by response
    public Cursor CreateCursor(String response) {
        Cursor cursor = new Cursor();
    }

    //Update list view. Should be used after using search.
    public void updateListView(Cursor cursor) {
        mAdapter.changeCursor(cursor);
        mAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
        mListView.refreshDrawableState();
    }



    public void search(String term, double latitude, double longitude) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = mConsumerKey;
        String consumerSecret = mConsumerSecret;
        String token = mToken;
        String tokenSecret = mTokenSecret;

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search(term, latitude, longitude); //example

        System.out.println(response);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yelp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getKey(String strKey) {
        AssetManager assetManager = this.getResources().getAssets();
        Properties properties = new Properties();

        try {
            InputStream inputStream = assetManager.open("keys.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(strKey);
    }









}
