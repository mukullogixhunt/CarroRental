package com.carro.carrorental.ui.activity;

import static android.view.View.GONE;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.carro.carrorental.BuildConfig;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.BranchResponse;
import com.carro.carrorental.api.response.HomePageResponse;
import com.carro.carrorental.api.response.OfferResponse;
import com.carro.carrorental.api.response.PackageResponse;
import com.carro.carrorental.api.response.SliderResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.callTest.CallActivity;
import com.carro.carrorental.databinding.ActivityHomeBinding;
import com.carro.carrorental.databinding.BranchDialogBinding;
import com.carro.carrorental.databinding.ExitBottomDialogBinding;
import com.carro.carrorental.databinding.LogoutBottomDialogBinding;
import com.carro.carrorental.listener.BranchClickListener;
import com.carro.carrorental.model.BranchModel;
import com.carro.carrorental.model.HomePageModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.OfferModel;
import com.carro.carrorental.model.PackageModel;
import com.carro.carrorental.model.SliderModel;
import com.carro.carrorental.ui.adapter.BranchAdapter;
import com.carro.carrorental.ui.adapter.OfferAdapter;
import com.carro.carrorental.ui.adapter.PackageAdapter;
import com.carro.carrorental.ui.adapter.SliderAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements BranchClickListener {

    // --- NEW: DECLARE THE PERMISSION LAUNCHER ---
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications enabled!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications permission denied. You can enable it in App Settings.", Toast.LENGTH_LONG).show();
                }
            });
    ActivityHomeBinding binding;
    LoginModel loginModel = new LoginModel();
    Dialog dialog, dialogBranch;
    List<BranchModel> branchModelList = new ArrayList<>();
    BranchAdapter branchAdapter;
    String branchId = "";
    String branchName = "";
    String bName = "";
    BranchDialogBinding branchDialogBinding;
    private SliderAdapter sliderAdapter;
    private OfferAdapter offerAdapter;
    private PackageAdapter packageAdapter;
    private Handler sliderHandler = new Handler();
    private int pos = 0;
    private String shareMessage;
    private List<SliderModel> sliderList = new ArrayList<>();
    private List<OfferModel> offerModelList = new ArrayList<>();
    private List<PackageModel> packageModelList = new ArrayList<>();
    private List<HomePageModel> homePageList = new ArrayList<>();
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {

            if (binding.vpBanner.getCurrentItem() == (sliderList.size() - 1)) {
                pos = 0;
            } else {
                pos = binding.vpBanner.getCurrentItem() + 1;
            }

            binding.vpBanner.setCurrentItem(pos);
        }
    };

    public static void showAdvertiseDialog(
            Context context,
            String item
    ) {
        Utils.isAdvShow = false;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_advertise);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        ImageView imgAdvertise = dialog.findViewById(R.id.imgAdvertise);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        // Load image from m_adv_image
        Glide.with(context)
                .load(ImagePathDecider.getAdvertisementPath() + item)
                .placeholder(android.R.color.darker_gray)
                .dontTransform()
                .override(Target.SIZE_ORIGINAL)
                .into(imgAdvertise);
        // Close dialog
        imgClose.setOnClickListener(v -> dialog.dismiss());

        // Optional: click on image (open web link)
        imgAdvertise.setOnClickListener(v -> {

        });

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        askNotificationPermission();


        getUserPreferences();

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, CallActivity.class));
                startActivity(new Intent(HomeActivity.this, CallActivity.class));
            }
        });
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d("Notifications", "Permission already granted.");
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Please enable notifications to receive important updates about your bookings.", Toast.LENGTH_LONG).show();
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                Log.d("Notifications", "Requesting notification permission.");
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void getUserPreferences() {

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, HomeActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        bName = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_NAME, HomeActivity.this);


        if (bName != null && !bName.isEmpty()) {
            binding.tvBranch.setText(bName);
            binding.tvSelectCity.setText("");
        } else {
            binding.tvBranch.setText("");
        }


        checkUserData();
        initialization();
    }

    private void checkUserData() {

        if (loginModel.getmCustName() != null && !loginModel.getmCustName().isEmpty() &&
                loginModel.getmCustMobile() != null && !loginModel.getmCustMobile().isEmpty() &&
                loginModel.getmCustGender() != null && !loginModel.getmCustGender().isEmpty()) {

/*            Glide.with(HomeActivity.this)
                    .load(ImagePathDecider.getUserImagePath() + loginModel.getmCustImg())
                    .error(R.drawable.img_no_profile)
                    .into(binding.ivUser);*/
        } else {
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            intent.putExtra(Constant.BundleExtras.PAGE_TYPE, "home");
            startActivity(intent);
            finish();
        }


    }

    private void initialization() {

        updateFCM();

        initiateSlider();
        getHomeImage();



/*        binding.ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
            }
        });*/


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    exitBottomDialog();
                }
            }
        });


        // Setup Drawer Toggle on the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.ivDrawer.setOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        binding.navView.getMenu().findItem(R.id.nav_home).setChecked(true);

        binding.navView.getMenu().findItem(R.id.nav_home).setOnMenuItemClickListener(menuItem -> {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END);
            }
            return true;
        });
        binding.navView.getMenu().findItem(R.id.nav_my_bookings).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(this, MyBookingsActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_notifications).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(this, ProfileDetailActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logoutBottomDialog();
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_rate).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(this, RateUsActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_t_c).setOnMenuItemClickListener(menuItem -> {
            Constant.WEBVIEW_TITLE = getString(R.string.terms_condition);
            Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_policy).setOnMenuItemClickListener(menuItem -> {
            Constant.WEBVIEW_TITLE = getString(R.string.privacy_policy);
            Constant.WEBVIEW_URL = getString(R.string.privacy_policy_url);
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_contact_us).setOnMenuItemClickListener(menuItem -> {
            Constant.WEBVIEW_TITLE = getString(R.string.contact_us);
            Constant.WEBVIEW_URL = getString(R.string.contact_us_url);
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            startActivity(intent);
            return false;
        });
        binding.navView.getMenu().findItem(R.id.nav_about_us).setOnMenuItemClickListener(menuItem -> {
            Constant.WEBVIEW_TITLE = getString(R.string.about_us);
            Constant.WEBVIEW_URL = getString(R.string.about_us_url);
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            startActivity(intent);
            return false;
        });


        binding.ivSelfDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bName != null && !bName.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, SelfDriveActivity.class);
                    startActivity(intent);
                } else {
                    setMessageForSnackbar("Please select branch to proceed", false);
                }

            }
        });

        binding.ivCabBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bName != null && !bName.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    setMessageForSnackbar("Please select branch to proceed", false);
                }


            }
        });
        binding.ivBusBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bName != null && !bName.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, BusBookActivity.class);
                    startActivity(intent);
                } else {
                    setMessageForSnackbar("Please select branch to proceed", false);
                }


            }
        });
        binding.ivLuxuryCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bName != null && !bName.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, LuxuryCarActivity.class);
                    startActivity(intent);
                } else {
                    setMessageForSnackbar("Please select branch to proceed", false);
                }


            }
        });
        binding.ivOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OffersActivity.class);
                startActivity(intent);

            }
        });
        binding.ivPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PackagesActivity.class);
                startActivity(intent);

            }
        });

        binding.ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutBottomDialog();
            }
        });

