package com.somsoft.route4me_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.somsoft.route4me_test.Retrofit2.AllRates;
import com.somsoft.route4me_test.Retrofit2.NetworkService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final static String BASE_CURRENCY = "USD";
    final static String TIMESTAMP = "TIMESTAMP";

    RecyclerView rv;
    ArrayList<Pair> list = new ArrayList<>();
    ProgressDialog progressDialog;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Progress Dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        // Get timestamp from shared preferences
        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        long currentTime = new Date().getTime();
        long lastTime = prefs.getLong(TIMESTAMP, currentTime);

        // is it first start of app or 10 minutes elapsed since the last request?
        if (currentTime == lastTime || currentTime - lastTime >= 10 * 60 * 1000) {
            // Get data from web site using Retrofit library
            NetworkService.getInstance()
                    .getJSONApi()
                    .getAllRates(BASE_CURRENCY)
                    .enqueue(new Callback<AllRates>() {
                        @Override
                        public void onResponse(Call<AllRates> call, Response<AllRates> response) {
                            // if success - convert data to ArrayList (to show lately in List)
                            TreeMap<String, Float> map = response.body().getRates();
                            for (Map.Entry<String, Float> rate : map.entrySet()) {
                                list.add(new Pair(rate.getKey(), rate.getValue()));
                            }
                            progressDialog.dismiss();
                            init_views(); // Show data in the list
                            saveDatas(); // Save data to DB
                        }

                        @Override
                        public void onFailure(Call<AllRates> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error occurred while getting request!", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
        } else {
            loadDatas();                // Load data from DB
            progressDialog.dismiss();
            init_views();               // Show data in the list
        }

    } // onCreate

    // Init and view the List
    private void init_views() {
        rv = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new MyAdapter(list);
        rv.setAdapter(adapter);
    }

    // Save data to DB (I decided to use the preference file, it uses the format "key-value")
    private void saveDatas() {
        SharedPreferences db = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = db.edit();
        editor.clear().apply();
        for (Pair pair : list) {
            editor.putFloat(pair.getCurrency(), pair.getRate());
        }
        editor.apply();
        // Save the current time to shared preferences
        prefs.edit().putLong(TIMESTAMP, new Date().getTime()).apply();
    }

    // Load data from DB
    private void loadDatas() {
        SharedPreferences db = getPreferences(MODE_PRIVATE);
        Map<String, ?> allPairs = db.getAll();
        for (Map.Entry pair : allPairs.entrySet()) {
            String currency = (String) pair.getKey();
            Float rate = (Float) pair.getValue();
            list.add(new Pair(currency, rate));
        }
        // Sort currency names alphabetically
        Collections.sort(list, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return a.getCurrency().compareTo(b.getCurrency());
            }
        });
    }

}