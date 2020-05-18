package com.bani.ecggraph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private LineChart lineChart;
    public static String dataY;
    public static int i;
    public static ArrayList<Entry> yValues = new ArrayList<>();
    public static boolean isDrawn;
    public static int j=0;
    public static String newApi;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = findViewById(R.id.chart);
//        lineChart.setOnChartGestureListener(MainActivity.this);
//        lineChart.setOnChartValueSelectedListener(MainActivity.this);

        newApi = getIntent().getExtras().getString("Value");
        Toast.makeText(this, newApi, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(this);
        fetchData();
        isDrawn = true;
        final Handler handler = new Handler();
        final int delay = 1; //milliseconds

        handler.postDelayed(new Runnable(){
            int lol;
            public void run(){
                fetchData();
                lol++;
                //Log.d(TAG, "run: Fetched! "+lol+" data is: "+yValues); //Just for logging purpose
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    public void fetchData(){
        String apiUrl;
        if (!newApi.isEmpty()){
            apiUrl = newApi;
        } else {
            apiUrl = "PUT_YOUR_API_URL";
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (isDrawn){
                        lineChart.invalidate();
                        lineChart.clear();
                    }
                    for (i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataY = jsonObject.getString("key");
                        yValues.add(new Entry(j, Float.parseFloat(dataY)));
                        j=j+10;
                    }
                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(false);

                    LineDataSet set1 = new LineDataSet(yValues, "ECG");
                    set1.setFillAlpha(110);
                    set1.setColor(Color.RED);
                    set1.setLineWidth(1f);
                    set1.setDrawCircles(false);
                    Legend legend = lineChart.getLegend();
                    legend.setEnabled(false);
                    set1.setDrawValues(false);
                    lineChart.setDrawBorders(false);
                    lineChart.setDrawGridBackground(false);
                    lineChart.getAxisRight().setDrawLabels(false);
                    lineChart.getDescription().setEnabled(false);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.getXAxis().setLabelCount(10);
//                    float minXRange = 10;
//                    float maxXRange = 0;
//                    lineChart.setVisibleXRange(minXRange, maxXRange);


                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    LineData data = new LineData(dataSets);
                    if (set1.getEntryCount() >= 50) {
                        set1.removeFirst();
                        for (int i = 0; i < set1.getEntryCount(); i++) {
                            Entry entryToChange = set1.getEntryForIndex(i);
                            entryToChange.setX(entryToChange.getX() - 1);
                        }
                    }
                        lineChart.setData(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
