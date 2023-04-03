package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    Button btnDownloadPDF;

    TextView petInfo, apptDate, selectedPackage, totalPrice;
    HashMap<String, String> bookingInfo;

    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    PaymentSheet paymentSheet;

    PaymentSheet.CustomerConfiguration configuration;

    Button stripeButton;

    String ephemeralKey;

    String customerIDStripe;

    String totalPriceText,petInfoText;

    String clientSecret;

    final String STRIPE_API_BASE_URL = "https://api.stripe.com/v1";

    final String STRIPE_SECRET_KEY = "<STRIPE_SECRET_KEY>";

    final String STRIPE_PUBLISHABLE_KEY = "<STRIPE_PUBLISHABLE_KEY>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        bookingInfo = (HashMap<String, String>)getIntent().getSerializableExtra("sendInfoToCheckout");
        petInfo = findViewById(R.id.petInfoInput);
        apptDate = findViewById(R.id.apptDateInput);
        selectedPackage = findViewById(R.id.packageSelected);
        totalPrice = findViewById(R.id.totalPrice);





        for(String i: bookingInfo.keySet())
        {
            Log.d("values",bookingInfo.get(i));
        }
        petInfoText = bookingInfo.get("name") + ", "
                + bookingInfo.get("breed") + " "+ bookingInfo.get("type")
                + "\nSize: " + bookingInfo.get("size") +"\n"
                + "Age: " + bookingInfo.get("age")
                ;
        petInfo.setText(petInfoText);
        apptDate.setText(bookingInfo.get("selectedDate"));
        selectedPackage.setText(bookingInfo.get("PackageInfo"));

       if(bookingInfo.get("PackageInfo").equals("Basic Package"))
        {
            totalPriceText = "65.00";
            totalPrice.setText("$"+totalPriceText);
        }
        else
        {
            totalPriceText = "90.00";
            totalPrice.setText("$"+totalPriceText);
        }

        getStripeCustomerID();


        //custom - Sri: Start

        btnDownloadPDF = findViewById(R.id.btnDownloadPdf);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paw);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        stripeButton = findViewById(R.id.btnPay);

        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
                paymentSheet.presentWithPaymentIntent(clientSecret,
                        new PaymentSheet.Configuration("PAW PAW", configuration));
            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

       /* if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)&& (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }*/

        btnDownloadPDF.setOnClickListener((View v) -> {
                generatePDF();
        });

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


    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();


        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth,
                pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 346, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(this, R.color.black));

        //canvas.drawText("Excited to meet you soon!", 209, 100, title);
        //canvas.drawText("Paw Paw", 209, 80, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(30);
        int x = 209;
        int y = 260;

        //title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Order Number: ", 209, y, title);
        y += 60;
        canvas.drawText("Pet Name: "+bookingInfo.get("name"), 209, y, title);
        y += 60;
        canvas.drawText("Pet Breed: "+bookingInfo.get("breed"), 209, y, title);
        y += 60;
        canvas.drawText("Pet Type: "+bookingInfo.get("type"), 209, y, title);
        y += 60;
        canvas.drawText("Pet Size: "+bookingInfo.get("size"), 209, y, title);
        y += 60;
        canvas.drawText("Pet Age: "+bookingInfo.get("age"), 209, y, title);
        y += 60;
        canvas.drawText("Package: "+bookingInfo.get("PackageInfo"), 209, y, title);
        y += 60;
        canvas.drawText("Total Cost: "+totalPriceText, 209, y, title);

        pdfDocument.finishPage(myPage);

        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"ordersummary.pdf");
        // getFilesDir() is working
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM),
                "OrderSummary.pdf");
        // PDF Path is - /storage/emulated/0/Android/data/com.example.petgrooming/files/DCIM


        try {

            pdfDocument.writeTo(new FileOutputStream(file));
            Log.i("pdf path",getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM).toString());
            Toast.makeText(CheckOutActivity.this, "PDF file generated successfully.",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
/*
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).
                            show();
                } else {
                    Toast.makeText(this, "Permission Denied.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    // custom:Sri - End

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
                header.put("Authorization","Bearer "+ STRIPE_SECRET_KEY);
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
                header.put("Authorization","Bearer "+ STRIPE_SECRET_KEY);
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
                header.put("Authorization","Bearer "+ STRIPE_SECRET_KEY);
                return header;
            }
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String amount = totalPriceText;
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", amount.replace(".",""));
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


