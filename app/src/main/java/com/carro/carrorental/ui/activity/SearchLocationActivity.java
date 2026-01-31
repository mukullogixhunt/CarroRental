/*
package com.logixhunt.carrorental.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.logixhunt.carrorental.R;
import com.logixhunt.carrorental.api.ApiInterface;
import com.logixhunt.carrorental.api.MapsApiClient;
import com.logixhunt.carrorental.api.response.PlaceDetailsResponse;
import com.logixhunt.carrorental.api.response.PlacesAutocompleteResponse;
import com.logixhunt.carrorental.databinding.ActivitySearchLocationBinding;
import com.logixhunt.carrorental.listener.SearchPlaceClickListener;
import com.logixhunt.carrorental.model.PlaceDetailsModel;
import com.logixhunt.carrorental.model.PredictionModel;
import com.logixhunt.carrorental.ui.adapter.SearchLocationAdapter;
import com.logixhunt.carrorental.ui.common.BaseActivity;
import com.logixhunt.carrorental.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchLocationActivity extends BaseActivity implements SearchPlaceClickListener {

    ActivitySearchLocationBinding binding;
    List<PredictionModel> predictionModels = new ArrayList<>();
    PlaceDetailsModel placeDetailsModel = new PlaceDetailsModel();
    SearchLocationAdapter searchLocationAdapter;
    String inputType, fromFragment, mainText;


    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY_MS = 500; // 500ms delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchLocationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSearchPreference();
    }

    private void getSearchPreference() {
        inputType = getIntent().getStringExtra(Constant.BundleExtras.INPUT_TYPE);
        fromFragment = getIntent().getStringExtra(Constant.BundleExtras.FROM_FRAGMENT);

        initiateSearchLocation();
    }

    private void initiateSearchLocation() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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


        Call<PlacesAutocompleteResponse> call = service.getPlaceAutocomplete(
                input,
                Constant.GOOGLE_MAP_API_KEY
        );

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvLocations.setLayoutManager(linearLayoutManager);
        searchLocationAdapter = new SearchLocationAdapter(SearchLocationActivity.this, predictionModels, this);
        binding.rvLocations.setAdapter(searchLocationAdapter);
        searchLocationAdapter.notifyDataSetChanged();
    }


    private void getPlaceDetails(String placeId) {

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiInterface service = retrofit.create(ApiInterface.class);

        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);

        Call<PlaceDetailsResponse> call = service.getPlaceDetails(
                placeId,
                Constant.GOOGLE_MAP_API_KEY
        );

//    showLoader();

        call.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {

                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body() != null) {

                            placeDetailsModel = response.body().getResult();

                            backToSamePage(String.valueOf(placeDetailsModel.getGeometry().getLocation().getLat()), String.valueOf(placeDetailsModel.getGeometry().getLocation().getLng()));

                        } else {
                            // Handle failure code
                            Log.e("getPlaceDetails", "Response Code: " + response.code());
                        }

                    } else {
                        // Handle failure code
                        Log.e("getPlaceDetails", "Response Code: " + response.code());
                    }
                } catch (Exception e) {
//                hideLoader();
                    Log.e("getPlaceDetails", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
//            hideLoader();
                Log.e("getPlaceDetails", "onFailure: " + t.toString());
            }
        });
    }

    private void backToSamePage(String lat, String lng) {

            Intent intent = new Intent(SearchLocationActivity.this, MainActivity.class);
            intent.putExtra(Constant.BundleExtras.ADDRESS_TYPE, inputType);
            intent.putExtra(Constant.BundleExtras.ADDRESS_FROM, fromFragment);
            if (inputType.equals("1")) {
                intent.putExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK, mainText);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, lat);
                intent.putExtra(Constant.BundleExtras.LNG_PICK, lng);
            } else {
                intent.putExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP, mainText);
                intent.putExtra(Constant.BundleExtras.LAT_DROP, lat);
                intent.putExtra(Constant.BundleExtras.LNG_DROP, lng);
            }
            startActivity(intent);
            finish();
    }


    @Override
    public void onPlaceClick(PredictionModel predictionModel) {
        mainText = predictionModel.getDescription();
        getPlaceDetails(predictionModel.getPlaceId());

    }


}*/



package com.carro.carrorental.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.MapsApiClient;
import com.carro.carrorental.api.response.PlaceDetailsResponse;
import com.carro.carrorental.api.response.PlacesAutocompleteResponse;
import com.carro.carrorental.databinding.ActivitySearchLocationBinding;
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

public class SearchLocationActivity extends BaseActivity implements SearchPlaceClickListener {

    private ActivitySearchLocationBinding binding;
    private List<PredictionModel> predictionModels = new ArrayList<>();
    private PlaceDetailsModel placeDetailsModel = new PlaceDetailsModel();
    private SearchLocationAdapter searchLocationAdapter;
    private String inputType, fromFragment, mainText;