/*        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });*/


/*        binding.ivRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });*/

        binding.llBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchDialog();
            }
        });


    }

    private void branchDialog() {

        branchDialogBinding = BranchDialogBinding.inflate(getLayoutInflater());
        dialogBranch = new Dialog(HomeActivity.this, R.style.my_dialog);
        dialogBranch.setContentView(branchDialogBinding.getRoot());
        dialogBranch.setCancelable(true);
        dialogBranch.create();
        getBranchAPi();
        dialogBranch.show();
    }

    private void getBranchAPi() {
        showLoader();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BranchResponse> call = apiInterface.get_branch();
        call.enqueue(new Callback<BranchResponse>() {
            @Override
            public void onResponse(Call<BranchResponse> call, Response<BranchResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            branchModelList.clear();
                            branchModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            branchDialogBinding.rvBranch.setLayoutManager(linearLayoutManager);
                            branchAdapter = new BranchAdapter(HomeActivity.this, branchModelList, HomeActivity.this);
                            branchDialogBinding.rvBranch.setAdapter(branchAdapter);

                            branchAdapter.notifyDataSetChanged();


                        } else {
                            hideLoader();
                        }
                    } else {
                        hideLoader();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoader();
                }
            }

            @Override
            public void onFailure(Call<BranchResponse> call, Throwable t) {
                showError("something went wrong");
                hideLoader();

            }
        });
    }

    @Override
    public void onBranchClick(BranchModel branchModel) {
        branchId = branchModel.getmBranchId();
        branchName = branchModel.getmBranchTitle();
        PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_ID, branchId, HomeActivity.this);
        PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_NAME, branchName, HomeActivity.this);
        PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_LATT, String.valueOf(branchModel.getmBranchLatt()), HomeActivity.this);
        PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_RADIUS,  String.valueOf(branchModel.getmBranchRadius()), HomeActivity.this);
        bName = PreferenceUtils.getString(Constant.PreferenceConstant.BRANCH_NAME, HomeActivity.this);

        binding.tvBranch.setText(bName);
        binding.tvSelectCity.setText("");
        dialogBranch.dismiss();
    }

    private void initiateSlider() {
        getSlider();

        getOffers();
        getPackage();

        sliderAdapter = new SliderAdapter(HomeActivity.this, sliderList, binding.vpBanner);
        binding.vpBanner.setAdapter(sliderAdapter);

        binding.vpBanner.setClipToPadding(false);
        binding.vpBanner.setClipChildren(false);
        binding.vpBanner.setOffscreenPageLimit(3);
        binding.vpBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(15));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
        binding.vpBanner.setPageTransformer(compositePageTransformer);

        binding.vpBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
            }
        });


    }


   /* private void getAdvertise() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AdvertiseModel> call = apiService.get_advertise();
        call.enqueue(new Callback<AdvertiseModel>() {
            @Override
            public void onResponse(Call<AdvertiseModel> call, Response<AdvertiseModel> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getData().get(0).getMAdvCust().equalsIgnoreCase("1")&& Utils.isAdvShow)
                            showAdvertiseDialog(HomeActivity.this, response.body().getData().get(0));
                    }

                } catch (Exception e) {


                }

            }

            @Override
            public void onFailure(Call<AdvertiseModel> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());

//                showError("Something went wrong");
            }
        });
    }*/

    private void getSlider() {

        binding.materialCardView.setVisibility(GONE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SliderResponse> call = apiService.getSlider();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            binding.materialCardView.setVisibility(View.VISIBLE);

                            sliderList.clear();
                            sliderList.addAll(response.body().getData());
                            binding.vpBanner.setVisibility(View.VISIBLE);
                            //  new pager
                            sliderAdapter.notifyDataSetChanged();

                        } else {

                            binding.materialCardView.setVisibility(GONE);
                        }
                    } else {
                        binding.materialCardView.setVisibility(GONE);
                    }
                } catch (Exception e) {
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.materialCardView.setVisibility(GONE);
                }

            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());
                binding.materialCardView.setVisibility(GONE);
