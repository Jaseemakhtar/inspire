package com.jsync.freebook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener{
    int number;
    String  userName,phone;
    int amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        startPayment();
    }

    private void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();

        Intent intent = getIntent();
        userName = intent.getStringExtra("NAME");
        phone = intent.getStringExtra("PHONE");

        number = intent.getIntExtra("ITEM_NUMBER",0);
        String amountString = intent.getStringExtra("AMOUNT");
        amount = Integer.parseInt(amountString);
        amount *= 100;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "FreeBook Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", userName + "@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent res = new Intent();
        res.putExtra("ITEM_NUMBER",number);
        Toast.makeText(this,"Payment Successfull",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK,res);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        /*Toast.makeText(this,"Payment Failed",Toast.LENGTH_SHORT).show();*/
        setResult(RESULT_CANCELED);
        finish();
    }
}
