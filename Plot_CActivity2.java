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
import com.dev.mars_infra.Model.Block.ResponseBlockC;
import com.dev.mars_infra.Model.Plot;
import com.dev.mars_infra.Retrofit.Api;
import com.dev.mars_infra.Retrofit.Retrofit_Instance;
import com.dev.mars_infra.SliderAdapter.BlockCadapter;
import com.dev.mars_infra.SliderAdapter.PlotAdapter;
import com.dev.mars_infra.databinding.ActivityPlotCactivity2Binding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Plot_CActivity2 extends AppCompatActivity {
ActivityPlotCactivity2Binding binding;

    private PlotAdapter plotAdapter;
    private List<Plot> plotList;

    private RecyclerView recyclerView;
    private BlockCadapter adapter;
    private List<ResponseBlockC> plotModelList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityPlotCactivity2Binding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        initview();
       // initView();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               replaceFragment(new DashBoardFragment());
            }
        });
    }

    private void initview() {
        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(Plot_CActivity2.this, 4));
        plotModelList = new ArrayList<>(); // Initialize plotModelList
        adapter = new BlockCadapter(plotModelList, Plot_CActivity2.this);
        recyclerView.setAdapter(adapter);
        progressBar = binding.progressBar;

        fetchDataFromApi(); // Move this line to fetch data after views initialization
    }
    

    private void fetchDataFromApi() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        Api api = Retrofit_Instance.getClient().create(Api.class);
        Call<List<ResponseBlockC>> call = api.GetblockC();
        call.enqueue(new Callback<List<ResponseBlockC>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ResponseBlockC>> call, @NonNull Response<List<ResponseBlockC>> response) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if (response.isSuccessful()) {
                    List<ResponseBlockC> responseBody = response.body();
                    if (responseBody != null) {
                        adapter.setData(responseBody); // Set data to adapter
                    } else {
                        Toast.makeText(Plot_CActivity2.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data: " + response.code());
                    Toast.makeText(Plot_CActivity2.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseBlockC>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(Plot_CActivity2.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    private void initView() {
//        // Initialize RecyclerView with grid layout
//
//        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
//
//        // Initialize data for RecyclerView
//        plotList = new ArrayList<>();
//
//        plotList.add(new Plot("C-01", "", Color.WHITE,false));
//        plotList.add(new Plot("C02-C06", "", Color.WHITE,false));
//        plotList.add(new Plot("C-07", "", Color.WHITE,false));
//        plotList.add(new Plot("C08-C14", "", Color.WHITE,false));
//        plotList.add(new Plot("C-15", "", Color.WHITE,false));
//        plotList.add(new Plot("C16-C18", "", Color.WHITE,false));
//        plotList.add(new Plot("C19-C24", "", Color.WHITE,false));
//
//        plotList.add(new Plot("C-01", "", Color.WHITE,false));
//        plotList.add(new Plot("C25-C36", "", Color.WHITE,false));
//        plotList.add(new Plot("C-37", "", Color.WHITE,false));
//        plotList.add(new Plot("C-38", "", Color.WHITE,false));
//        plotList.add(new Plot("C-49", "", Color.WHITE,false));
//        plotList.add(new Plot("C-50", "", Color.WHITE,false));
//        plotList.add(new Plot("C-51", "", Color.WHITE,false));
//
//
//        // Initialize adapter
//        plotAdapter = new PlotAdapter(plotList);
//
//        // Set adapter to RecyclerView
//        binding.recyclerview.setAdapter(plotAdapter);
//    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


}