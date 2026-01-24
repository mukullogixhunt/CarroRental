package com.carro.carrorental.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.LoginResponse;
import com.carro.carrorental.databinding.ActivityEditProfileBinding;
import com.carro.carrorental.model.CityModel;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.model.StateModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.ImagePathDecider;
import com.carro.carrorental.utils.PreferenceUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {

    private static final int PERMISSION_CAMERA = 221;
    private static final int PERMISSION_WRITE_EXTERNAL = 222;
    private static final int PERMISSION_READ_MEDIA_IMAGES = 223;
    ActivityEditProfileBinding binding;
    String gender;
    String user_id;
    LoginModel loginModel = new LoginModel();
    List<StateModel> stateModelList = new ArrayList<>();
    List<CityModel> cityModelList = new ArrayList<>();
    String state = "";
    String state_name = "";
    String city = "";
    String pageType = "";
    private List<String> genderList = new ArrayList<>();
    private Dialog dialog;
    private Uri imageUri;
    private File uploadImg = null;
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            String croppedImagePath = result.getUriFilePath(EditProfileActivity.this, true);
            uploadImg = new File(croppedImagePath);

            Glide.with(EditProfileActivity.this)
                    .load(uploadImg)
                    .into(binding.ivProfile);
        } else {
            showError("Image cropping failed");
        }
    });
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            launchImageCropper();
        } else {
            showError("Camera capture cancelled or failed");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getProfilePreferences();


    }

    private void getProfilePreferences() {
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, EditProfileActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);

        pageType = getIntent().getStringExtra(Constant.BundleExtras.PAGE_TYPE);

        initiateEditProfile();
    }

/*    private void getStateAPi() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StateResponse> call = apiInterface.get_country_state("IN");
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            stateModelList.clear();
                            stateModelList.add(new StateModel("Select State"));
                            stateModelList.addAll(response.body().getData());


                            ArrayAdapter<StateModel> itemAdapter = new ArrayAdapter<>(EditProfileActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, stateModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            binding.spState.setAdapter(itemAdapter);

                            if (loginModel.getmCustState() != null && !loginModel.getmCustState().isEmpty()) {
                                int stateSelected = 0;
                                for (int i = 0; i < stateModelList.size(); i++) {
                                    if (stateModelList.get(i).getName().equals(loginModel.getmCustState())) {
                                        stateSelected = i;
                                    }
                                }
                                binding.spState.setSelection(stateSelected);
                            }


                            binding.spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    state = stateModelList.get(position).getIso2();
                                    state_name = stateModelList.get(position).getName();
                                    getCityAPi(state);

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
            public void onFailure(Call<StateResponse> call, Throwable t) {


            }
        });
    }*/

