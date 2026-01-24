package com.carro.carrorental.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
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

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.gson.Gson;
import com.carro.carrorental.BuildConfig;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityUploadDocBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UploadDocActivity extends BaseActivity {

    ActivityUploadDocBinding binding;

    String user_id;
    LoginModel loginModel = new LoginModel();
    private Dialog dialog;
    private Uri imageUri;
    private File uploadAdhar = null;
    private File uploadDl = null;
    private File uploadId = null;
    private static final int PERMISSION_CAMERA = 221;
    private static final int PERMISSION_WRITE_EXTERNAL = 222;
    private static final int PERMISSION_READ_MEDIA_IMAGES = 223;
    private String selectedDocumentType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadDocBinding.inflate(getLayoutInflater());
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
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, UploadDocActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);
        user_id = loginModel.getmCustId();
        initialization();
    }
    private void initialization(){
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadDocActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callUploadDocApi();
            }
        });

        binding.tvSelectAdhar.setOnClickListener(v -> {
            selectedDocumentType = "Aadhar";
            imagePickerDialog();
        });
        binding.tvSelectDl.setOnClickListener(v -> {
            selectedDocumentType = "DL";
            imagePickerDialog();
        });
        binding.tvSelectId.setOnClickListener(v -> {
            selectedDocumentType = "ID";
            imagePickerDialog();
        });
    }


//    private void callUploadDocApi() {
//        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), user_id);
//
//        MultipartBody.Part adharImagePart = null;
//        if (uploadAdhar != null) {
//            adharImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_ADHAR, uploadAdhar.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), uploadAdhar));
//        }
//
//        MultipartBody.Part dlImagePart = null;
//        if (uploadDl != null) {
//            dlImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_DRIVE_LIC, uploadDl.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), uploadDl));
//        }
//        MultipartBody.Part idImagePart = null;
//        if (uploadId != null) {
//            idImagePart = MultipartBody.Part.createFormData(Constant.ApiKey.USER_WORKING_PROOF, uploadId.getPath(), RequestBody.create(MediaType.parse("multipart/form-data"), uploadId));
//        }
//
//        showLoader();
//
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<BaseResponse> call = apiService.update_document(rbUserId,adharImagePart,dlImagePart,idImagePart);
//        call.enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                hideLoader();
//                try {
//                    if (String.valueOf(response.code()).equalsIgnoreCase(Constant.SUCCESS_RESPONSE_CODE)) {
//                        if (response.body().getResult().equalsIgnoreCase(Constant.SUCCESS_RESPONSE)) {
//
//                            showAlert(response.body().getMessage());
//
//                                Intent intent = new Intent(UploadDocActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//
//
//
//                        } else {
//                            hideLoader();
//                            showError(response.message());
//                        }
//                    } else {
//                        hideLoader();
//                        showError(response.message());
//                    }
//                } catch (Exception e) {
//                    hideLoader();
//                    log_e(this.getClass().getSimpleName(), "onResponse: ", e);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                hideLoader();
//                Log.e("Failure", t.toString());
//                showError("Something went wrong");
//            }
//        });
//    }


    private void imagePickerDialog() {
        dialog = new Dialog(UploadDocActivity.this, R.style.my_dialog);
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

        if (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadDocActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            openCamera();
        } else {
            openCamera();
        }
    }

    ///////////////////////// check gallery permission///////////
    private void checkGalleryPermission() {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadDocActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_READ_MEDIA_IMAGES);
                openGallery();
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadDocActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                openGallery();
            } else {
                openGallery();
            }
        }
    }

    /////////////////// for open camera /////////////////////
    private void openCamera() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat(Constant.yyyyMMdd_HHmmss, Locale.getDefault()).format(new java.util.Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, ".jpg", UploadDocActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(UploadDocActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
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

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            launchImageCropper();
        }
    });

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // File selected successfully
            imageUri = result.getData().getData();
            launchImageCropper();
            // Now you can use the selectedFileUri as needed
        }
    });

    private void launchImageCropper() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressQuality = 60;
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;
        cropImageOptions.fixAspectRatio = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            String croppedImagePath = result.getUriFilePath(UploadDocActivity.this, true);

            if (croppedImagePath != null) {
                File croppedFile = new File(croppedImagePath);
                String fileName = getImageName(croppedImagePath);

                switch (selectedDocumentType) {
                    case "Aadhar":
                        uploadAdhar = croppedFile;
                        binding.tvAdhar.setText(fileName);
                        break;
                    case "DL":
                        uploadDl = croppedFile;
                        binding.tvDl.setText(fileName);
                        break;
                    case "ID":
                        uploadId = croppedFile;
                        binding.tvId.setText(fileName);
                        break;
                }
                selectedDocumentType = null;
            }

        }
    });

    /////////// for set selected image name //////////////

    private String getImageName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }


}