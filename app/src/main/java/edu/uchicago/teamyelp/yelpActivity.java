package edu.uchicago.teamyelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;




public class yelpActivity extends AppCompatActivity {

    //Yelp API Keys given by Prof. Gerber
    private static final String mConsumerKey = "dSZgGbpE51gcJ2mPFy8Dag";
    private static final String mConsumerSecret = "CAe7Yp1NEYVPh2Z2ZpDDetqUpWM";
    private static final String mToken = "ksJ-aFEUA-sO8YKI9TwbTem8DoLOOtH0";
    private static final String mTokenSecret = "O1oqDGf93zFEz-_ctYgicO1VYQM";
    //private static final String mKey;
    private Yelp mYelp;



    //Jennifer Edit: List View things
    private ListView mListView;
    private BusinessAdapter mAdapter;


    //Layout Stuffs:
    private EditText mSearchTerm;
    private EditText mSearchLatitude;
    private EditText mSearchLongitude;

    private Button mSearchButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);

        //Get Yelp API Keys from keys.properties
        //these are properly populated
        /*
        mConsumerKey = getKey("Consumer_Key");
        mConsumerSecret = getKey("Consumer_Secret");
        mToken = getKey("Token");
        mTokenSecret = getKey("Token_Secret");
        //Don't really need this
        mKey = getKey("Key");
        */

        //Get Layout Setup
        mSearchTerm = (EditText) findViewById(R.id.SearchTerm);
        mSearchLatitude = (EditText) findViewById(R.id.SearchLatitude);
        mSearchLongitude = (EditText) findViewById(R.id.SearchLongitude);

        mSearchButton = (Button) findViewById(R.id.SearchButton);

        mListView = (ListView) findViewById(R.id.yelp_list_view);
        mListView.setDivider(null);
        //Register Context Menu  --> Is this necessary? (Comment by Jen)
        //registerForContextMenu(mListView);
        //Button behavior for Search

        mYelp = new Yelp(mConsumerKey, mConsumerSecret, mToken, mTokenSecret);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new YelpTask().execute("dummy");
                mListView.setAdapter(mAdapter);
            }
        });

    }



    //Create Adapter and set it given YelpSearchResult
    public void setAdapter(YelpSearchResult data) {
        final List<Business> businessesList = data.getBusinesses();
        mAdapter = new BusinessAdapter(this, businessesList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int nId = (int) arg3;
                Business business = businessesList.get(nId);
                new AlertDialog.Builder(yelpActivity.this)
                        .setTitle(business.getName()+" - "+business.getCategories())
                        .setMessage(business.getLocation().getAddress().toString()+", "+business.getLocation().getCity()+", "+business.getLocation().getStateCode()+", "+business.getLocation().getCountryCode()+" "+business.getLocation().getPostalCode()+" "+business.getPhone())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    //Update list view. Should be used after using search. We might not even need this
    public void updateListView() {
        mAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
        mListView.refreshDrawableState();


    }




    //Returns YelpSearch Result, using the yelp functionality
    public String search(String term, double latitude, double longitude) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = mConsumerKey;
        String consumerSecret = mConsumerSecret;
        String token = mToken;
        String tokenSecret = mTokenSecret;

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search(term, latitude, longitude); //example
        return response;
    }


    //getKey code -- same as the one from Procurrency
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

    //MENU STUFFS

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

    private class YelpTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

/*
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog((yelpActivity.this));
            progressDialog.setTitle("Calculating Result...");
            progressDialog.setMessage("One Moment Please...");
            progressDialog.setCancelable(true);

            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    YelpTask.this.cancel(true);
                    progressDialog.dismiss();

                }
            });
            progressDialog.show();

        }*/

        @Override
        protected String doInBackground(String... params) {
            mYelp.setTerm(mSearchTerm.getText().toString());
            mYelp.setLatitude(Double.parseDouble(mSearchLatitude.getText().toString()));
            mYelp.setLongitude(Double.parseDouble(mSearchLongitude.getText().toString()));

            mYelp.setResult(
                    mYelp.makingGSON(
                            mYelp.search(mYelp.getTerm(), mYelp.getLatitude(), mYelp.getLongitude())));

            setAdapter(mYelp.getResult());

            return "test";
        }

    }



}

