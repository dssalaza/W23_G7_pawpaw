package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    Button payPalButton;
    static String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getAccessToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payPalButton = findViewById(R.id.pp_btn);
        payPalButton.setOnClickListener((View view) -> {
            createOrder();
        });
    }

    String encodeStringToBase64(){
        String input = "<CLIENT_ID>:<CLIENT_SECRET>";
        String encodedString = null;
        encodedString = Base64.getEncoder().encodeToString(input.getBytes());
        return encodedString;
    }

    void getAccessToken(){
        String AUTH = encodeStringToBase64();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/x-www-form-urlencoded");
        client.addHeader("Authorization", "Basic "+ AUTH);
        String jsonString = "grant_type=client_credentials";

        HttpEntity entity = new StringEntity(jsonString, "utf-8");

        client.post(this, "https://api-m.sandbox.paypal.com/v1/oauth2/token", entity,"application/x-www-form-urlencoded", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("RESPONSE-getAccessToken: ", responseString);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jobj = new JSONObject(responseString);
                    accessToken = jobj.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        }


    void createOrder(){
        AsyncHttpClient client = new AsyncHttpClient();
        getAccessToken();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + getMyAccessToken());

        String order = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [\n" +
                "      {\n" +
                "        \"amount\": {\n" +
                "          \"currency_code\": \"SGD\",\n" +
                "          \"value\": \"1.00\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\"application_context\": {\n" +
                "        \"brand_name\": \"Test Store\",\n" +
                "        \"return_url\": \"studio://exit\",\n" +
                "        \"cancel_url\": \"https://www.example.com\"\n" +
                "    }}";

        HttpEntity entity = new StringEntity(order, "utf-8");

        client.post(this, "https://api.sandbox.paypal.com/v2/checkout/orders", entity, "application/json",new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("RESPONSE", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.i("Success RESPONSE", response);
                    try {
                        JSONArray links = new JSONObject(response).getJSONArray("links");

                        for (int i = 0; i < links.length(); ++i) {
                            String rel = links.getJSONObject(i).getString("rel");
                            if (rel.equals("approve")){
                                String link = links.getJSONObject(i).getString("href");
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(link));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        });
    }

    public static String getMyAccessToken(){
        return accessToken;
    }


}