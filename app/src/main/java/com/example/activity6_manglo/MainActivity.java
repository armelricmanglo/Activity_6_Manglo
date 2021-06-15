package com.example.activity6_manglo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity6_manglo.api.ApiUtilities;
import com.example.activity6_manglo.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirm, text_total_active, text_total_recovered, text_total_death, text_total_tests;
    TextView text_today_confirm, text_today_recovered, text_today_death;
    TextView text_date;
    PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();


        text_total_confirm = findViewById(R.id.txtview_totalconfirmed);
        text_total_active = findViewById(R.id.txtview_totalactive);
        text_total_recovered = findViewById(R.id.txtview_totalrecovered);
        text_total_death = findViewById(R.id.txtview_totaldeath);
        text_total_tests = findViewById(R.id.txtview_totaltests);

        text_today_confirm = findViewById(R.id.txtview_todayconfirmed);
        text_today_recovered = findViewById(R.id.txtview_todayrecovered);
        text_today_death = findViewById(R.id.txtview_todaydeath);

        text_date = findViewById(R.id.txtview_date);

        pieChart = findViewById(R.id.piechart);


        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals("Philippines")){
                                int total_confirm = Integer.parseInt(list.get(i).getCases());
                                int total_death = Integer.parseInt(list.get(i).getDeaths());
                                int total_recovered = Integer.parseInt(list.get(i).getRecovered());
                                int total_active = Integer.parseInt(list.get(i).getActive());
                                int total_tests = Integer.parseInt(list.get(i).getTests());

                                int today_confirm = Integer.parseInt(list.get(i).getTodayCases());
                                int today_death = Integer.parseInt(list.get(i).getTodayDeaths());
                                int today_recovered = Integer.parseInt(list.get(i).getTodayRecovered());

                                text_total_confirm.setText(NumberFormat.getInstance().format(total_confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(total_active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(total_recovered));
                                text_total_death.setText(NumberFormat.getInstance().format(total_death));
                                text_total_tests.setText(NumberFormat.getInstance().format(total_tests));

                                text_today_confirm.setText(NumberFormat.getInstance().format(today_confirm));
                                text_today_recovered.setText(NumberFormat.getInstance().format(today_recovered));
                                text_today_death.setText(NumberFormat.getInstance().format(today_death));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm", total_confirm, Color.parseColor("#4CAF50")));
                                pieChart.addPieSlice(new PieModel("Active", total_active, Color.parseColor("#FF2222")));
                                pieChart.addPieSlice(new PieModel("Recovered", total_recovered, Color.parseColor("#FF9800")));
                                pieChart.addPieSlice(new PieModel("Death", total_death, Color.parseColor("#9C27B0")));
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {

                    }
                });

    }

    private void setText(String updated){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy" );

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        text_date.setText("Updated at "+format.format(calendar.getTime()));
    }
}