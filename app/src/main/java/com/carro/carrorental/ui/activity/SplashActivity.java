package com.carro.carrorental.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;


import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.databinding.ActivitySplashBinding;
import com.carro.carrorental.model.CheckBlockModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
    private String TAG = SplashActivity.class.getSimpleName();


    // --- NEW: DECLARE THE PERMISSION LAUNCHER ---
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications enabled!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications permission denied. You can enable it in App Settings.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        askNotificationPermission();


        PreferenceUtils.setString(Constant.PreferenceConstant.BRANCH_NAME, "",SplashActivity.this);

        //get firebase token
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                log_e(TAG, "", task.getException());
                return;
            }
            String notificationToken = task.getResult();
            log_d(TAG, "token=====>" + notificationToken);
            PreferenceUtils.setString(
                    Constant.PreferenceConstant.FIREBASE_TOKEN,
                    notificationToken,
                    SplashActivity.this
            );
        });

        checkUpdate();
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d("Notifications", "Permission already granted.");
            }
            else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "Please enable notifications to receive important updates about your bookings.", Toast.LENGTH_LONG).show();
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
            else {
                Log.d("Notifications", "Requesting notification permission.");
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }



    private void checkUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashActivity.this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());

            } else {
                startSplash();
            }
        }).addOnFailureListener(e -> {
            Log.e("Splash", "App update check failed: " + e.getMessage());
            startSplash();
        });
    }


    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // handle callback
                    int resultCode = result.getResultCode();
                    if (resultCode != RESULT_OK) {
                        Log.d("Splash", "Update flow failed! Result code: " + resultCode);
                        // If the update is cancelled or fails,
                        // you can request to start the update again.
                        showAlertDialog();
                    } else {
//                        showAlert("App Updated Successfully");
                        startSplash();
                    }
                }
            });


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setMessage("Please install the latest version to continue using the app");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        AlertDialog dialog = builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkUpdate();
            }
        }).create();
        dialog.show();
    }

    private void showBlockAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setMessage("You are blocked by admin");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        AlertDialog dialog = builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).create();
        dialog.show();
    }


    private void checkBlock() {

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CheckBlockModel> call = apiService.check_block_with_id(loginModel.getmCustId());
        call.enqueue(new Callback<CheckBlockModel>() {
            @Override
            public void onResponse(Call<CheckBlockModel> call, Response<CheckBlockModel> response) {
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getUser().get(0).getmCustStatus().equalsIgnoreCase("3")) {
                            showBlockAlertDialog();

                        } else {
                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                } catch (Exception e) {


                }

            }

            @Override
            public void onFailure(Call<CheckBlockModel> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());

//                showError("Something went wrong");
            }
        });
    }

    private void startSplash() {

        String m_identification = getPackageName();
        String[] packageArray = m_identification.split("\\.");

       /* if (!Constant.CARRO_RENTAL.equals(packageArray[packageArray.length - 1])) {
           // cloneAlert();
        } else*/ {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start the main activity
                    if (PreferenceUtils.getBoolean(Constant.PreferenceConstant.IS_LOGIN, SplashActivity.this)) {
                        checkBlock();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, AfterSplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2500);
        }
    }
}