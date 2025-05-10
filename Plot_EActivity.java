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
import com.dev.mars_infra.Model.Block.ResponseBlockE;
import com.dev.mars_infra.Model.Plot;
import com.dev.mars_infra.Retrofit.Api;
import com.dev.mars_infra.Retrofit.Retrofit_Instance;
import com.dev.mars_infra.SliderAdapter.BlockEadapter;
import com.dev.mars_infra.SliderAdapter.PlotAdapter;
import com.dev.mars_infra.databinding.ActivityPlotEactivityBinding;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Plot_EActivity extends AppCompatActivity {
ActivityPlotEactivityBinding binding;
    private PlotAdapter plotAdapter;
    private List<Plot> plotList;

    private RecyclerView recyclerView;
    private BlockEadapter adapter;
    private List<ResponseBlockE> plotModelList;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityPlotEactivityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
       // initView();
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
        recyclerView.setLayoutManager(new GridLayoutManager(Plot_EActivity.this, 4));
        plotModelList = new ArrayList<>(); // Initialize plotModelList
        adapter = new BlockEadapter(plotModelList, Plot_EActivity.this);
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
//        plotList.add(new Plot("E1", "", Color.WHITE,false));
//        plotList.add(new Plot("E2-E10", "", Color.WHITE,false));
//        plotList.add(new Plot("E11", "", Color.WHITE,false));
//        plotList.add(new Plot("E12", "", Color.WHITE,false));
//        plotList.add(new Plot("E13-E21", "", Color.WHITE,false));
//        plotList.add(new Plot("E22", "", Color.WHITE,false));
//        plotList.add(new Plot("E31", "", Color.WHITE,false));
//
//        plotList.add(new Plot("E32-E45", "", Color.WHITE,false));
//        plotList.add(new Plot("E46", "", Color.WHITE,false));
//        plotList.add(new Plot("E47-E53", "", Color.WHITE,false));
//        plotList.add(new Plot("E54", "", Color.WHITE,false));
//        plotList.add(new Plot("E55", "", Color.WHITE,false));
//        plotList.add(new Plot("E56-E65", "", Color.WHITE,false));
//        plotList.add(new Plot("E66-E86", "", Color.WHITE,false));
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
        Call<List<ResponseBlockE>> call = api.GetblockE();
        call.enqueue(new Callback<List<ResponseBlockE>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ResponseBlockE>> call, @NonNull Response<List<ResponseBlockE>> response) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if (response.isSuccessful()) {
                    List<ResponseBlockE> responseBody = response.body();
                    if (responseBody != null) {
                        adapter.setData(responseBody); // Set data to adapter
                    } else {
                        Toast.makeText(Plot_EActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data: " + response.code());
                    Toast.makeText(Plot_EActivity.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseBlockE>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(Plot_EActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}