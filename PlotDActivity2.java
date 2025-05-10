package com.dev.mars_infra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dev.mars_infra.Fragment.DashBoardFragment;
import com.dev.mars_infra.Model.Block.ResponseBlockD;
import com.dev.mars_infra.Model.Plot;
import com.dev.mars_infra.Retrofit.Api;
import com.dev.mars_infra.Retrofit.Retrofit_Instance;
import com.dev.mars_infra.SliderAdapter.BlockDadapter;
import com.dev.mars_infra.SliderAdapter.PlotAdapter;
import com.dev.mars_infra.databinding.ActivityPlotDactivity2Binding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotDActivity2 extends AppCompatActivity {
ActivityPlotDactivity2Binding binding;
    private PlotAdapter plotAdapter;
    private List<Plot> plotList;

    private RecyclerView recyclerView;
    private BlockDadapter adapter;
    private List<ResponseBlockD> plotModelList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityPlotDactivity2Binding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        //initView();
        initview();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               replaceFragment(new DashBoardFragment());
            }
        });
    }

    private void initview() {
        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(PlotDActivity2.this, 4));
        plotModelList = new ArrayList<>(); // Initialize plotModelList
        adapter = new BlockDadapter(plotModelList, PlotDActivity2.this);
        recyclerView.setAdapter(adapter);
        progressBar = binding.progressBar;

        fetchDataFromApi(); // Move this line to fetch data after views initialization
    }

//    private void initView() {
//        // Initialize RecyclerView with grid layout
//
//        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
//
//        // Initialize data for RecyclerView
//        plotList = new ArrayList<>();
//
//        plotList.add(new Plot("D1-D3", "", Color.WHITE,false));
//        plotList.add(new Plot("D4-D20", "", Color.WHITE,false));
//        plotList.add(new Plot("D-21", "", Color.WHITE,false));
//        plotList.add(new Plot("D-22", "", Color.WHITE,false));
//        plotList.add(new Plot("D23-D52", "", Color.WHITE,false));
//        plotList.add(new Plot("D-53", "", Color.WHITE,false));
//        plotList.add(new Plot("D-54", "", Color.WHITE,false));
//
//        plotList.add(new Plot("D55-D73", "", Color.WHITE,false));
//        plotList.add(new Plot("D74-D82", "", Color.WHITE,false));
//        plotList.add(new Plot("D-84", "", Color.WHITE,false));
//        plotList.add(new Plot("D85-D90", "", Color.WHITE,false));
//        plotList.add(new Plot("D91-D94", "", Color.WHITE,false));
//        plotList.add(new Plot("D-95", "", Color.WHITE,false));
//        plotList.add(new Plot("D96-D104", "", Color.WHITE,false));
//
//
//        // Initialize adapter
//        plotAdapter = new PlotAdapter(plotList);
//
//        // Set adapter to RecyclerView
//        binding.recyclerview.setAdapter(plotAdapter);
//    }


    private void fetchDataFromApi() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        Api api = Retrofit_Instance.getClient().create(Api.class);
        Call<List<ResponseBlockD>> call = api.GetblockD();
        call.enqueue(new Callback<List<ResponseBlockD>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ResponseBlockD>> call, @NonNull Response<List<ResponseBlockD>> response) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if (response.isSuccessful()) {
                    List<ResponseBlockD> responseBody = response.body();
                    if (responseBody != null) {
                        adapter.setData(responseBody); // Set data to adapter
                    } else {
                        Toast.makeText(PlotDActivity2.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data: " + response.code());
                    Toast.makeText(PlotDActivity2.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseBlockD>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(PlotDActivity2.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}