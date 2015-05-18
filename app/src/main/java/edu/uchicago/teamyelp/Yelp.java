package edu.uchicago.teamyelp;

/**
 * Created by wuyanzhe on 5/15/15.
 */
/*
 Example code based on code from Nicholas Smith at http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
 For a more complete example (how to integrate with GSON, etc) see the blog post above.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;



/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

    OAuthService service;
    Token accessToken;

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
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }



    // makes String JSON into GSON object
    // precisely, using GSON to convert JSON into Java Object, YelpSearchResult
    public YelpSearchResult makingGSON(String rawData) {

        YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
        return places;
    }

    // CLI
    public static void main(String[] args) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = "HcxdKRawNf4RbbOM-0Otnw";
        String consumerSecret = "58qOb_C4ajGFe6qOSQa_vCEwzE4";
        String token = "q-qkNDTsI6ujKrgN0obVy1oyzmgQmHdE";
        String tokenSecret = "6G5cFBBC1FbrsASFJJBZDqjkyME";

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search("burritos", 30.361471, -87.164326);

        System.out.println(response);
    }


/*
Following is the result from the main function.
    {
        "region":
        {
            "span":
            {
                "latitude_delta":0.12626284000000254, "longitude_delta":0.16365657000000056
            },
            "center":{
            "latitude":30.390230199999998, "longitude":-87.141395649999993
        }
        },
        "total":22,
            "businesses":
        [
        {
            "is_claimed":true, "distance":3559.7263811893226,
                "mobile_url":"http://m.yelp.com/biz/cactus-flower-cafe-pensacola-beach",
                "rating_img_url":"http://s3-media1.fl.yelpcdn.com/assets/2/www/img/5ef3eb3cb162/ico/stars/v1/stars_3_half.png",
                "review_count":46,
                "name":"Cactus Flower Cafe",
                "snippet_image_url":"http://s3-media3.fl.yelpcdn.com/photo/X2dY4l4keR02bAbIfcCfFQ/ms.jpg",
                "rating":3.5,
                "url":"http://www.yelp.com/biz/cactus-flower-cafe-pensacola-beach",
                "snippet_text": "Kinda just OK.\n\nThe Queso was the thinnest that I've ever had. Asked for a spoon and ate it like soup. Not served overly warm, either. Maybe it's just their...",
                "image_url":"http://s3-media4.fl.yelpcdn.com/bphoto/Et88v9CC5HpYruyqrOJk1w/ms.jpg",
                "categories":[["Mexican", "mexican"]],
                "rating_img_url_small":"http://s3-media1.fl.yelpcdn.com/assets/2/www/img/2e909d5d3536/ico/stars/v1/stars_small_3_half.png",
                "rating_img_url_large": "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png",
                "id":"cactus-flower-cafe-pensacola-beach",
                "is_closed":false,
                "location":{"city":"Pensacola Beach",
                            "display_address":["400 Quietwater Beach Rd", "Pensacola Beach, FL 32561"],
                            "geo_accuracy":9.5,
                            "postal_code":"32561",
                            "country_code":"US",
                            "address":["400 Quietwater Beach Rd"],
                            "coordinate":{
                                            "latitude":30.335521,
                                            "longitude":-87.142601999999997},
                            "state_code":"FL"}
                            },

        {
            "is_claimed":true, "distance":7017.3101938356931,
                "mobile_url":"http://m.yelp.com/biz/ztaco-pensacola",
                "rating_img_url":"http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png",
                "review_count":29,
                "name":"ZTaco",
                "snippet_image_url": "http://s3-media3.fl.yelpcdn.com/photo/O5EmDw9268FdrM9hIzTEEQ/ms.jpg",
                "rating":4.0,
                "url":"http://www.yelp.com/biz/ztaco-pensacola",
                "location":{ "city":"Pensacola", "display_address":["501 S Palafox St", "Pensacola, FL 32502"],
                     "geo_accuracy":9.5, "postal_code":"32502", "country_code":"US",
                    "address":["501 S Palafox St"],
                    "coordinate":{
                        "latitude":30.407435, "longitude":-87.214454000000003
                        },"state_code":"FL"
                    },
            "phone":"8504381999",
                "snippet_text":
            "We had a yummy meal on a sunny afternoon. Got the grande burrito with fried avocado and shrimp ($7.95). The shrimp was absolutely delicious and the chunks...",
                    "image_url":
            "http://s3-media3.fl.yelpcdn.com/bphoto/bEofyl-ukwqRWz8ziSHlrQ/ms.jpg",
                    "categories":[["Mexican", "mexican"]],
            "display_phone":"+1-850-438-1999", "rating_img_url_large":
            "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png",
                    "id":"ztaco-pensacola",
                "is_closed":false,
                "rating_img_url_small":
            "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
        },

        {
            "is_claimed":true, "distance":412.51460045830487,
                "mobile_url":"http://m.yelp.com/biz/taco-bell-gulf-breeze", "rating_img_url":
            "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/34bc8086841c/ico/stars/v1/stars_3.png", "review_count":
            6, "name":"Taco Bell", "snippet_image_url":
            "http://s3-media4.fl.yelpcdn.com/photo/cG7_MdyNJzgG923vT0GaPA/ms.jpg", "rating":
            3.0, "url":"http://www.yelp.com/biz/taco-bell-gulf-breeze", "location":{
            "city":"Gulf Breeze", "display_address":[
            "112 Gulf Breeze Pkwy", "Gulf Breeze, FL 32561"],"geo_accuracy":9.5, "postal_code":
            "32561", "country_code":"US", "address":["112 Gulf Breeze Pkwy"],"coordinate":{
                "latitude":30.357782, "longitude":-87.163871
            },"state_code":"FL"
        },"phone":"8509321347", "snippet_text":
            "Ha, I know, I know. It's just Taco Bell. But this one is probably the best 1 I've visited.\n\nI came here to Pensacola on my mini-birthday/vaca getaway...", "image_url":
            "http://s3-media4.fl.yelpcdn.com/bphoto/qB9noM33McI-gfUc1z6YUQ/ms.jpg", "categories":[[
            "Fast Food", "hotdogs"],["Mexican", "mexican"],["Tex-Mex", "tex-mex"]],"display_phone":
            "+1-850-932-1347", "rating_img_url_large":
            "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/e8b5b79d37ed/ico/stars/v1/stars_large_3.png", "id":
            "taco-bell-gulf-breeze", "is_closed":false, "rating_img_url_small":
            "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/902abeed0983/ico/stars/v1/stars_small_3.png"
        },{
        "is_claimed":false, "distance":5665.2039453709685, "mobile_url":
        "http://m.yelp.com/biz/mariachis-mexican-grill-gulf-breeze", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        21, "name":"Mariachi's Mexican Grill", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/photo/y_G476hZxnrUcon2VdnsBQ/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/mariachis-mexican-grill-gulf-breeze", "location":{
            "city":"Gulf Breeze", "display_address":[
            "2747 Gulf Breeze Pkwy", "Gulf Breeze, FL 32563"],"geo_accuracy":8.0, "postal_code":
            "32563", "country_code":"US", "address":["2747 Gulf Breeze Pkwy"],"coordinate":{
                "latitude":30.3810997, "longitude":-87.109909099999996
            },"state_code":"FL"
        },"menu_date_updated":1387712968, "phone":"8509343606", "snippet_text":
        "Great salsa and fresh chips. Everyone was very nice. We got our food quickly. :) I really loved their rice. Cheese sauce was very tasty. Really enjoyed this...", "image_url":
        "http://s3-media3.fl.yelpcdn.com/bphoto/f4zzhA1gHghNs-zaQN1nTA/ms.jpg", "categories":[[
        "Mexican", "mexican"]],"display_phone":"+1-850-934-3606", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "menu_provider":
        "single_platform", "id":"mariachis-mexican-grill-gulf-breeze", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":true, "distance":4233.0245423219585, "mobile_url":
        "http://m.yelp.com/biz/dog-house-deli-pensacola-beach", "rating_img_url":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/99493c12711e/ico/stars/v1/stars_4_half.png", "review_count":
        57, "name":"Dog House Deli", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/photo/_GXbm0TTDqf3lyCgOd5msQ/ms.jpg", "rating":4.5, "url":
        "http://www.yelp.com/biz/dog-house-deli-pensacola-beach", "location":{
            "city":"Pensacola Beach", "display_address":[
            "35 Via De Luna Dr", "Pensacola Beach, FL 32561"],"geo_accuracy":9.5, "postal_code":
            "32561", "country_code":"US", "address":["35 Via De Luna Dr"],"coordinate":{
                "latitude":30.335008999999999, "longitude":-87.132613000000006
            },"state_code":"FL"
        },"phone":"8509164993", "snippet_text":
        "This was my first time to Pensacola, but I'm so glad I saw this place listed on Yelp, because I LOVE hot dogs. And I come from Cincinnati, where we have the...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/le9YkoR6Ag_rfs2iu4zKfw/ms.jpg", "categories":[[
        "Breakfast \u0026 Brunch", "breakfast_brunch"],["Hot Dogs", "hotdog"]],"display_phone":
        "+1-850-916-4993", "rating_img_url_large":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png", "id":
        "dog-house-deli-pensacola-beach", "is_closed":false, "rating_img_url_small":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/a5221e66bc70/ico/stars/v1/stars_small_4_half.png"
    },{
        "is_claimed":false, "distance":4257.7458834715917, "mobile_url":
        "http://m.yelp.com/biz/native-cafe-pensacola-beach", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        137, "name":"Native Cafe", "snippet_image_url":
        "http://s3-media1.fl.yelpcdn.com/photo/3L4IGhLXKjvrepA4pSOe4g/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/native-cafe-pensacola-beach", "location":{
            "city":"Pensacola Beach", "display_address":[
            "45 A Via De Luna Dr", "Pensacola Beach, FL 32561"],"geo_accuracy":9.5, "postal_code":
            "32561", "country_code":"US", "address":["45 A Via De Luna Dr"],"coordinate":{
                "latitude":30.334805723071, "longitude":-87.132482528686509
            },"state_code":"FL"
        },"phone":"8509344848", "snippet_text":
        "I love this place! Everytime we go down to Florida, we always come over here. It's a small, local cafe. The walls are decorated with some cool art, the...", "image_url":
        "http://s3-media4.fl.yelpcdn.com/bphoto/bOhIJ24DkwrmXrkUxRCmCA/ms.jpg", "categories":[[
        "American (Traditional)", "tradamerican"]],"display_phone":
        "+1-850-934-4848", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "id":
        "native-cafe-pensacola-beach", "is_closed":false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":false, "distance":8102.7988286678456, "mobile_url":
        "http://m.yelp.com/biz/the-east-hill-yard-pensacola", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        22, "name":"The East Hill Yard", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/photo/rA0ZTsCPffI_LbCw2GyaZg/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/the-east-hill-yard-pensacola", "location":{
            "city":"Pensacola", "display_address":["1010 N 12th Ave", "Pensacola, FL 32502"],
            "geo_accuracy":9.5, "postal_code":"32502", "country_code":"US", "address":[
            "1010 N 12th Ave"],"coordinate":{
                "latitude":30.4264396506595, "longitude":-87.202586419880404
            },"state_code":"FL"
        },"phone":"8506962663", "snippet_text":
        "Everytime I go, the food has been excellent. Everything I've tried has been great quality. Love the tamales, they are huge ! \nUnfortunately I've had more...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/dnGKXHeTQ3Fd0t_mj3C8Og/ms.jpg", "categories":[[
        "Cafes", "cafes"],["Mexican", "mexican"]],"display_phone":
        "+1-850-696-2663", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "id":
        "the-east-hill-yard-pensacola", "is_closed":false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":true, "distance":7133.7778491288864, "mobile_url":
        "http://m.yelp.com/biz/xiscali-pensacola", "rating_img_url":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/34bc8086841c/ico/stars/v1/stars_3.png", "review_count":
        27, "name":"Xiscali", "snippet_image_url":
        "http://s3-media2.fl.yelpcdn.com/photo/l5GHxtScNIRY7L1CJJc2rw/ms.jpg", "rating":3.0, "url":
        "http://www.yelp.com/biz/xiscali-pensacola", "location":{
            "city":"Pensacola", "display_address":["920 E Gregory St", "Pensacola, FL 32502"],
            "geo_accuracy":9.5, "postal_code":"32502", "country_code":"US", "address":[
            "920 E Gregory St"],"coordinate":{
                "latitude":30.419010026641502, "longitude":-87.197222001850605
            },"state_code":"FL"
        },"phone":"8504352907", "snippet_text":
        "Wow this place is such a gem! Delicious, authentic Mexican right in downtown pcola. My husband \u0026 I came in about an hour before close on a Saturday night....", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/K8jZKUy55ijPCVYwmLG3mw/ms.jpg", "categories":[[
        "Mexican", "mexican"]],"display_phone":"+1-850-435-2907", "rating_img_url_large":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/e8b5b79d37ed/ico/stars/v1/stars_large_3.png", "id":
        "xiscali-pensacola", "is_closed":false, "rating_img_url_small":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/902abeed0983/ico/stars/v1/stars_small_3.png"
    },{
        "is_claimed":false, "distance":7240.7700496300031, "mobile_url":
        "http://m.yelp.com/biz/cazadores-mexican-restaurant-pensacola-2", "rating_img_url":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/34bc8086841c/ico/stars/v1/stars_3.png", "review_count":
        19, "name":"Cazadores Mexican Restaurant", "snippet_image_url":
        "http://s3-media1.fl.yelpcdn.com/photo/utUO9t4KSTPiwY7vldG9AA/ms.jpg", "rating":3.0, "url":
        "http://www.yelp.com/biz/cazadores-mexican-restaurant-pensacola-2", "location":{
            "city":"Pensacola", "display_address":["3005 E Cervantes St", "Pensacola, FL 32503"],
            "geo_accuracy":8.0, "postal_code":"32503", "country_code":"US", "address":[
            "3005 E Cervantes St"],"coordinate":{
                "latitude":30.424862000000001, "longitude":-87.181595000000002
            },"state_code":"FL"
        },"phone":"8504381747", "snippet_text":
        "Drove by this place and wanted to try them out. \nSalsa is average. \nHorchata was so good. It was delicious. \nI got a chicken chimichanga and chicken fluta....", "image_url":
        "http://s3-media4.fl.yelpcdn.com/bphoto/a4VlFaKzdTjxwyiIAn3fAg/ms.jpg", "categories":[[
        "Mexican", "mexican"]],"display_phone":"+1-850-438-1747", "rating_img_url_large":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/e8b5b79d37ed/ico/stars/v1/stars_large_3.png", "id":
        "cazadores-mexican-restaurant-pensacola-2", "is_closed":false, "rating_img_url_small":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/902abeed0983/ico/stars/v1/stars_small_3.png"
    },{
        "is_claimed":true, "distance":10458.294925543167, "mobile_url":
        "http://m.yelp.com/biz/cactus-flower-cafe-pensacola", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        81, "name":"Cactus Flower Cafe", "snippet_image_url":
        "http://s3-media3.fl.yelpcdn.com/photo/P26bKMJsDd5VAeMnnOA1cQ/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/cactus-flower-cafe-pensacola", "location":{
            "city":"Pensacola", "display_address":["3425 N 12th Ave", "Pensacola, FL 32503"],
            "geo_accuracy":8.0, "postal_code":"32503", "country_code":"US", "address":[
            "3425 N 12th Ave"],"coordinate":{
                "latitude":30.4477688670158, "longitude":-87.208138257265105
            },"state_code":"FL"
        },"menu_date_updated":1387539199, "phone":"8504328100", "snippet_text":
        "Love this place wonderful place to eat love the one on 12th Ave just tried the one on hwy 98 and it was great to", "image_url":
        "http://s3-media1.fl.yelpcdn.com/bphoto/R3PWyzwoOkupAqIjcMURiw/ms.jpg", "categories":[[
        "Mexican", "mexican"]],"display_phone":"+1-850-432-8100", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "menu_provider":
        "single_platform", "id":"cactus-flower-cafe-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":false, "distance":9815.4638790554018, "mobile_url":
        "http://m.yelp.com/biz/rio-bravo-mexican-restaurant-gulf-breeze", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        45, "name":"Rio Bravo Mexican Restaurant", "snippet_image_url":
        "http://s3-media3.fl.yelpcdn.com/photo/4PwChTEu_GUcJJhFREqxaQ/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/rio-bravo-mexican-restaurant-gulf-breeze", "location":{
            "city":"Gulf Breeze", "display_address":[
            "3755 Gulf Breeze Pkwy", "Ste I", "Gulf Breeze, FL 32563"],"geo_accuracy":
            8.0, "postal_code":"32563", "country_code":"US", "address":[
            "3755 Gulf Breeze Pkwy", "Ste I"],"coordinate":{
                "latitude":30.389553100000001, "longitude":-87.067131000000003
            },"state_code":"FL"
        },"phone":"8509161221", "snippet_text":
        "This is by far one of the best Mexican restaurants I've eaten at. All of the food is seasoned well and the portions are great! I really enjoy the grilled...", "image_url":
        "http://s3-media3.fl.yelpcdn.com/bphoto/Y_guj2EDPfjs2cC9LZvK9A/ms.jpg", "categories":[[
        "Mexican", "mexican"]],"display_phone":"+1-850-916-1221", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "id":
        "rio-bravo-mexican-restaurant-gulf-breeze", "is_closed":false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":true, "distance":7121.2421576645811, "mobile_url":
        "http://m.yelp.com/biz/o-rileys-irish-pub-downtown-pensacola", "rating_img_url":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/5ef3eb3cb162/ico/stars/v1/stars_3_half.png", "review_count":
        16, "name":"O'Riley's Irish Pub Downtown", "snippet_image_url":
        "http://s3-media3.fl.yelpcdn.com/photo/wLVUhm3c3QVOtGIciu64Zw/ms.jpg", "rating":3.5, "url":
        "http://www.yelp.com/biz/o-rileys-irish-pub-downtown-pensacola", "location":{
            "city":"Pensacola", "display_address":["321 S Palafox St", "Pensacola, FL 32502"],
            "geo_accuracy":9.5, "postal_code":"32502", "country_code":"US", "address":[
            "321 S Palafox St"],"coordinate":{
                "latitude":30.408559, "longitude":-87.214645000000004
            },"state_code":"FL"
        },"menu_date_updated":1429596388, "phone":"8509124001", "snippet_text":
        "super nice Irie pub.\nlarge tap selection.\nwell dressed,friendly staff.\nenough HD tvs to watch all the games.\nnot so many people you cant enjoy it.", "image_url":
        "http://s3-media3.fl.yelpcdn.com/bphoto/y4x7IBCAuOAH861K6CKuxw/ms.jpg", "categories":[[
        "Irish Pub", "irish_pubs"],["Irish", "irish"]],"display_phone":
        "+1-850-912-4001", "rating_img_url_large":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png", "menu_provider":
        "single_platform", "id":"o-rileys-irish-pub-downtown-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/2e909d5d3536/ico/stars/v1/stars_small_3_half.png"
    },{
        "is_claimed":false, "distance":7316.426940410418, "mobile_url":
        "http://m.yelp.com/biz/sluggos-pensacola", "rating_img_url":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/99493c12711e/ico/stars/v1/stars_4_half.png", "review_count":
        70, "name":"Sluggo's", "snippet_image_url":
        "http://s3-media2.fl.yelpcdn.com/photo/odywk3RRvyQQ7eX56mGsjw/ms.jpg", "rating":4.5, "url":
        "http://www.yelp.com/biz/sluggos-pensacola", "location":{
            "city":"Pensacola", "display_address":["101 S Jefferson St", "Pensacola, FL 32501"],
            "geo_accuracy":8.0, "postal_code":"32501", "country_code":"US", "address":[
            "101 S Jefferson St"],"coordinate":{
                "latitude":30.411252999999999, "longitude":-87.214195000000004
            },"state_code":"FL"
        },"phone":"8507916501", "snippet_text":
        "This place was a gem. I got the golden bowl (sub rice for mash) and it was huge and delicious and perfect. I couldn't even finish it. \n\nMy friend got the...", "image_url":
        "http://s3-media3.fl.yelpcdn.com/bphoto/wzqh_WEy4zwz7LCtXHeF1g/ms.jpg", "categories":[[
        "Vegan", "vegan"],["Music Venues", "musicvenues"]],"display_phone":
        "+1-850-791-6501", "rating_img_url_large":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png", "id":
        "sluggos-pensacola", "is_closed":false, "rating_img_url_small":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/a5221e66bc70/ico/stars/v1/stars_small_4_half.png"
    },{
        "is_claimed":true, "distance":7860.6886476207837, "mobile_url":
        "http://m.yelp.com/biz/polonza-bistro-pensacola", "rating_img_url":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/c2f3dd9799a5/ico/stars/v1/stars_4.png", "review_count":
        39, "name":"Polonza Bistro", "snippet_image_url":
        "http://s3-media1.fl.yelpcdn.com/photo/m3Xuek7t_LcxnfrWXZb6BA/ms.jpg", "rating":4.0, "url":
        "http://www.yelp.com/biz/polonza-bistro-pensacola", "location":{
            "city":"Pensacola", "display_address":["286 N Palafox St", "Pensacola, FL 32502"],
            "geo_accuracy":8.0, "postal_code":"32502", "country_code":"US", "address":[
            "286 N Palafox St"],"coordinate":{
                "latitude":30.416490599999999, "longitude":-87.215782200000007
            },"state_code":"FL"
        },"menu_date_updated":1418489948, "phone":"8509126454", "snippet_text":
        "We tried this place based on the recommendation of a friend. I must admit, I was not sure what to expect when going Saturday morning. I debated whether to...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/isTqKITbOe7_6D_f7VXYBA/ms.jpg", "categories":[[
        "American (New)", "newamerican"],["Coffee \u0026 Tea", "coffee"]],"display_phone":
        "+1-850-912-6454", "rating_img_url_large":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/ccf2b76faa2c/ico/stars/v1/stars_large_4.png", "menu_provider":
        "single_platform", "id":"polonza-bistro-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/f62a5be2f902/ico/stars/v1/stars_small_4.png"
    },{
        "is_claimed":false, "distance":7602.7204259829914, "mobile_url":
        "http://m.yelp.com/biz/end-of-the-line-cafe-pensacola", "rating_img_url":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/99493c12711e/ico/stars/v1/stars_4_half.png", "review_count":
        58, "name":"End of the Line Cafe", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/assets/srv0/yelp_styleguide/cc4afe21892e/assets/img/default_avatars/user_medium_square.png", "rating":
        4.5, "url":"http://www.yelp.com/biz/end-of-the-line-cafe-pensacola", "location":{
            "city":"Pensacola", "display_address":["610 E Wright St", "Pensacola, FL 32501"],
            "geo_accuracy":8.0, "postal_code":"32501", "country_code":"US", "address":[
            "610 E Wright St"],"coordinate":{
                "latitude":30.418300599999998, "longitude":-87.208679200000006
            },"state_code":"FL"
        },"menu_date_updated":1406018001, "phone":"8504290336", "snippet_text":
        "AWESOME!!\nI go alot for Sunday brunch, you can't beat the amount of food for the price! \nI'm ADDICTED to the chai latte there, it's got some kick to it. Jen...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/d4DTDPpTh70NEftLTEannw/ms.jpg", "categories":[[
        "Coffee \u0026 Tea", "coffee"],["Cafes", "cafes"],["Vegan", "vegan"]],"display_phone":
        "+1-850-429-0336", "rating_img_url_large":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png", "menu_provider":
        "single_platform", "id":"end-of-the-line-cafe-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/a5221e66bc70/ico/stars/v1/stars_small_4_half.png"
    },{
        "is_claimed":false, "distance":3484.8705422074972, "mobile_url":
        "http://m.yelp.com/biz/surf-burger-pensacola-beach-2", "rating_img_url":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/34bc8086841c/ico/stars/v1/stars_3.png", "review_count":
        44, "name":"Surf Burger", "snippet_image_url":
        "http://s3-media1.fl.yelpcdn.com/photo/FjxIMFp4AChdUk5_H8pJSg/ms.jpg", "rating":3.0, "url":
        "http://www.yelp.com/biz/surf-burger-pensacola-beach-2", "location":{
            "city":"Pensacola Beach", "display_address":[
            "500 Quietwater Beach Blvd", "Pensacola Beach, FL 32561"],"geo_accuracy":
            8.0, "postal_code":"32561", "country_code":"US", "address":[
            "500 Quietwater Beach Blvd"],"coordinate":{
                "latitude":30.335979500000001, "longitude":-87.143302899999995
            },"state_code":"FL"
        },"menu_date_updated":1387666035, "phone":"8509321417", "snippet_text":
        "Really great place to grab a burger and beer on the beach! Great staff and atmosphere with an awesome view!", "image_url":
        "http://s3-media1.fl.yelpcdn.com/bphoto/ArjjqRRAFupLOKFdFQvn0A/ms.jpg", "categories":[[
        "Burgers", "burgers"]],"display_phone":"+1-850-932-1417", "rating_img_url_large":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/e8b5b79d37ed/ico/stars/v1/stars_large_3.png", "menu_provider":
        "single_platform", "id":"surf-burger-pensacola-beach-2", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/902abeed0983/ico/stars/v1/stars_small_3.png"
    },{
        "is_claimed":true, "distance":4175.7901185272394, "mobile_url":
        "http://m.yelp.com/biz/riptides-sports-grill-pensacola-beach", "rating_img_url":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/5ef3eb3cb162/ico/stars/v1/stars_3_half.png", "review_count":
        22, "name":"Riptides Sports Grill", "snippet_image_url":
        "http://s3-media1.fl.yelpcdn.com/photo/9HiDhCa9SycP2fE7o5PXYQ/ms.jpg", "rating":3.5, "url":
        "http://www.yelp.com/biz/riptides-sports-grill-pensacola-beach", "location":{
            "city":"Pensacola Beach", "display_address":[
            "14 Via de Luna Dr", "Pensacola Beach, FL 32561"],"geo_accuracy":8.0, "postal_code":
            "32561", "country_code":"US", "address":["14 Via de Luna Dr"],"coordinate":{
                "latitude":30.333083999999999, "longitude":-87.136070000000004
            },"state_code":"FL"
        },"menu_date_updated":1409110918, "phone":"8509325331", "snippet_text":
        "Great place for quick service food while staying at the holiday Inn. Kids under 12 eat breakfast for free. Coffee is available in the lobby in the mornings...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/7Sj-MP6VU6EKKHS6qsdfow/ms.jpg", "categories":[[
        "American (Traditional)", "tradamerican"]],"display_phone":
        "+1-850-932-5331", "rating_img_url_large":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png", "menu_provider":
        "single_platform", "id":"riptides-sports-grill-pensacola-beach", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/2e909d5d3536/ico/stars/v1/stars_small_3_half.png"
    },{
        "is_claimed":false, "distance":9172.9558838699395, "mobile_url":
        "http://m.yelp.com/biz/city-grocery-pensacola", "rating_img_url":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/99493c12711e/ico/stars/v1/stars_4_half.png", "review_count":
        21, "name":"City Grocery", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/assets/srv0/yelp_styleguide/cc4afe21892e/assets/img/default_avatars/user_medium_square.png", "rating":
        4.5, "url":"http://www.yelp.com/biz/city-grocery-pensacola", "location":{
            "city":"Pensacola", "display_address":["2050 N 12th Ave", "Pensacola, FL 32503"],
            "geo_accuracy":8.0, "postal_code":"32503", "country_code":"US", "address":[
            "2050 N 12th Ave"],"coordinate":{
                "latitude":30.435963000000001, "longitude":-87.205078
            },"state_code":"FL"
        },"phone":"8504698100", "snippet_text":
        "We have eaten here several times and the sandwiches are always great. I too wish you could pay while you wait for your sandwich, but it doesn't bother me...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/8nFMSDY8m8ryIF3FVhfReQ/ms.jpg", "categories":[[
        "Grocery", "grocery"]],"display_phone":"+1-850-469-8100", "rating_img_url_large":
        "http://s3-media4.fl.yelpcdn.com/assets/2/www/img/9f83790ff7f6/ico/stars/v1/stars_large_4_half.png", "id":
        "city-grocery-pensacola", "is_closed":false, "rating_img_url_small":
        "http://s3-media2.fl.yelpcdn.com/assets/2/www/img/a5221e66bc70/ico/stars/v1/stars_small_4_half.png"
    },{
        "is_claimed":true, "distance":6564.9654850464658, "mobile_url":
        "http://m.yelp.com/biz/jacos-bayfront-bar-and-grille-pensacola", "rating_img_url":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/5ef3eb3cb162/ico/stars/v1/stars_3_half.png", "review_count":
        93, "name":"Jaco's Bayfront Bar \u0026 Grille", "snippet_image_url":
        "http://s3-media2.fl.yelpcdn.com/photo/s5WR33OKqTZ8pwwgCRBXdQ/ms.jpg", "rating":3.5, "url":
        "http://www.yelp.com/biz/jacos-bayfront-bar-and-grille-pensacola", "location":{
            "city":"Pensacola", "display_address":["997 S Palafox St", "Pensacola, FL 32502"],
            "geo_accuracy":8.0, "postal_code":"32502", "country_code":"US", "address":[
            "997 S Palafox St"],"coordinate":{
                "latitude":30.402633999999999, "longitude":-87.213408999999999
            },"state_code":"FL"
        },"menu_date_updated":1423126012, "phone":"8504325226", "snippet_text":
        "I feel like when your review restaurants in Pensacola, you have to remember that they're in Pensacola. Me and my wife have been here more times than I can...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/gX06S_LuzpJRSCCzOLKoRQ/ms.jpg", "categories":[[
        "American (New)", "newamerican"],["Seafood", "seafood"]],"display_phone":
        "+1-850-432-5226", "rating_img_url_large":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png", "menu_provider":
        "single_platform", "id":"jacos-bayfront-bar-and-grille-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/2e909d5d3536/ico/stars/v1/stars_small_3_half.png"
    },{
        "is_claimed":false, "distance":6970.464300373993, "mobile_url":
        "http://m.yelp.com/biz/the-happy-pig-cafe-pensacola", "rating_img_url":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/5ef3eb3cb162/ico/stars/v1/stars_3_half.png", "review_count":
        51, "name":"The Happy Pig Cafe", "snippet_image_url":
        "http://s3-media4.fl.yelpcdn.com/photo/k2ZsigxMF6Fl9_i9zirwig/ms.jpg", "rating":3.5, "url":
        "http://www.yelp.com/biz/the-happy-pig-cafe-pensacola", "location":{
            "city":"Pensacola", "display_address":["200 S Alcaniz St", "Pensacola, FL 32502"],
            "geo_accuracy":9.5, "postal_code":"32502", "country_code":"US", "address":[
            "200 S Alcaniz St"],"coordinate":{
                "latitude":30.410504208072801, "longitude":-87.209601402282701
            },"state_code":"FL"
        },"menu_date_updated":1387662946, "phone":"8509128480", "snippet_text":
        "We are from out of town had lunch here today. It was outstanding!  Their pork bbq is delicious, and their mac \u0026 cheese is homemade!  The BEST mac \u0026 cheese...", "image_url":
        "http://s3-media2.fl.yelpcdn.com/bphoto/-B5G1PrXrfuh7hL0mEoeVw/ms.jpg", "categories":[[
        "Barbeque", "bbq"]],"display_phone":"+1-850-912-8480", "rating_img_url_large":
        "http://s3-media3.fl.yelpcdn.com/assets/2/www/img/bd9b7a815d1b/ico/stars/v1/stars_large_3_half.png", "menu_provider":
        "single_platform", "id":"the-happy-pig-cafe-pensacola", "is_closed":
        false, "rating_img_url_small":
        "http://s3-media1.fl.yelpcdn.com/assets/2/www/img/2e909d5d3536/ico/stars/v1/stars_small_3_half.png"
    }]}

*/
}


