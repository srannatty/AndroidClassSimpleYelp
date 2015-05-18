package edu.uchicago.teamyelp;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
    private ArrayAdapter<Business> mAdapter;


    //Layout Stuffs:
    private EditText mSearchTerm;
    private EditText mSearchLatitude;
    private EditText mSearchLongitude;

    private String mTerm;
    private double mLatitude;
    private double mLongitude;

    private Button mSearchButton;

    private YelpSearchResult mYelpData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);

        //Get Yelp API Keys from keys.properties
        mConsumerKey = getKey("Consumer_Key");
        mConsumerSecret = getKey("Consumer_Secret");
        mToken = getKey("Token");
        mTokenSecret = getKey("Token_Secret");
        //Don't really need this
        mKey = getKey("Key");

        //Get Layout Setup
        mSearchTerm = (EditText) findViewById(R.id.SearchTerm);
        mSearchLatitude = (EditText) findViewById(R.id.SearchLatitude);
        mSearchLongitude = (EditText) findViewById(R.id.SearchLongitude);

        mSearchButton = (Button) findViewById(R.id.SearchButton);

        mListView = (ListView) findViewById(R.id.yelp_list_view);
        mListView.setDivider(null);
        //Register Context Menu  --> Is this necessary?
        //registerForContextMenu(mListView);

        //Button behavior for Search
        /*
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTerm("burritos");
                setLatitude(30.361471);
                setLongitude(-87.164326);
                setAdapter(search("burritos", 30.361471, -87.164326));
                mListView.setAdapter(mAdapter);
            }
        });
        */

        mTerm = "burritos";
        mLatitude = 30.361471;
        mLongitude = -87.164326;

        mYelpData = search(mTerm, mLatitude, mLongitude);
        setAdapter(mYelpData);
        mListView.setAdapter(mAdapter);
    }


    public void setAdapter(YelpSearchResult data) {
        List<Business> businessesList = data.getBusinesses();
        mAdapter = new ArrayAdapter<Business>(this, R.layout.yelp_row, businessesList);
    }

    public void setTerm(String term) {
        this.mTerm = term;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }


    //Update list view. Should be used after using search.
    public void updateListView() {
        mAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
        mListView.refreshDrawableState();
    }


    public YelpSearchResult search(String term, double latitude, double longitude) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = mConsumerKey;
        String consumerSecret = mConsumerSecret;
        String token = mToken;
        String tokenSecret = mTokenSecret;

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search(term, latitude, longitude); //example
        return yelp.makingGSON(response);
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
