package com.example.gyroapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        Button searchButton = findViewById(R.id.searchButton);
        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;

                // 선택된 날짜 정보를 사용하여 원하는 작업을 수행합니다.
                String selectedDate = year + "-" + month + "-" + dayOfMonth;

                JSONObject postParam = new JSONObject();
                try {
                    postParam.put("id", "1");
                    postParam.put("datetime", selectedDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Thread(() -> {
                    PostManager postManager = new PostManager("138.2.126.137");
                    JSONObject dayFromMonth, weekFromMonth, monthFromMonth;
                    dayFromMonth = postManager.sendPost(postParam.toString(), "request/day_from_month");
                    weekFromMonth = postManager.sendPost(postParam.toString(), "request/week_from_month");
                    monthFromMonth = postManager.sendPost(postParam.toString(), "request/month_from_year");
                    drawDayFromMonth(dayFromMonth);
                    drawWeekFromMonth(weekFromMonth);
                    drawMonthFromYear(monthFromMonth);
                }).start();
            }
        });


//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                JSONObject postParam = new JSONObject();
//                try {
//                    postParam.put("id", "1");
//                    postParam.put("datetime", "2023-04-25 01:30:00");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                new Thread(() -> {
//                    PostManager postManager = new PostManager("138.2.126.137");
//                    JSONObject dayFromMonth, weekFromMonth, monthFromMonth;
//                    dayFromMonth = postManager.sendPost(postParam.toString(), "request/day_from_month");
//                    weekFromMonth = postManager.sendPost(postParam.toString(), "request/week_from_month");
//                    monthFromMonth = postManager.sendPost(postParam.toString(), "request/month_from_year");
//                    drawDayFromMonth(dayFromMonth);
//                    drawWeekFromMonth(weekFromMonth);
//                    drawMonthFromYear(monthFromMonth);
//                }).start();
//            }
//        });

        new Thread(() -> {
            JSONObject updateParam = new JSONObject();
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("id", "1");
                postParam.put("data", "40");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PostManager postManager = new PostManager("138.2.126.137");
            JSONObject postRet = postManager.sendPost(postParam.toString(), "update/gyro");
        }).start();

    }

    public void drawDayFromMonth(JSONObject data)
    {
        LineChart lineChart = findViewById(R.id.chart1);

        List<Entry> entries = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = data.getJSONArray("data");

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                entries.add(new Entry(jo.getInt("dayofmonth"), (float)jo.getDouble("average")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "날짜");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis;
        yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

    public void drawWeekFromMonth(JSONObject data)
    {
        LineChart lineChart = findViewById(R.id.chart2);

        List<Entry> entries = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = data.getJSONArray("data");

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                entries.add(new Entry(jo.getInt("dayofweek"), (float)jo.getDouble("average")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "주차");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis;
        yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

    public void drawMonthFromYear(JSONObject data)
    {
        LineChart lineChart = (LineChart)findViewById(R.id.chart3);

        List<Entry> entries = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = data.getJSONArray("data");

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                entries.add(new Entry(jo.getInt("month"), (float)jo.getDouble("average")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "월");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis;
        yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}