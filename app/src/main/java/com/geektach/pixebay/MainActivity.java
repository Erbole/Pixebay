package com.geektach.pixebay;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geektach.pixebay.adapter.ImageAdapter;
import com.geektach.pixebay.databinding.ActivityMainBinding;
import com.geektach.pixebay.network.RetrofitServices;
import com.geektach.pixebay.network.model.Hit;
import com.geektach.pixebay.network.model.PixabayModel;

import java.util.ArrayList;

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

        binding.btnSearch.setOnClickListener(
                view -> {
                    if (binding.etSearh.getText().toString().isEmpty()) {
                        Animation snake = AnimationUtils.loadAnimation(getBaseContext(), R.anim.snake);
                        binding.etSearh.startAnimation(snake);
                        Toast.makeText(this, "Поле не должен быть пустым", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        String word = binding.etSearh.getText().toString();
                        getImageFromApi(word, 1, 20);
                    }
                });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            int count = 1;
            String word = binding.etSearh.getText().toString();
            getImageFromApi(word, ++count, 5);
            binding.swipeRefresh.setRefreshing(false);
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
