package com.carro.carrorental.ui.fragment.myBookings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.MapsApiClient;
import com.carro.carrorental.api.response.BranchResponse;
import com.carro.carrorental.api.response.MyBookingsResponse;
import com.carro.carrorental.api.response.PlacesAutocompleteResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.CancelBookingDialogBinding;
import com.carro.carrorental.databinding.EditBookingDialogBinding;
import com.carro.carrorental.databinding.FragmentUpcomingBinding;
import com.carro.carrorental.databinding.SearchLocationDialogBinding;
import com.carro.carrorental.listener.MyBookingClickListener;
import com.carro.carrorental.listener.SearchPlaceClickListener;
import com.carro.carrorental.model.BookingListModel;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.PredictionModel;
import com.carro.carrorental.ui.activity.BookingDetailsActivity;
import com.carro.carrorental.ui.adapter.BookingListAdapter;
import com.carro.carrorental.ui.adapter.SearchLocationAdapter;
import com.carro.carrorental.ui.common.BaseFragment;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends BaseFragment implements MyBookingClickListener, SearchPlaceClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentUpcomingBinding binding;
    String userId;
    Dialog dialog, dialog1, dialog2;
    String bking_Id;
    String bking_type;
    String name;
    String mobile;
    String email;
    String pick_address = "";
    String branch = "";
    String branch_name = "";
    BookingListAdapter bookingListAdapter;
    List<BookingListModel> bookingListModelList = new ArrayList<>();
    LoginModel loginModel = new LoginModel();
    SearchLocationDialogBinding searchLocationDialogBinding;
    List<PredictionModel> predictionModels = new ArrayList<>();
    SearchLocationAdapter searchLocationAdapter;
    EditBookingDialogBinding editBookingDialogBinding;
    List<BranchModel> branchModelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingBinding.inflate(getLayoutInflater());
        getPreferenceData();
        return binding.getRoot();
    }

    private void getPreferenceData() {
        userId = PreferenceUtils.getString(Constant.PreferenceConstant.USER_ID, getContext());
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, getContext());
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        initialization();
    }

    private void initialization() {

        getBookingApi();

    }

    private void getBookingApi() {

        showLoader();

        binding.lvNoData.setVisibility(View.VISIBLE);
        binding.rvUpcoming.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MyBookingsResponse> call = apiInterface.upcomingBooking(userId);
        call.enqueue(new Callback<MyBookingsResponse>() {
            @Override
            public void onResponse(Call<MyBookingsResponse> call, Response<MyBookingsResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            binding.lvNoData.setVisibility(View.GONE);
                            binding.rvUpcoming.setVisibility(View.VISIBLE);

                            bookingListModelList.clear();
                            bookingListModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvUpcoming.setLayoutManager(linearLayoutManager);
                            bookingListAdapter = new BookingListAdapter(getContext(), bookingListModelList, UpcomingFragment.this);
                            binding.rvUpcoming.setAdapter(bookingListAdapter);

                        } else {
                            hideLoader();
                            binding.lvNoData.setVisibility(View.VISIBLE);
                            binding.rvUpcoming.setVisibility(View.GONE);
                        }
                    } else {
                        hideLoader();
                        binding.lvNoData.setVisibility(View.VISIBLE);
                        binding.rvUpcoming.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();
                    binding.lvNoData.setVisibility(View.VISIBLE);
                    binding.rvUpcoming.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MyBookingsResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                binding.lvNoData.setVisibility(View.VISIBLE);
                binding.rvUpcoming.setVisibility(View.GONE);
                showError("Something went wrong");
            }
        });


    }

    private void getBranchAPi() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BranchResponse> call = apiInterface.get_branch();
        call.enqueue(new Callback<BranchResponse>() {
            @Override
            public void onResponse(Call<BranchResponse> call, Response<BranchResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            branchModelList.clear();
                            branchModelList.add(new BranchModel("Select Branch"));
                            branchModelList.addAll(response.body().getData());


                            ArrayAdapter<BranchModel> itemAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, branchModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            editBookingDialogBinding.spBranch.setAdapter(itemAdapter);

                            editBookingDialogBinding.spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    branch = branchModelList.get(position).getmBranchId();
                                    branch_name = branchModelList.get(position).getmBranchTitle();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } else {
                        }
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BranchResponse> call, Throwable t) {
                showError("something went wrong");


            }
        });
    }

    private void updateBookingBottomDialog() {

        editBookingDialogBinding = EditBookingDialogBinding.inflate(getLayoutInflater());
        getBranchAPi();

        dialog = new BottomSheetDialog(getContext());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(editBookingDialogBinding.getRoot());
        dialog.show();

        if (bking_type.equals("2")) {
            editBookingDialogBinding.llBranch.setVisibility(View.VISIBLE);
            editBookingDialogBinding.llPickup.setVisibility(View.GONE);
        } else {
            editBookingDialogBinding.llBranch.setVisibility(View.GONE);
            editBookingDialogBinding.llPickup.setVisibility(View.VISIBLE);
        }

        editBookingDialogBinding.etPickLocation.setText(pick_address);

        editBookingDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        editBookingDialogBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editBookingDialogBinding.etName.getText().toString();
                mobile = editBookingDialogBinding.etMobile.getText().toString();
                email = editBookingDialogBinding.etEmail.getText().toString();
                pick_address = editBookingDialogBinding.etPickLocation.getText().toString();
                dialog.dismiss();
                updateBookingApi(name, mobile, email, pick_address);

            }
        });

        editBookingDialogBinding.etPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                searchLocationDialog();
            }
        });
    }

    private void cancelBookingBottomDialog() {
        CancelBookingDialogBinding cancelBookingDialogBinding;
        cancelBookingDialogBinding = CancelBookingDialogBinding.inflate(getLayoutInflater());

        dialog2 = new BottomSheetDialog(getContext());
        dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog2.setContentView(cancelBookingDialogBinding.getRoot());
        dialog2.show();

        cancelBookingDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        cancelBookingDialogBinding.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                cancelBookingApi();

            }
        });


    }

    private void searchLocationDialog() {
        searchLocationDialogBinding = SearchLocationDialogBinding.inflate(getLayoutInflater());
        dialog1 = new Dialog(getContext(), R.style.my_dialog);
        dialog1.setContentView(searchLocationDialogBinding.getRoot());
        dialog1.setCancelable(true);
        dialog1.create();
        dialog1.show();

        searchLocationDialogBinding.etserach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPlaces(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBookingClick(BookingListModel bookingListModel, String type, String bookType) {
        bking_Id = bookingListModel.getmBkingId();
        bking_type = bookType;

//        if (type.equals("cancel")) {
//            cancelBookingBottomDialog();
//        } else {
//            updateBookingBottomDialog();
//
//        }

        if (type.equals("details")){
            Intent intent = new Intent(requireContext(), BookingDetailsActivity.class);
            intent.putExtra(Constant.BundleExtras.BOOKING_ID,bking_Id);
            startActivity(intent);
        }

        if (type.equals("cancel")){
            cancelBookingBottomDialog();

        }

    }

    private void cancelBookingApi() {

        showLoader();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.cancelBooking(bking_Id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            getBookingApi();
                            showAlert(response.body().getMessage());

                        } else {
                            hideLoader();

                        }
                    } else {
                        hideLoader();

                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });


    }

    private void updateBookingApi(String name, String mobile, String email, String address) {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiInterface.update_booking(bking_Id, name, mobile, email, address, branch);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                            getBookingApi();
                            showAlert(response.body().getMessage());

                        } else {
                            hideLoader();

                        }
                    } else {
                        hideLoader();

                    }
                } catch (Exception e) {
                    hideLoader();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void searchPlaces(String input) {

        searchLocationDialogBinding.lvNoData.setVisibility(View.VISIBLE);
        searchLocationDialogBinding.rvLocations.setVisibility(View.GONE);

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

                            searchLocationDialogBinding.lvNoData.setVisibility(View.GONE);
                            searchLocationDialogBinding.rvLocations.setVisibility(View.VISIBLE);

                            predictionModels.clear();
                            predictionModels.addAll(response.body().getPredictions());
                            initiateSearchPlaces();

                        } else {
                            // No predictions found
                            searchLocationDialogBinding.lvNoData.setVisibility(View.VISIBLE);
                            searchLocationDialogBinding.rvLocations.setVisibility(View.GONE);
                            Log.e("SearchPlaces", "No predictions found");
                        }
                    } else {
                        // Handle failure code
                        searchLocationDialogBinding.lvNoData.setVisibility(View.VISIBLE);
                        searchLocationDialogBinding.rvLocations.setVisibility(View.GONE);
                        Log.e("SearchPlaces", "Response Code: " + response.code());
                    }
                } catch (Exception e) {
//                    hideLoader();
                    searchLocationDialogBinding.lvNoData.setVisibility(View.VISIBLE);
                    searchLocationDialogBinding.rvLocations.setVisibility(View.GONE);
                    Log.e("SearchPlaces", "onResponse: Exception", e);
                }
            }

            @Override
            public void onFailure(Call<PlacesAutocompleteResponse> call, Throwable t) {
//                hideLoader();
                searchLocationDialogBinding.lvNoData.setVisibility(View.VISIBLE);
                searchLocationDialogBinding.rvLocations.setVisibility(View.GONE);
                Log.e("SearchPlaces", "onFailure: " + t.toString());
            }
        });
    }

    private void initiateSearchPlaces() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        searchLocationDialogBinding.rvLocations.setLayoutManager(linearLayoutManager);
        searchLocationAdapter = new SearchLocationAdapter(requireContext(), predictionModels, this);
        searchLocationDialogBinding.rvLocations.setAdapter(searchLocationAdapter);
        searchLocationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlaceClick(PredictionModel predictionModel) {
        pick_address = predictionModel.getDescription();
        dialog1.dismiss();
        updateBookingBottomDialog();


    }

}