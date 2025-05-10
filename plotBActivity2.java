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
import com.dev.mars_infra.Model.Block.ResponseBlockB;
import com.dev.mars_infra.Retrofit.Api;
import com.dev.mars_infra.Retrofit.Retrofit_Instance;
import com.dev.mars_infra.SliderAdapter.BlockBAdapter;
import com.dev.mars_infra.databinding.ActivityPlotBactivity2Binding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class plotBActivity2 extends AppCompatActivity {
    ActivityPlotBactivity2Binding binding;
    private RecyclerView recyclerView;
    private BlockBAdapter adapter;
    private List<ResponseBlockB> plotModelList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlotBactivity2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();
        fetchDataFromApi();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DashBoardFragment());
            }
        });
    }

    private void initview() {
        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(plotBActivity2.this, 4));
        plotModelList = new ArrayList<>(); // Initialize plotModelList
        adapter = new BlockBAdapter(plotModelList, plotBActivity2.this);
        recyclerView.setAdapter(adapter);
        progressBar = binding.progressBar;

        fetchDataFromApi(); // Move this line to fetch data after views initialization
    }


    private void fetchDataFromApi() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        Api api = Retrofit_Instance.getClient().create(Api.class);
        Call<List<ResponseBlockB>> call = api.GetblockB();
        call.enqueue(new Callback<List<ResponseBlockB>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ResponseBlockB>> call, @NonNull Response<List<ResponseBlockB>> response) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar

                if (response.isSuccessful()) {
                    List<ResponseBlockB> responseBody = response.body();
                    if (responseBody != null) {
                        adapter.setData(responseBody); // Set data to adapter
                    } else {
                        Toast.makeText(plotBActivity2.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data: " + response.code());
                    Toast.makeText(plotBActivity2.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseBlockB>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                Log.e("API Error", "Failed to fetch data: " + t.getMessage());
                Toast.makeText(plotBActivity2.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
