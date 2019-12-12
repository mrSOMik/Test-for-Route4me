package com.somsoft.route4me_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.somsoft.route4me_test.Retrofit2.History;
import com.somsoft.route4me_test.Retrofit2.NetworkService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.somsoft.route4me_test.MainActivity.BASE_CURRENCY;

public class ChartActivity extends AppCompatActivity {
    final String WEB_DATE_FORMAT = "yyyy-MM-dd";
    final String MY_DATE_FORMAT = "dd.MM";
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    String currency;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Get currency name from previous activity
        Intent intent = getIntent();
        currency = intent.getStringExtra("currency");
        setTitle(currency);

        // Convert date from web format to my format and calculte range of history (last 7 days)
        DateFormat df = new SimpleDateFormat(WEB_DATE_FORMAT, Locale.getDefault());
        String start_at = df.format(new Date((new Date()).getTime() - 8 * 24 * 60 * 60 * 1000));
        String end_at = df.format(new Date());

        // Progress Dialog
        progressDialog = new ProgressDialog(ChartActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        // Get data from web site using Retrofit library
        NetworkService.getInstance()
                .getJSONApi()
                .getHystory(start_at, end_at, BASE_CURRENCY, currency)
                .enqueue(new Callback<History>() {
                    @Override
                    public void onResponse(Call<History> call, Response<History> response) {
                        TreeMap<String, TreeMap<String, Float>> history = response.body().getHistory();
                        if (history.size() > 0) {
                            int i = 0;
                            for (Map.Entry entry : history.entrySet()) {
                                TreeMap<String, Float> hd = (TreeMap<String, Float>) entry.getValue();
                                Float rate = hd.get(currency);
                                entries.add(new BarEntry(rate, i));
                                String date = entry.getKey().toString();
                                labels.add(TransformDate(date));
                                i++;
                            }
                            progressDialog.dismiss();
                            show_chart();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ChartActivity.this, "No exchange rate data is available for the selected currency.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<History> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error occurred while getting request!", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }


    // Show chart using MPAndroidChart library
    private void show_chart() {
        BarChart barChart = findViewById(R.id.barChart);
        BarDataSet bardataset = new BarDataSet(entries, "Rate by " + BASE_CURRENCY);
        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription(null);  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextSize(12);
        barChart.invalidate(); // refresh
    }

    public String TransformDate(String webDateStr) {
        String result = "?";
        DateFormat df = new SimpleDateFormat(WEB_DATE_FORMAT, Locale.getDefault());
        Date webDate = null;
        try {
            webDate = df.parse(webDateStr);
            DateFormat newDF = new SimpleDateFormat(MY_DATE_FORMAT, Locale.getDefault());
            result = newDF.format(webDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


}
