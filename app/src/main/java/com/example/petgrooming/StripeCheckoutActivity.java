package com.example.petgrooming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StripeCheckoutActivity extends AppCompatActivity {
    PaymentSheet paymentSheet;
    PaymentSheet.CustomerConfiguration configuration;
    Button stripeButton;
    String ephemeralKey;
    String customerIDStripe;
    String clientSecret;
    final String STRIPE_API_BASE_URL = "https://api.stripe.com/v1";
    final String STRIPE_SECRET_KEY = "<STRIPE_SECRET_KEY>";
    final String STRIPE_PUBLISHABLE_KEY = "<STRIPE_PUBLISHABLE_KEY>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_checkout);
        getStripeCustomerID();
        stripeButton = findViewById(R.id.pay_now);
        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
                paymentSheet.presentWithPaymentIntent(clientSecret,
                        new PaymentSheet.Configuration("PAW PAW", configuration));
            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof  PaymentSheetResult.Canceled){
            Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Failed){
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Completed", Toast.LENGTH_SHORT).show();
        }
    }

    private void getStripeCustomerID(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                STRIPE_API_BASE_URL + "/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerIDStripe = object.getString("id");
                            getEphemeralKey(customerIDStripe);
                            Log.d("STRIPE","customerIDStripe retrieved");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("STRIPE","Error retrieving customerIDStripe" + error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+STRIPE_SECRET_KEY);
                return header;
            }
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "paw user"); // This is hardcoded for the time being
                params.put("email", "test@pawpaw.com"); // This is hardcoded for the time being
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getEphemeralKey(String customerIDStripe){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                STRIPE_API_BASE_URL + "/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ephemeralKey = object.getString("id");
                            getClientSecret(customerIDStripe);
                            Log.d("STRIPE","ephemeralKey retrieved");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("STRIPE","Error retrieving ephemeralKey" + error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+STRIPE_SECRET_KEY);
                header.put("Stripe-Version","2022-11-15");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",customerIDStripe);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getClientSecret (String customerID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                STRIPE_API_BASE_URL + "/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            clientSecret = object.getString("client_secret");
                            Log.d("STRIPE","clientSecret retrieved");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("STRIPE","Error retrieving clientSecret" + error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+STRIPE_SECRET_KEY);
                return header;
            }
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", Double.toString(101).replace(".",""));
                params.put("currency","cad");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void startPayment(){
        configuration = new PaymentSheet.CustomerConfiguration(
                customerIDStripe,
                ephemeralKey
        );
        PaymentConfiguration.init(getApplicationContext(), STRIPE_PUBLISHABLE_KEY);
    }
}