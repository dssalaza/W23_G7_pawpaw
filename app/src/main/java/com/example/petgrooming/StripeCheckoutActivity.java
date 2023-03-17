package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StripeCheckoutActivity extends AppCompatActivity {
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;

    Button stripeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_checkout);
        fetchApi();
        stripeButton = findViewById(R.id.pay_now);
        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentIntentClientSecret != null) {
                    paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, new PaymentSheet.Configuration("Codes Easy", configuration));
                }
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
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchApi(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://demo.codeseasy.com/apis/stripe/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")

                            );
                                    paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey") );
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}