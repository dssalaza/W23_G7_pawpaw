package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CheckoutActivity extends AppCompatActivity {

    TextView orderID_label;
    Button confirm_btn;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //get the orderID from the query parameter
        Uri redirectUri = getIntent().getData();
        orderID = redirectUri.getQueryParameter("token");

        //set the orderID string to the UI
        orderID_label = (TextView) findViewById(R.id.orderID);
        orderID_label.setText("Order ID: " +orderID);

        //add an onClick listener to the confirm button
        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                captureOrder(orderID); //function to finalize the payment
            }
        });
    }

    void captureOrder(String orderID){
        //get the accessToken from MainActivity
        String accessToken = MainActivity.getMyAccessToken();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.post("https://api-m.sandbox.paypal.com/v2/checkout/orders/"+orderID+"/capture", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("RESPONSE", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    //redirect back to home page of app
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}