//                showError("Something went wrong");
            }
        });
    }

    private void getHomeImage() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<HomePageResponse> call = apiService.home_page();
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            homePageList.clear();
                            homePageList.addAll(response.body().getData());

                            binding.textView.setText(homePageList.get(0).getmHcTagline());

                            Glide.with(HomeActivity.this)
                                    .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcIcon())
                                    .error(R.drawable.img_car)
                                    .into(binding.imageView);

                            Glide.with(HomeActivity.this)
                                    .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcCabImg())
                                    .error(R.drawable.image_cab_service)
                                    .into(binding.ivCabBook);

                            Glide.with(HomeActivity.this)
                                    .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcSelfDriveImg())
                                    .error(R.drawable.image_self_drive)
                                    .into(binding.ivSelfDrive);

                            Glide.with(HomeActivity.this)
                                    .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcLuxuryCarImg())
                                    .error(R.drawable.image_luxury)
                                    .into(binding.ivLuxuryCar);

                            Glide.with(HomeActivity.this)
                                    .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcBusServiceImg())
                                    .error(R.drawable.image_bus_book)
                                    .into(binding.ivBusBook);


                            if (response.body().getData().get(0).getmHcAdvImgSh().equalsIgnoreCase("1") && Utils.isAdvShow)
                                showAdvertiseDialog(HomeActivity.this, homePageList.get(0).getmHcAdvImg());

                            if (response.body().getData().get(0).getmHcReferralImgSh().equalsIgnoreCase("1"))
                                Glide.with(HomeActivity.this)
                                        .load(ImagePathDecider.getHomePageImagePath() + homePageList.get(0).getmHcReferralImg())
                                        .error(R.drawable.img_refer)
                                        .into(binding.ivRefer);
                            else
                                binding.ivRefer.setVisibility(GONE);

                        } else {


                        }
                    } else {

                    }
                } catch (Exception e) {
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);

                }

            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());