    // --- NEW: A flag to determine the return method ---
    private boolean isLaunchedForResult = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DEBOUNCE_DELAY_MS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchLocationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSearchPreference();
    }

    private void getSearchPreference() {
        inputType = getIntent().getStringExtra(Constant.BundleExtras.INPUT_TYPE);
        fromFragment = getIntent().getStringExtra(Constant.BundleExtras.FROM_FRAGMENT);

        // --- NEW: Check if this activity was started for a result ---
        // We can pass an extra from the calling activity to know this.
        isLaunchedForResult = getIntent().getBooleanExtra(Constant.BundleExtras.FROM_ACTIVITY, false);

        initiateSearchLocation();
    }

    private void initiateSearchLocation() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

        binding.etserach.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.etserach, InputMethodManager.SHOW_IMPLICIT);

        binding.etserach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString().trim();
                if (!currentText.isEmpty()) {
                    searchRunnable = () -> searchPlaces(currentText);
                    handler.postDelayed(searchRunnable, DEBOUNCE_DELAY_MS);
                } else {
                    predictionModels.clear();
                    if (searchLocationAdapter != null) searchLocationAdapter.notifyDataSetChanged();
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.rvLocations.setVisibility(View.GONE);
                }
            }
        });
    }

    private void searchPlaces(String input) {
        binding.lvNoData.setVisibility(View.VISIBLE);
        binding.rvLocations.setVisibility(View.GONE);
        String latt = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_LATT, this);
        String lng = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_LONG, this);
        String radius = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_RADIUS, this);


        ApiInterface service = MapsApiClient.getClient().create(ApiInterface.class);

        // Assuming you have the country restriction logic here
        Call<PlacesAutocompleteResponse> call = service.getPlaceAutocompleteNearBy(
                input,
                latt+","+lng,
                radius,
                "true",
                "establishment",
                Constant.GOOGLE_MAP_API_KEY

                );

        call.enqueue(new Callback<PlacesAutocompleteResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlacesAutocompleteResponse> call, @NonNull Response<PlacesAutocompleteResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.lvNoData.setVisibility(View.GONE);
                        binding.rvLocations.setVisibility(View.VISIBLE);
                        predictionModels.clear();
                        predictionModels.addAll(response.body().getPredictions());
                        initiateSearchRecyclerView();
                    } else {
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        binding.rvLocations.setVisibility(View.GONE);
                        Log.e("SearchPlaces", "No predictions found or error. Code: " + response.code());
                    }
                } catch (Exception e) {
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.rvLocations.setVisibility(View.GONE);
                    Log.e("SearchPlaces", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesAutocompleteResponse> call, @NonNull Throwable t) {
                binding.lvNoData.setVisibility(View.VISIBLE);
                binding.rvLocations.setVisibility(View.GONE);
                Log.e("SearchPlaces", "onFailure: " + t.toString());
            }
        });
    }

    private void initiateSearchRecyclerView() {
        if (searchLocationAdapter == null) {
            binding.rvLocations.setLayoutManager(new LinearLayoutManager(this));
            searchLocationAdapter = new SearchLocationAdapter(this, predictionModels, this);
            binding.rvLocations.setAdapter(searchLocationAdapter);
        } else {
            searchLocationAdapter.notifyDataSetChanged();
        }
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
                        backToCallingScreen(
                                String.valueOf(placeDetailsModel.getGeometry().getLocation().getLat()),
                                String.valueOf(placeDetailsModel.getGeometry().getLocation().getLng())
                        );
                    } else {
                        Log.e("getPlaceDetails", "Response Code: " + response.code());
                        Toast.makeText(SearchLocationActivity.this, "Could not get place details.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("getPlaceDetails", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsResponse> call, @NonNull Throwable t) {
                hideLoader();
                Log.e("getPlaceDetails", "onFailure: " + t.toString());
                Toast.makeText(SearchLocationActivity.this, "Failed to get place details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method now decides how to return the result.
     * If started for result, it uses setResult().
     * Otherwise, it uses the old startActivity() logic.
     */
    private void backToCallingScreen(String lat, String lng) {
        if (isLaunchedForResult) {
            // --- NEW LOGIC ---
            // This is for SelfCarDetailActivity and similar cases
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", mainText);
            resultIntent.putExtra("latitude", lat);
            resultIntent.putExtra("longitude", lng);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        } else {
            // --- EXISTING LOGIC ---
            // This is for your older screens that depend on this
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constant.BundleExtras.ADDRESS_TYPE, inputType);
            intent.putExtra(Constant.BundleExtras.ADDRESS_FROM, fromFragment);
            if ("1".equals(inputType)) {
                intent.putExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK, mainText);
                intent.putExtra(Constant.BundleExtras.LAT_PICK, lat);
                intent.putExtra(Constant.BundleExtras.LNG_PICK, lng);
            } else {
                intent.putExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP, mainText);
                intent.putExtra(Constant.BundleExtras.LAT_DROP, lat);
                intent.putExtra(Constant.BundleExtras.LNG_DROP, lng);
            }
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPlaceClick(PredictionModel predictionModel) {
        mainText = predictionModel.getDescription();
        getPlaceDetails(predictionModel.getPlaceId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up the handler to prevent memory leaks
        if (handler != null && searchRunnable != null) {
            handler.removeCallbacks(searchRunnable);
        }
    }
}