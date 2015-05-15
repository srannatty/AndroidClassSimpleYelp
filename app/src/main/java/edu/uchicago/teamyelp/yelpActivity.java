package edu.uchicago.teamyelp;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class yelpActivity extends ActionBarActivity {

    //Yelp API Keys
    private String mConsumerKey;
    private String mConsumerSecret;
    private String mToken;
    private String mTokenSecret;
    private String mKey;
    private Yelp yelp;

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





}