//                showError("Something went wrong");
            }
        });
    }


    private void getOffers() {

        binding.recOffers.setVisibility(GONE);
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OfferResponse> call = apiService.getOffers();
        call.enqueue(new Callback<OfferResponse>() {
            @Override
            public void onResponse(Call<OfferResponse> call, Response<OfferResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            binding.recOffers.setVisibility(View.VISIBLE);

                            offerModelList.clear();
                            offerModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.recOffers.setLayoutManager(linearLayoutManager);
                            offerAdapter = new OfferAdapter(HomeActivity.this, offerModelList);
                            binding.recOffers.setAdapter(offerAdapter);

                        } else {
                            hideLoader();
                            binding.recOffers.setVisibility(GONE);
                        }
                    } else {
                        hideLoader();
                        binding.recOffers.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.recOffers.setVisibility(GONE);
                }

            }

            @Override
            public void onFailure(Call<OfferResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
                binding.recOffers.setVisibility(GONE);
//                showError("Something went wrong");
            }
        });
    }

    private void getPackage() {

        binding.recPackages.setVisibility(GONE);
        showLoader();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PackageResponse> call = apiService.get_package();
        call.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            binding.recPackages.setVisibility(View.VISIBLE);

                            packageModelList.clear();
                            packageModelList.addAll(response.body().getData());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.recPackages.setLayoutManager(linearLayoutManager);
                            packageAdapter = new PackageAdapter(HomeActivity.this, packageModelList);
                            binding.recPackages.setAdapter(packageAdapter);

                        } else {
                            hideLoader();
                            binding.recPackages.setVisibility(GONE);
                        }
                    } else {
                        hideLoader();
                        binding.recPackages.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                    binding.recPackages.setVisibility(GONE);
                }

            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                hideLoader();
                // Log error here since request failed
                Log.e("Failure", t.toString());
                binding.recPackages.setVisibility(GONE);
//                showError("Something went wrong");
            }
        });
    }

    private void logoutBottomDialog() {
        LogoutBottomDialogBinding logoutBottomDialogBinding;
        logoutBottomDialogBinding = LogoutBottomDialogBinding.inflate(getLayoutInflater());

        dialog = new BottomSheetDialog(HomeActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(logoutBottomDialogBinding.getRoot());
        dialog.show();

        logoutBottomDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        logoutBottomDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        logoutBottomDialogBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGIN, false, HomeActivity.this);
                PreferenceUtils.clearAll(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void exitBottomDialog() {
        ExitBottomDialogBinding exitBottomDialogBinding;
        exitBottomDialogBinding = ExitBottomDialogBinding.inflate(getLayoutInflater());

        dialog = new BottomSheetDialog(HomeActivity.this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(exitBottomDialogBinding.getRoot());
        dialog.show();

        exitBottomDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exitBottomDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        exitBottomDialogBinding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();

            }
        });
    }

    public void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App"); // Subject (optional)

        shareMessage = "Ready to experience hassle-free rides? Download CarroRental today and enjoy your journey with us.";
        shareMessage = shareMessage + "\n\n" + "Download Now-" + "\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" + "\uD83D\uDE96 Your Journey, Our Priority! \uD83D\uDE96";


        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage); // Content to share

        // Verify if there are apps to handle this intent
        if (shareIntent.resolveActivity(HomeActivity.this.getPackageManager()) != null) {
            HomeActivity.this.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            // Handle scenario when no apps can handle the intent
            Toast.makeText(HomeActivity.this, "No app found to share.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFCM() {

        String fcmToken = PreferenceUtils.getString(Constant.PreferenceConstant.FIREBASE_TOKEN, HomeActivity.this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiService.updateFCM(loginModel.getmCustId(), fcmToken);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        } else {
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
            }
        });
    }

    private void setMessageForSnackbar(String msg, boolean flag) {
        if (flag) {

            showError(msg);

          /*  Snacky.builder()
                    .setActivity(HomeActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .success()
                    .show();*/
        } else {

            showError(msg);
           /* Snacky.builder()
                    .setActivity(HomeActivity.this)
                    .setActionText("Ok")
                    .setActionClickListener(v -> {
                        //do something
                    })
                    .setText(msg)
                    .setDuration(Snacky.LENGTH_INDEFINITE)
                    .error()
                    .show();*/
        }
    }
}