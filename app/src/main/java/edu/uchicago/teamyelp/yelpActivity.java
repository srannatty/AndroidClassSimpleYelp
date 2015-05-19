package edu.uchicago.teamyelp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;


public class yelpActivity extends AppCompatActivity {

    //Yelp API Keys
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


    private String mTerm;
    private double mLatitude;
    private double mLongitude;
    private YelpSearchResult mResult;
    private String mRawData;


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
        //Register Context Menu  --> Is this necessary?
        //registerForContextMenu(mListView);

        //Button behavior for Search

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new YelpTask().execute("test");
//                mYelp.setTerm(mSearchTerm.getText().toString());
//                mYelp.setLatitude(Double.parseDouble(mSearchLatitude.getText().toString()));
//                mYelp.setLongitude(Double.parseDouble(mSearchLongitude.getText().toString()));
//
//                mYelp.setResult(
//                        mYelp.makingGSON(
//                                mYelp.search(mYelp.getTerm(), mYelp.getLatitude(), mYelp.getLongitude())));
//
//                setAdapter(mYelp.getResult());
//                mListView.setAdapter(mAdapter);


                //setAdapter(search("burritos", 30.361471, -87.164326));
             //   mListView.setAdapter(mAdapter);

            }
        });

        //setAdapter(mYelp.getResult());
        //mListView.setAdapter(mAdapter);
    }

    //getters and setters
    public void setTerm(String term) {
        this.mTerm = term;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public void setResult(YelpSearchResult result) {
        this.mResult = result;
    }

    public String getTerm() {
        return this.mTerm;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public YelpSearchResult getResult() {
        return this.mResult;
    }


    //Create Adapter and set it given YelpSearchResult
    public void setAdapter(YelpSearchResult data) {
        List<Business> businessesList = data.getBusinesses();
        mAdapter = new BusinessAdapter(this, businessesList);
        //Below is before we made BusinessAdapter
        //ArrayAdapter<Business>(this, R.layout.yelp_row, businessesList);
    }

    //Update list view. Should be used after using search. We might not even need this
    public void updateListView() {
        mAdapter.notifyDataSetChanged();
        mListView.invalidateViews();
        mListView.refreshDrawableState();
    }

    // makes String JSON into GSON object
    // precisely, using GSON to convert JSON into Java Object, YelpSearchResult
    public YelpSearchResult makingGSON(String rawData) {
        YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
        return places;
    }

    //Returns YelpSearch Result, using the yelp functionality
    public YelpSearchResult search(String term, double latitude, double longitude) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = mConsumerKey;
        String consumerSecret = mConsumerSecret;
        String token = mToken;
        String tokenSecret = mTokenSecret;

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search(term, latitude, longitude); //example
        return makingGSON(response);
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

        }

        @Override
        protected String doInBackground(String... params) {
            mYelp.setTerm(mSearchTerm.getText().toString());
            mYelp.setLatitude(Double.parseDouble(mSearchLatitude.getText().toString()));
            mYelp.setLongitude(Double.parseDouble(mSearchLongitude.getText().toString()));

            mYelp.setResult(
                    mYelp.makingGSON(
                            mYelp.search(mYelp.getTerm(), mYelp.getLatitude(), mYelp.getLongitude())));

            setAdapter(mYelp.getResult());
            mListView.setAdapter(mAdapter);
            return "test";
        }

//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            double dCalculated = 0.0;
//            String strForCode = extractCodeFromCurrency(mCurrencies[mForSpinner.getSelectedItemPosition()]);
//            String strHomCode = extractCodeFromCurrency(mCurrencies[mHomSpinner.getSelectedItemPosition()]);
//
//            String strAmount = mAmountEditText.getText().toString();
//            try{
//                if (jsonObject==null){
//                    throw new JSONException("no data available.");
//
//                }
//                JSONObject jsonRates= jsonObject.getJSONObject(RATES);
//                if (strHomCode.equalsIgnoreCase("USD")) {
//                    dCalculated = Double.parseDouble(strAmount) / jsonRates.getDouble(strForCode);
//                }else if (strForCode.equalsIgnoreCase("USD")){
//                    dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode);
//                }else {
//                    dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode) /
//                            jsonRates.getDouble(strForCode);
//                }
//            }
//            catch(JSONException e) {
//                Toast.makeText(MainActivity.this, "There's benn a JSON exception: ", Toast.LENGTH_LONG).show();
//                mConvertedTextView.setText("");
//                e.printStackTrace();
//            }
//            mConvertedTextView.setText(DECIMAL_FORMAT.format(dCalculated)+" "+strHomCode);
//            progressDialog.dismiss();
//        }
    }


    class RetreiveSearchResults extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... terms) {
            mYelp = new Yelp(mConsumerKey, mConsumerSecret, mToken, mTokenSecret);
            mRawData = mYelp.search(terms[0], Double.parseDouble(terms[1]), Double.parseDouble(terms[2]));
            mResult = makingGSON(mRawData);
            return mRawData;

        }


        @Override
        protected void onPostExecute(String rawData) {
            //get result in form of YelpSearchResult
            //mResult = makingGSON(mRawData)
            Toast.makeText(yelpActivity.this, rawData, Toast.LENGTH_LONG).show();

        }


    }
}
