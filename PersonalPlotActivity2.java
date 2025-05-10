package com.dev.mars_infra;
import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.dev.mars_infra.Fragment.DashBoardFragment;
import com.dev.mars_infra.Model.Plot;
import com.dev.mars_infra.SliderAdapter.PlotAdapter;
import com.dev.mars_infra.databinding.ActivityPersonalPlot2Binding;
import java.util.ArrayList;
import java.util.List;


public class PersonalPlotActivity2 extends AppCompatActivity {
    private ActivityPersonalPlot2Binding binding;
    private PlotAdapter plotAdapter;
    private List<Plot> plotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalPlot2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DashBoardFragment());
            }
        });
    }

    private void initView() {
        // Initialize RecyclerView with grid layout
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 4));

        // Initialize data for RecyclerView
        plotList = new ArrayList<>();
        plotList.add(new Plot("COM.10", "", Color.WHITE, false));
        plotList.add(new Plot("MARKETING OFFICE", "", Color.WHITE, false));

        // Initialize adapter
        plotAdapter = new PlotAdapter(plotList);

        // Set adapter to RecyclerView
        binding.recyclerview.setAdapter(plotAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}
