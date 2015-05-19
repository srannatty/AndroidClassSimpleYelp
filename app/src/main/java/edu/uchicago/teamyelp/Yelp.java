package edu.uchicago.teamyelp;

/**
 * Created by wuyanzhe on 5/15/15.
 */
/*
 Example code based on code from Nicholas Smith at http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
 For a more complete example (how to integrate with GSON, etc) see the blog post above.
 */

import com.google.gson.Gson;
import com.yelp.v2.YelpSearchResult;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;



/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

    OAuthService service;
    Token accessToken;

    private String mTerm;
    private double mLatitude;
    private double mLongitude;
    private YelpSearchResult mResult;
    private String mRawData;


    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
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

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     *
     */
    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);

        //This is where the app is quiting now.
        this.service.signRequest(this.accessToken, request);

        //the line where Android seem to be having problem?
        Response response = request.send();
        return response.getBody();
    }

    // makes String JSON into GSON object
    // precisely, using GSON to convert JSON into Java Object, YelpSearchResult
    public YelpSearchResult makingGSON(String rawData) {
        YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
        return places;
    }
}