/*    private void getCityAPi(String iso) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CityResponse> call = apiInterface.get_state_cities("IN", iso);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                //  hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            cityModelList.clear();
                            cityModelList.add(new CityModel("Select City"));
                            cityModelList.addAll(response.body().getData());


                            ArrayAdapter<CityModel> itemAdapter = new ArrayAdapter<>(EditProfileActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cityModelList);
                            itemAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                            binding.spCity.setAdapter(itemAdapter);

                            if (loginModel.getmCustCity() != null && !loginModel.getmCustCity().isEmpty()) {
                                int citySelected = 0;
                                for (int i = 0; i < cityModelList.size(); i++) {
                                    if (cityModelList.get(i).getName().equals(loginModel.getmCustCity())) {
                                        citySelected = i;
                                    }
                                }
                                binding.spCity.setSelection(citySelected);
                            }

                            binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    city = cityModelList.get(position).getName();


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
            public void onFailure(Call<CityResponse> call, Throwable t) {


            }
        });
    }*/

    private void initiateEditProfile() {

/*        getStateAPi();
        getCityAPi("CT");*/

        genderList.clear();
        genderList.add("Choose gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        ArrayAdapter arrayAdapter = new ArrayAdapter(EditProfileActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderList);
        binding.spGender.setAdapter(arrayAdapter);

        if (loginModel.getmCustGender() != null && !loginModel.getmCustGender().isEmpty()) {
            switch (loginModel.getmCustGender()) {
                case "1":
                    binding.spGender.setSelection(1);
                    break;
                case "2":
                    binding.spGender.setSelection(2);
                    break;
                case "3":
                    binding.spGender.setSelection(3);
                    break;
            }
        }

        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                gender = binding.spGender.getSelectedItem().toString();
                gender = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (loginModel.getmCustName() != null && !loginModel.getmCustName().isEmpty()) {
            binding.etName.setText(loginModel.getmCustName());
        }
        if (loginModel.getmCustEmail() != null && !loginModel.getmCustEmail().isEmpty()) {
            binding.etEmail.setText(loginModel.getmCustEmail());
        }
        if (loginModel.getmCustAltMobile() != null && !loginModel.getmCustAltMobile().isEmpty()) {
            binding.etAltMobile.setText(loginModel.getmCustAltMobile());
        }
        if (loginModel.getmCustAddress() != null && !loginModel.getmCustAddress().isEmpty()) {
            binding.etAddress.setText(loginModel.getmCustAddress());
        }
        if (loginModel.getmCustImg() != null && !loginModel.getmCustImg().isEmpty()) {
            Glide.with(EditProfileActivity.this)
                    .load(ImagePathDecider.getUserImagePath() + loginModel.getmCustImg())
                    .error(R.drawable.img_no_profile)
                    .into(binding.ivProfile);
        }


        binding.btnRegister.setOnClickListener(v -> {
            if (validate()) {
                callUpdateProfileApi();
            }

        });

        binding.ivCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

    }

    private void callUpdateProfileApi() {

        String userName = binding.etName.getText().toString();
        String userAltMobile = binding.etAltMobile.getText().toString();
        String userEmail = binding.etEmail.getText().toString();
        String userAddress = binding.etAddress.getText().toString();
        user_id = loginModel.getmCustId();


        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody rbUserName = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody rbUserMobile = RequestBody.create(MediaType.parse("text/plain"), loginModel.getmCustMobile());
        RequestBody rbUserEmail = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody rbAltMobile = RequestBody.create(MediaType.parse("text/plain"), userAltMobile);
        RequestBody rbUserAddress = RequestBody.create(MediaType.parse("text/plain"), userAddress);
        RequestBody rbGender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody rbState = RequestBody.create(MediaType.parse("text/plain"), state_name);
        RequestBody rbCity = RequestBody.create(MediaType.parse("text/plain"), city);

        MultipartBody.Part profileImagePart = null;
        if (uploadImg != null) {
            profileImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_PIC, uploadImg.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), uploadImg));
        }


        showLoader();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.update_profile(rbUserId, rbUserName, rbUserMobile, rbGender, rbUserEmail, rbAltMobile, rbUserAddress, rbState, rbCity, profileImagePart);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoader();
                try {
                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {

                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_DATA, new Gson().toJson(response.body().getData().get(0)), EditProfileActivity.this);

                            PreferenceUtils.setString(Constant.PreferenceConstant.USER_ID, response.body().getData().get(0).getmCustId(), EditProfileActivity.this);

                            if (pageType.equals("home")) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileVerificationActivity.class);
                                intent.putExtra(Constant.BundleExtras.PAGE_TYPE, "edit");
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }


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

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        } else {
            openFrontCamera();
        }
    }

    private void openFrontCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Explicitly set front camera using multiple extras for broader compatibility
        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        // Ensure no camera switch option is available
        intent.putExtra("android.intent.extras.CAMERA_FACING_EXCLUSIVE", true);

        String timeStamp = new SimpleDateFormat(Constant.yyyyMMdd_HHmmss, Locale.getDefault()).format(new java.util.Date());
        String imageFileName = "SELFIE_" + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("SELFIE_" + timeStamp, ".jpg", EditProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(Constant.FILENAME, imageFileName);
            cameraActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open camera");
        }
    }

    private void launchImageCropper() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressQuality = 60;
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;
        cropImageOptions.fixAspectRatio = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }


