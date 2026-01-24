package com.carro.carrorental.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.carro.carrorental.api.response.PlaceDetailsResponse;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.MapsApiClient;
import com.carro.carrorental.api.response.PlacesAutocompleteResponse;
import com.carro.carrorental.databinding.ActivitySearchAirportsBinding;
import com.carro.carrorental.listener.SearchPlaceClickListener;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.PlaceDetailsModel;
import com.carro.carrorental.model.PredictionModel;
import com.carro.carrorental.ui.adapter.SearchLocationAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAirportsActivity extends BaseActivity implements SearchPlaceClickListener {

    ActivitySearchAirportsBinding binding;
    List<PredictionModel> predictionModels = new ArrayList<>();
    PlaceDetailsModel placeDetailsModel = new PlaceDetailsModel();
    SearchLocationAdapter searchLocationAdapter;
    String airport_name;



    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY_MS = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchAirportsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });       initiateSearchLocation();
    }

    private void initiateSearchLocation() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        //// for open keyboard default//////////
        binding.etserach.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.etserach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchPlaces(s.toString());

                // 1. Remove any pending search queries
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 2. If text is not empty, schedule a new search after the delay
                String currentText = s.toString().trim();
                if (!currentText.isEmpty()) {
                    searchRunnable = () -> searchPlaces(currentText);
                    handler.postDelayed(searchRunnable, DEBOUNCE_DELAY_MS);
                } else {
                    // Optional: If text is empty, clear the results immediately
                    predictionModels.clear();
                    initiateSearchPlaces(); // Assuming this updates the adapter
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.rvLocations.setVisibility(View.GONE);
                }
            }
        });

    }

    private void searchPlaces(String input) {

        binding.lvNoData.setVisibility(View.VISIBLE);
        binding.rvLocations.setVisibility(View.GONE);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface service = retrofit.create(ApiInterface.class);

        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);

        Call<PlacesAutocompleteResponse> call = service.getAirports(
                "airport", input, Constant.GOOGLE_MAP_API_KEY);

        call.enqueue(new Callback<PlacesAutocompleteResponse>() {
            @Override
            public void onResponse(Call<PlacesAutocompleteResponse> call, Response<PlacesAutocompleteResponse> response) {

                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body() != null) {

                            binding.lvNoData.setVisibility(View.GONE);
                            binding.rvLocations.setVisibility(View.VISIBLE);

                            predictionModels.clear();
                            predictionModels.addAll(response.body().getPredictions());
                            initiateSearchPlaces();

                        } else {
                            // No predictions found
                            binding.lvNoData.setVisibility(View.VISIBLE);
                            binding.rvLocations.setVisibility(View.GONE);
                            Log.e("SearchPlaces", "No predictions found");
                        }
                    } else {
                        // Handle failure code
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        binding.rvLocations.setVisibility(View.GONE);
                        Log.e("SearchPlaces", "Response Code: " + response.code());
                    }
                } catch (Exception e) {
//                    hideLoader();
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.rvLocations.setVisibility(View.GONE);
                    Log.e("SearchPlaces", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(Call<PlacesAutocompleteResponse> call, Throwable t) {
//                hideLoader();
                binding.lvNoData.setVisibility(View.VISIBLE);
                binding.rvLocations.setVisibility(View.GONE);
                Log.e("SearchPlaces", "onFailure: " + t.toString());
            }
        });
    }

    private void initiateSearchPlaces() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchAirportsActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvLocations.setLayoutManager(linearLayoutManager);
        searchLocationAdapter = new SearchLocationAdapter(SearchAirportsActivity.this, predictionModels, this);
        binding.rvLocations.setAdapter(searchLocationAdapter);
        searchLocationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlaceClick(PredictionModel predictionModel) {
        airport_name = predictionModel.getDescription();
        getPlaceDetails(predictionModel.getPlaceId());

    }

    private void getPlaceDetails(String placeId) {
        showLoader();
        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);
        Call<PlaceDetailsResponse> call = service.getPlaceDetails(
                placeId,
                Constant.GOOGLE_MAP_API_KEY
        );

        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                hideLoader();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        placeDetailsModel = response.body().getResult();
                        PreferenceUtils.setString(Constant.PreferenceConstant.AIRPORT_NAME,airport_name,SearchAirportsActivity.this);
                        PreferenceUtils.setString(Constant.PreferenceConstant.AIRPORT_LAT,String.valueOf(placeDetailsModel.getGeometry().getLocation().getLat()),SearchAirportsActivity.this);
                        PreferenceUtils.setString(Constant.PreferenceConstant.AIRPORT_LONG,String.valueOf(placeDetailsModel.getGeometry().getLocation().getLng()),SearchAirportsActivity.this);
                        getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        Log.e("getPlaceDetails", "Response Code: " + response.code());
                        Toast.makeText(SearchAirportsActivity.this, "Could not get place details.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("getPlaceDetails", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e("getPlaceDetails", "onFailure: " + t.toString());
                Toast.makeText(SearchAirportsActivity.this, "Failed to get place details.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}