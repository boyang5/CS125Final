package com.example.cs125final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private static final String TAG = "cs125final:Main";
    private TextView startView;
    private TextView endView;
    private TextView amountView;
    private TextView outputView;
    private String startCurrency;
    private String endCurrency;
    private double exchangeAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = findViewById(R.id.start);
        endView = findViewById(R.id.end);
        amountView = findViewById(R.id.amount);
        outputView = findViewById(R.id.result);
        requestQueue = Volley.newRequestQueue(this);

        Button convert = findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Generate Button was pressed.");
                try {
                    startCurrency = startView.getText().toString();
                    startAPICall(startCurrency);
                } catch (Exception ignored) { }
            }
        });
    }

    private void startAPICall(final String start) {
        try {
            String URL = "https://api.exchangeratesapi.io/latest?base=" + start;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            finishAPICall(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    outputView.setText("error with web request");
                }
            }
            );
            requestQueue.add(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishAPICall (JSONObject response) {
        try {
            JSONObject result = response.getJSONObject("rates");
            endCurrency =  endView.getText().toString();
            exchangeAmount = Double.valueOf(amountView.getText().toString());
            double rate = result.getDouble(endCurrency);
            outputView.setText(Double.toString(exchangeAmount * rate));
        } catch (JSONException ignored) { }
    }
}