//    private void imagePickerDialog() {
//        dialog = new Dialog(EditProfileActivity.this, R.style.my_dialog);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.image_selection_dialog);
//        dialog.show();
//
//        ImageView ivCamera = dialog.findViewById(R.id.ivCamera);
//        ImageView ivGallery = dialog.findViewById(R.id.ivGallery);
//        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
//        ivCamera.setOnClickListener(view -> checkCameraPermission());
//        ivGallery.setOnClickListener(view -> checkGalleryPermission());
//        tvCancel.setOnClickListener(view -> dialog.dismiss());
//    }
//
//    private void checkCameraPermission() {
//
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//
//        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
//            openCamera();
//        } else {
//            openCamera();
//        }
//    }
//
//    ///////////////////////// check gallery permission///////////
//    private void checkGalleryPermission() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_READ_MEDIA_IMAGES);
//                openGallery();
//            } else {
//                openGallery();
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
//                openGallery();
//            } else {
//                openGallery();
//            }
//        }
//    }
//
//    /////////////////// for open camera /////////////////////
//    private void openCamera() {
//
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        String timeStamp = new SimpleDateFormat(Constant.yyyyMMdd_HHmmss, Locale.getDefault()).format(new java.util.Date());
//        String imageFileName = "IMG_" + timeStamp + ".jpg";
//
//        try {
//            File file = File.createTempFile("IMG_" + timeStamp, ".jpg", EditProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//            imageUri = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            intent.putExtra(Constant.FILENAME, imageFileName);
//            cameraActivityResultLauncher.launch(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*"); // Set the MIME type to allow any file type
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        try {
//            galleryActivityResultLauncher.launch(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//        if (result.getResultCode() == Activity.RESULT_OK) {
//            launchImageCropper();
//        }
//    });
//
//    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//        if (result.getResultCode() == Activity.RESULT_OK) {
//            // File selected successfully
//            imageUri = result.getData().getData();
//            launchImageCropper();
//            // Now you can use the selectedFileUri as needed
//        }
//    });
//
//    private void launchImageCropper() {
//        CropImageOptions cropImageOptions = new CropImageOptions();
//        cropImageOptions.imageSourceIncludeGallery = false;
//        cropImageOptions.imageSourceIncludeCamera = true;
//        cropImageOptions.outputCompressQuality = 60;
//        cropImageOptions.aspectRatioX = 1;
//        cropImageOptions.aspectRatioY = 1;
//        cropImageOptions.fixAspectRatio = true;
//        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
//        cropImage.launch(cropImageContractOptions);
//    }
//
//    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
//        if (result.isSuccessful()) {
//            String croppedImagePath = result.getUriFilePath(EditProfileActivity.this, true);
//
//            uploadImg = new File(croppedImagePath);
//
//            Glide.with(EditProfileActivity.this)
//                    .load(uploadImg)
//                    .into(binding.ivProfile);
//
//        }
//    });

    private boolean validate() {
        boolean valid = true;
        if (binding.etName.getText().toString().isEmpty()) {
            binding.etName.setError("Please enter your name..!");
            valid = false;
        } else {
            binding.etName.setError(null);
        }
        if (binding.etAltMobile.getText().toString().isEmpty()) {
            binding.etAltMobile.setError("Please enter your Alternate mobile Number...!");
            valid = false;
        } else {
            binding.etAltMobile.setError(null);
        }
        if (binding.etEmail.getText().toString().isEmpty()) {
            binding.etEmail.setError("Please enter your email..!");
            valid = false;
        } else {
            binding.etEmail.setError(null);
        }
        if (binding.etAddress.getText().toString().isEmpty()) {
            binding.etAddress.setError("Please enter your address..!");
            valid = false;
        } else {
            binding.etAddress.setError(null);
        }

        if (binding.spGender.getSelectedItemPosition() == 0) {  // The first item is considered invalid
            TextView errorText = (TextView) binding.spGender.getSelectedView();
            errorText.setError("");  // Set error on selected item
            errorText.setTextColor(ContextCompat.getColor(this, R.color.red)); // Change the color to red
            errorText.setText("Please select a gender"); // Show error message
            valid = false;
        }
        return valid;
    }
}