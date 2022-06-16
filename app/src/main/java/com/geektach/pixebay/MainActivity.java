package com.geektach.pixebay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Visibility;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.geektach.pixebay.adapter.ImageAdapter;
import com.geektach.pixebay.databinding.ActivityMainBinding;
import com.geektach.pixebay.network.RetrofitServices;
import com.geektach.pixebay.network.model.Hit;
import com.geektach.pixebay.network.model.PixabayModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageAdapter adapter;
    ActivityMainBinding binding;
    RetrofitServices retrofitServices;
    public static final String KEY = "27653329-27bc214295bc7b10046341f78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitServices = new RetrofitServices();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initClickers();
    }

    private void initClickers() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String word = binding.etSearh.getText().toString();
                getImageFromApi(word, 1, 20);
            }
        });
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int count = 1;
                String word = binding.etSearh.getText().toString();
                getImageFromApi(word, ++count, 5);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getImageFromApi(String word, int page, int perPage) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        retrofitServices.getApi().getImages(KEY, word, page, perPage).enqueue(new Callback<PixabayModel>() {
            @Override
            public void onResponse(@NonNull Call<PixabayModel> call, @NonNull Response<PixabayModel> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    assert response.body() != null;
                    adapter = new ImageAdapter((ArrayList<Hit>) response.body().getHits());
                    binding.recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixabayModel> call, @NonNull Throwable t) {
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }
}
