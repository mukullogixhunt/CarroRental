package com.carro.carrorental.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.carro.carrorental.utils.Constant.QUICKEKYC_API_KEY;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.gson.Gson;
import com.carro.carrorental.BuildConfig;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiClientVerification;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.LoginResponse;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivityProfileVerificationBinding;
import com.carro.carrorental.databinding.UploadAdharDialogBinding;
import com.carro.carrorental.databinding.UploadDlDialogBinding;
import com.carro.carrorental.databinding.UploadOtherDocDialogBinding;
import com.carro.carrorental.databinding.UploadWorkIdDialogBinding;
import com.carro.carrorental.databinding.VerifyAdharDialogBinding;
import com.carro.carrorental.model.AadharOtpSendModel;
import com.carro.carrorental.model.AadharVerificationModel;
import com.carro.carrorental.model.DLVerificationModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;
import com.carro.carrorental.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileVerificationActivity extends BaseActivity {

    private static final int PERMISSION_CAMERA = 221;
    private static final int PERMISSION_WRITE_EXTERNAL = 222;
    private static final int PERMISSION_READ_MEDIA_IMAGES = 223;
    private final Calendar myCalendar = Calendar.getInstance();
    ActivityProfileVerificationBinding binding;
    Dialog dialog, dialogDl, dialogAdhar, dialogVerifyAdhar, dialogWork, dialogOther;
    UploadDlDialogBinding uploadDlDialogBinding;
    UploadAdharDialogBinding uploadAdharDialogBinding;
    VerifyAdharDialogBinding verifyAdharDialogBinding;
    UploadWorkIdDialogBinding uploadWorkIdDialogBinding;
    UploadOtherDocDialogBinding uploadOtherDocDialogBinding;
    int type;
    LoginModel loginModel = new LoginModel();
    String dlNo = "";
    String adharNo = "";
    String requestId = "";
    String dlIssue = "";
    String dlExpiry = "";
    String pageType = "";
    List<LoginModel> userDetailList = new ArrayList<>();
    private File adharFrontImg = null;
    private File adharBackImg = null;
    private File dlFrontImg = null;
    private File dlBackImg = null;
    private File workImg = null;
    private File workBackImg = null;
    private File otherImg = null;
    private File otherBackImg = null;
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            String croppedImagePath = result.getUriFilePath(ProfileVerificationActivity.this, true);


            if (type == 5) {
                workImg = new File(croppedImagePath);
                Glide.with(ProfileVerificationActivity.this)
                        .load(workImg)
                        .into(uploadWorkIdDialogBinding.ivWorkId);
                uploadWorkIdDialogBinding.llWork.setVisibility(GONE);
                uploadWorkIdDialogBinding.ivWorkId.setVisibility(VISIBLE);
            } else if (type == 6) {
                workBackImg = new File(croppedImagePath);
                Glide.with(ProfileVerificationActivity.this)
                        .load(workBackImg)
                        .into(uploadWorkIdDialogBinding.ivWorkBack);
                uploadWorkIdDialogBinding.llWorkBack.setVisibility(GONE);
                uploadWorkIdDialogBinding.ivWorkBack.setVisibility(VISIBLE);
            } else if (type == 7) {
                otherImg = new File(croppedImagePath);
                Glide.with(ProfileVerificationActivity.this)
                        .load(otherImg)
                        .into(uploadOtherDocDialogBinding.ivOtherFront);
                uploadOtherDocDialogBinding.llOtherFront.setVisibility(GONE);
                uploadOtherDocDialogBinding.ivOtherFront.setVisibility(VISIBLE);
            } else if (type == 8) {
                otherBackImg = new File(croppedImagePath);
                Glide.with(ProfileVerificationActivity.this)
                        .load(otherBackImg)
                        .into(uploadOtherDocDialogBinding.ivOtherBack);
                uploadOtherDocDialogBinding.llOtherBack.setVisibility(GONE);
                uploadOtherDocDialogBinding.ivOtherBack.setVisibility(VISIBLE);
            }


        }
    });
    private Uri imageUri;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            launchImageCropper();
        }
    });
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // File selected successfully
            imageUri = result.getData().getData();
            if (type == 5 || type == 6 || type == 7 || type == 8) {
                launchImageCropper2();
            } else {
                launchImageCropper();
            }

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileVerificationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getPreferences();
    }

    private void getPreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, ProfileVerificationActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        pageType = getIntent().getStringExtra(Constant.BundleExtras.PAGE_TYPE);
        userDetailApi();
        initialization();
    }

    private void initialization() {
        if (pageType.equals("self")) {
            binding.btnSkip.setVisibility(GONE);
            binding.btnNext.setVisibility(VISIBLE);
        } else if (pageType.equals("profile")) {
            binding.btnSkip.setVisibility(GONE);
            binding.btnNext.setVisibility(VISIBLE);
        } else {
            binding.btnSkip.setVisibility(VISIBLE);
            binding.btnNext.setVisibility(VISIBLE);
        }

        binding.btnUploadDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDlDialog();
            }
        });
        binding.btnUploadAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAdharDialog();
            }
        });
        binding.btnUploadWorkingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWorkDialog();
            }
        });

        binding.btnUploadOtherDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadOtherDocDialog();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageType.equals("self")) {
                    getOnBackPressedDispatcher().onBackPressed();
                } else if (pageType.equals("profile")) {
                    getOnBackPressedDispatcher().onBackPressed();
                } else {
                    Intent intent = new Intent(ProfileVerificationActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileVerificationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void userDetailApi() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.user_details(loginModel.getmCustId());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            userDetailList.clear();
                            userDetailList.addAll(response.body().getData());

                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_DATA, new Gson().toJson(userDetailList.get(0)), ProfileVerificationActivity.this);

                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_ID, userDetailList.get(0).getmCustId(), ProfileVerificationActivity.this);

                            if (userDetailList.get(0).getmCustStatus().equals("1")) {

                            } else {
                                showMessage();
                            }

                            if (userDetailList.get(0).getmCustDriveLic() == null || userDetailList.get(0).getmCustDriveLicb() == null) {
                                binding.btnUploadDl.setVisibility(VISIBLE);

                            } else {
                                binding.btnUploadDl.setText("Edit");
                                binding.btnUploadDl.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                            }


                        /*    if (userDetailList.get(0).getmCustAdhar() == null || userDetailList.get(0).getmCustAdharb() == null) {
                                binding.btnUploadAdhar.setVisibility(VISIBLE);

                            } else {
                                binding.btnUploadAdhar.setText("Edit");
                                binding.btnUploadAdhar.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                            }*/

                            if (userDetailList.get(0).getmCustWorkingProof() == null || userDetailList.get(0).getmCustWorkingProofb() == null) {
                                binding.btnUploadWorkingId.setVisibility(VISIBLE);
                            } else {
                                binding.btnUploadWorkingId.setText("Edit");
                                binding.btnUploadWorkingId.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                            }

                            if (userDetailList.get(0).getmCustOtherDoc() == null || userDetailList.get(0).getmCustOtherDocb() == null) {
                                binding.btnUploadOtherDoc.setVisibility(VISIBLE);
                            } else {
                                binding.btnUploadOtherDoc.setText("Edit");
                                binding.btnUploadOtherDoc.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                            }


                        } else {

                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Failure", t.toString());
//                showError("Something went wrong");
            }
        });


    }

    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileVerificationActivity.this);
        builder.setTitle("Your profile is in-active");
        builder.setMessage("Please contact to admin!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uploadDlDialog() {
        uploadDlDialogBinding = UploadDlDialogBinding.inflate(getLayoutInflater());
        dialogDl = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialogDl.setContentView(uploadDlDialogBinding.getRoot());
        dialogDl.setCancelable(true);
        dialogDl.create();
        dialogDl.show();


        String dlNumber = userDetailList.get(0).getmCustDriveLicno();


        if (dlNumber != null && !dlNumber.isEmpty()) {
            uploadDlDialogBinding.etDlNumber.setText(dlNumber);
        }


        uploadDlDialogBinding.etDOB.setOnClickListener(v -> {

            Utils.attachDatePicker(ProfileVerificationActivity.this, uploadDlDialogBinding.etDOB);

        });

        uploadDlDialogBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlNo = uploadDlDialogBinding.etDlNumber.getText().toString();

                callDlGetDataApi();
                dialogDl.dismiss();




            }
        });

    }

    private void uploadAdharDialog() {
        uploadAdharDialogBinding = UploadAdharDialogBinding.inflate(getLayoutInflater());
        dialogAdhar = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialogAdhar.setContentView(uploadAdharDialogBinding.getRoot());
        dialogAdhar.setCancelable(true);
        dialogAdhar.create();
        dialogAdhar.show();

        String number = userDetailList.get(0).getmCustAdharno();
        if (number != null && !number.isEmpty()) {
            uploadAdharDialogBinding.etAdharNumber.setText(number);
        }


        uploadAdharDialogBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callAadharSendOtp();

            }
        });

    }

    private void VerifyAadharDialog() {
        verifyAdharDialogBinding = VerifyAdharDialogBinding.inflate(getLayoutInflater());
        dialogVerifyAdhar = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialogVerifyAdhar.setContentView(verifyAdharDialogBinding.getRoot());
        dialogVerifyAdhar.setCancelable(true);
        dialogVerifyAdhar.create();
        dialogVerifyAdhar.show();


        verifyAdharDialogBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyAdharDialogBinding.otpview.getOTP().length() == 6)
                    callAadharSubmitOtp();
            }
        });

    }


    private void uploadWorkDialog() {
        uploadWorkIdDialogBinding = UploadWorkIdDialogBinding.inflate(getLayoutInflater());
        dialogWork = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialogWork.setContentView(uploadWorkIdDialogBinding.getRoot());
        dialogWork.setCancelable(true);
        dialogWork.create();
        dialogWork.show();
        String front = userDetailList.get(0).getmCustWorkingProof();
        String back = userDetailList.get(0).getmCustWorkingProofb();

        if (front != null && !front.isEmpty()) {
            uploadWorkIdDialogBinding.llWork.setVisibility(GONE);
            uploadWorkIdDialogBinding.ivWorkId.setVisibility(VISIBLE);
            Glide.with(ProfileVerificationActivity.this)
                    .load(ImagePathDecider.getDocumentImagePath() + userDetailList.get(0).getmCustWorkingProof())
                    .error(R.drawable.img_no_image)
                    .into(uploadWorkIdDialogBinding.ivWorkId);
        }
        if (back != null && !back.isEmpty()) {
            uploadWorkIdDialogBinding.llWorkBack.setVisibility(GONE);
            uploadWorkIdDialogBinding.ivWorkBack.setVisibility(VISIBLE);
            Glide.with(ProfileVerificationActivity.this)
                    .load(ImagePathDecider.getDocumentImagePath() + userDetailList.get(0).getmCustWorkingProofb())
                    .error(R.drawable.img_no_image)
                    .into(uploadWorkIdDialogBinding.ivWorkBack);
        }

        uploadWorkIdDialogBinding.llWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 5;
                imagePickerDialog();

            }
        });

        uploadWorkIdDialogBinding.llWorkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 6;
                imagePickerDialog();

            }
        });


        uploadWorkIdDialogBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (front != null && !front.isEmpty() && back != null && !back.isEmpty()) {
                binding.btnUploadWorkingId.setText("Edit");
                binding.btnUploadWorkingId.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                callUploadWorkApi();
                dialogWork.dismiss();
//                }else {
//                    Toast.makeText(ProfileVerificationActivity.this, "Please select documents to proceed", Toast.LENGTH_SHORT).show();
//                }


            }
        });

    }

    private void uploadOtherDocDialog() {
        uploadOtherDocDialogBinding = UploadOtherDocDialogBinding.inflate(getLayoutInflater());
        dialogOther = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialogOther.setContentView(uploadOtherDocDialogBinding.getRoot());
        dialogOther.setCancelable(true);
        dialogOther.create();
        dialogOther.show();
        String front = userDetailList.get(0).getmCustOtherDoc();
        String back = userDetailList.get(0).getmCustOtherDocb();

        if (front != null && !front.isEmpty()) {
            uploadOtherDocDialogBinding.llOtherFront.setVisibility(GONE);
            uploadOtherDocDialogBinding.ivOtherFront.setVisibility(VISIBLE);
            Glide.with(ProfileVerificationActivity.this)
                    .load(ImagePathDecider.getDocumentImagePath() + userDetailList.get(0).getmCustOtherDoc())
                    .error(R.drawable.img_no_image)
                    .into(uploadOtherDocDialogBinding.ivOtherFront);
        }
        if (back != null && !back.isEmpty()) {
            uploadOtherDocDialogBinding.llOtherBack.setVisibility(GONE);
            uploadOtherDocDialogBinding.ivOtherBack.setVisibility(VISIBLE);
            Glide.with(ProfileVerificationActivity.this)
                    .load(ImagePathDecider.getDocumentImagePath() + userDetailList.get(0).getmCustOtherDocb())
                    .error(R.drawable.img_no_image)
                    .into(uploadOtherDocDialogBinding.ivOtherBack);
        }

        uploadOtherDocDialogBinding.llOtherFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 7;
                imagePickerDialog();

            }
        });

        uploadOtherDocDialogBinding.llOtherBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 8;
                imagePickerDialog();

            }
        });


        uploadOtherDocDialogBinding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (front != null && !front.isEmpty() && back != null && !back.isEmpty()) {
                binding.btnUploadOtherDoc.setText("Edit");
                binding.btnUploadOtherDoc.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                callUploadOtherDocApi();
                dialogOther.dismiss();
//                }else {
//                    Toast.makeText(ProfileVerificationActivity.this, "Please select documents to proceed", Toast.LENGTH_SHORT).show();
//                }


            }
        });

    }



    private void callDlGetDataApi() {

        showLoader();

        JSONObject json = new JSONObject();

        try {
            json.put("key", QUICKEKYC_API_KEY);
            json.put("id_number", uploadDlDialogBinding.etDlNumber.getText().toString());
            json.put("dob", uploadDlDialogBinding.etDOB.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                okhttp3.MediaType.parse("application/json")
        );
        ApiInterface apiService = ApiClientVerification.getClient().create(ApiInterface.class);
        Call<DLVerificationModel> call = apiService.driving_license(body);
        call.enqueue(new Callback<DLVerificationModel>() {
            @Override
            public void onResponse(Call<DLVerificationModel> call, Response<DLVerificationModel> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getStatusCode() == 200) {
                            updateDrivingLic(response.body().getData());

                        } else {
                            hideLoader();
                            showError(response.body().getMessage());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<DLVerificationModel> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void callAadharSendOtp() {

        showLoader();

        JSONObject json = new JSONObject();

        try {
            json.put("key", QUICKEKYC_API_KEY);
            json.put("id_number", uploadAdharDialogBinding.etAdharNumber.getText().toString());


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                okhttp3.MediaType.parse("application/json")
        );
        ApiInterface apiService = ApiClientVerification.getClient().create(ApiInterface.class);
        Call<AadharOtpSendModel> call = apiService.aadhaar_generate_otp(body);
        call.enqueue(new Callback<AadharOtpSendModel>() {
            @Override
            public void onResponse(Call<AadharOtpSendModel> call, Response<AadharOtpSendModel> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getStatusCode() == 200) {
                            requestId = String.valueOf(response.body().getRequestId());
                            VerifyAadharDialog();
                            dialogAdhar.dismiss();


                        } else {
                            hideLoader();
                            showError(response.body().getMessage());

                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<AadharOtpSendModel> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void callAadharSubmitOtp() {

        showLoader();

        JSONObject json = new JSONObject();

        try {
            json.put("key", QUICKEKYC_API_KEY);
            json.put("otp", verifyAdharDialogBinding.otpview.getOTP());
            json.put("request_id", String.valueOf(requestId));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                okhttp3.MediaType.parse("application/json")
        );
        ApiInterface apiService = ApiClientVerification.getClient().create(ApiInterface.class);
        Call<AadharVerificationModel> call = apiService.aadhaar_submit_otp(body);
        call.enqueue(new Callback<AadharVerificationModel>() {
            @Override
            public void onResponse(Call<AadharVerificationModel> call, Response<AadharVerificationModel> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getStatusCode() == 200) {
                            requestId = "";

//                            binding.btnUploadAdhar.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                            dialogVerifyAdhar.dismiss();

                            updateAadhar(response.body().getData());

                        } else {
                            hideLoader();
                            showError(response.body().getMessage());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<AadharVerificationModel> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void updateAadhar(AadharVerificationModel.Data data) {
        showLoader();

        String address=data.getAddress().getLandmark()+","+data.getAddress().getHouse()+","+data.getAddress().getVtc()+","+data.getAddress().getPo()
                +","+data.getAddress().getSubdist()+","+data.getAddress().getDist()+","+data.getAddress().getState()+","+data.getAddress().getCountry();

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiService.update_adhar(loginModel.getmCustId(), data.getAadhaarNumber(), data.getFullName(), data.getDob(),
                address,data.getGender(), data.getZip());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        binding.btnUploadAdhar.setText("Edit");
                        binding.btnUploadAdhar.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                        hideLoader();
                    } else {
                        hideLoader();

                    }

                } catch (Exception e) {
                    hideLoader();

                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());
                hideLoader();
                showError("Something went wrong");
            }
        });
    }


    private void updateDrivingLic(DLVerificationModel.Data data) {
        showLoader();
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseResponse> call = apiService.update_driving_lic(loginModel.getmCustId(),data.getLicenseNumber(),data.getName(),data.getTemporaryAddress(),data.getTemporaryZip(),data.getPermanentAddress(),data.getPermanentZip(),data.getState(),data.getOlaCode(),
                data.getOlaName(),data.getGender(),data.getFatherOrHusbandName(),data.getDob(),data.getBloodGroup(),data.getVehicleClasses(),data.getCurrentStatus(),data.getDoi(),data.getDoe());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
                        binding.btnUploadDl.setText("Edit");
                        binding.btnUploadDl.setBackgroundTintList(ContextCompat.getColorStateList(ProfileVerificationActivity.this, R.color.green));
                        hideLoader();


                    } else {
                        hideLoader();

                    }

                } catch (Exception e) {
                    hideLoader();

                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Failure", t.toString());
                hideLoader();
                showError("Something went wrong");
            }
        });
    }

    private void callUploadDlApi() {
        String issue = "";
        String expire = "";

        if (dlIssue != null && !dlIssue.isEmpty()) {
            issue = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, dlIssue);
        }

        if (dlExpiry != null && !dlExpiry.isEmpty()) {
            expire = Utils.changeDateFormat(Constant.ddMMyyyy, Constant.yyyyMMdd, dlExpiry);
        }


        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), loginModel.getmCustId());
        RequestBody rbDlNO = RequestBody.create(MediaType.parse("text/plain"), dlNo);
        RequestBody rbDlIssue = RequestBody.create(MediaType.parse("text/plain"), issue);
        RequestBody rbDlExpiry = RequestBody.create(MediaType.parse("text/plain"), expire);

        MultipartBody.Part dlFrontImagePart = null;
        if (dlFrontImg != null) {
            dlFrontImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_DRIVER_LIC, dlFrontImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), dlFrontImg));
        }

        MultipartBody.Part dlBackImagePart = null;
        if (dlBackImg != null) {
            dlBackImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_DRIVER_LIC_B, dlBackImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), dlBackImg));
        }


        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.update_dl(rbUserId, rbDlNO, rbDlIssue, rbDlExpiry, dlFrontImagePart, dlBackImagePart);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            userDetailApi();
                            showAlert(response.body().getMessage());


                        } else {
                            hideLoader();
                            showError(response.message());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void callUploadAdharApi() {

        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), loginModel.getmCustId());
        RequestBody rbAdharNo = RequestBody.create(MediaType.parse("text/plain"), adharNo);


        MultipartBody.Part adharFrontImagePart = null;
        if (adharFrontImg != null) {
            adharFrontImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_AADHAR, adharFrontImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), adharFrontImg));
        }

        MultipartBody.Part adharBackImagePart = null;
        if (adharBackImg != null) {
            adharBackImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_AADHAR_B, adharBackImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), adharBackImg));
        }


        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.update_adhar(rbUserId, rbAdharNo, adharFrontImagePart, adharBackImagePart);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            userDetailApi();
                            showAlert(response.body().getMessage());


                        } else {
                            hideLoader();
                            showError(response.message());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void callUploadWorkApi() {


        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), loginModel.getmCustId());

        MultipartBody.Part workImagePart = null;
        if (workImg != null) {
            workImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_WORKING_PROOF, workImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), workImg));
        }

        MultipartBody.Part workImagePartB = null;
        if (workBackImg != null) {
            workImagePartB = MultipartBody.Part.createFormData(Constant.ApiKey.USER_WORKING_PROOFB, workBackImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), workBackImg));
        }

        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.update_work(rbUserId, workImagePart, workImagePartB);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            userDetailApi();
                            showAlert(response.body().getMessage());


                        } else {
                            hideLoader();
                            showError(response.message());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void callUploadOtherDocApi() {


        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), loginModel.getmCustId());

        MultipartBody.Part otherImagePart = null;
        if (otherImg != null) {
            otherImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_OTHER_DOC, otherImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), otherImg));
        }

        MultipartBody.Part otherImagePartB = null;
        if (otherBackImg != null) {
            otherImagePartB = MultipartBody.Part.createFormData(Constant.ApiKey.USER_OTHER_DOCB, otherBackImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), otherBackImg));
        }

        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.update_other_doc(rbUserId, otherImagePart, otherImagePartB);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            userDetailApi();
                            showAlert(response.body().getMessage());


                        } else {
                            hideLoader();
                            showError(response.message());
                        }
                    } else {
                        hideLoader();
                        showError(response.message());
                    }
                } catch (Exception e) {
                    hideLoader();
                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideLoader();
                Log.e("Failure", t.toString());
                showError("Something went wrong");
            }
        });
    }

    private void imagePickerDialog() {
        dialog = new Dialog(ProfileVerificationActivity.this, R.style.my_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_selection_dialog);
        dialog.show();

        ImageView ivCamera = dialog.findViewById(R.id.ivCamera);
        ImageView ivGallery = dialog.findViewById(R.id.ivGallery);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        ivCamera.setOnClickListener(view -> checkCameraPermission());
        ivGallery.setOnClickListener(view -> checkGalleryPermission());
        tvCancel.setOnClickListener(view -> dialog.dismiss());
    }

    private void checkCameraPermission() {

        if (dialog != null) {
            dialog.dismiss();
        }

        if (ContextCompat.checkSelfPermission(ProfileVerificationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileVerificationActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            openCamera();
        } else {
            openCamera();
        }
    }

    /// ////////////////////// check gallery permission///////////
    private void checkGalleryPermission() {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(ProfileVerificationActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileVerificationActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_READ_MEDIA_IMAGES);
                openGallery();
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(ProfileVerificationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileVerificationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                openGallery();
            } else {
                openGallery();
            }
        }
    }

    /// //////////////// for open camera /////////////////////
    private void openCamera() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat(Constant.yyyyMMdd_HHmmss, Locale.getDefault()).format(new java.util.Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, ".jpg", ProfileVerificationActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(ProfileVerificationActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(Constant.FILENAME, imageFileName);
            cameraActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Set the MIME type to allow any file type
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            galleryActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchImageCropper() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressQuality = 60;
       /* cropImageOptions.aspectRatioX = 2;
        cropImageOptions.aspectRatioY = 1;*/
        cropImageOptions.fixAspectRatio = false;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

    private void launchImageCropper2() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressQuality = 60;
       /* cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;*/
        cropImageOptions.fixAspectRatio = false;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }
}