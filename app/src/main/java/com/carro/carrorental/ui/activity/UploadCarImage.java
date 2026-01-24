package com.carro.carrorental.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import android.widget.ImageView;

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
import com.carro.carrorental.BuildConfig;
import com.carro.carrorental.R;
import com.carro.carrorental.api.ApiClient;
import com.carro.carrorental.api.ApiInterface;
import com.carro.carrorental.api.response.commonResponse.BaseResponse;
import com.carro.carrorental.databinding.ActivityUploadCarImageBinding;
import com.carro.carrorental.ui.adapter.ScratchImageAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadCarImage extends BaseActivity {

    private ActivityUploadCarImageBinding binding;
    private Dialog dialog;
    private Uri imageUri;

    private static final int PERMISSION_CAMERA = 201;
    private static final int PERMISSION_GALLERY = 202;

    /* ===== TYPES ===== */
    private static final int FRONT = 1;
    private static final int BACK = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
    private static final int INT_FRONT = 5;
    private static final int INT_BACK = 6;
    private static final int METER = 7;
    private static final int TOOLKIT = 8;
    private static final int SPARE = 9;
    private static final int SCRATCH = 10;

    private int type = 0;

    /* ===== FILES ===== */
    private File frontImg, backImg, leftImg, rightImg;
    private File intFrontImg, intBackImg;
    private File meterImg, toolkitImg, spareImg;

    private final ArrayList<File> scratchImages = new ArrayList<>();
    private ScratchImageAdapter scratchAdapter;

    /* ===== CROP RESULT ===== */
    ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), result -> {
                if (!result.isSuccessful()) return;

                String path = result.getUriFilePath(this, true);
                if (path == null) return;

                File file = new File(path);

                switch (type) {
                    case FRONT:
                        frontImg = file;
                        show(binding.llFront, binding.ivFront, file);
                        break;
                    case BACK:
                        backImg = file;
                        show(binding.llBack, binding.ivBack, file);
                        break;
                    case LEFT:
                        leftImg = file;
                        show(binding.llLeft, binding.ivLeft, file);
                        break;
                    case RIGHT:
                        rightImg = file;
                        show(binding.llRight, binding.ivRight, file);
                        break;
                    case INT_FRONT:
                        intFrontImg = file;
                        show(binding.llFrontInt, binding.ivFrontInt, file);
                        break;
                    case INT_BACK:
                        intBackImg = file;
                        show(binding.llInt2, binding.ivInt2, file);
                        break;
                    case METER:
                        meterImg = file;
                        show(binding.llMeter, binding.ivMeter, file);
                        break;
                    case TOOLKIT:
                        toolkitImg = file;
                        show(binding.llToolkit, binding.ivToolkit, file);
                        break;
                    case SPARE:
                        spareImg = file;
                        show(binding.llSpare, binding.ivSpare, file);
                        break;
                    case SCRATCH:
                        scratchImages.add(file);
                        scratchAdapter.notifyDataSetChanged();
                        break;
                }

                updateUploadButtonState();
            });

    ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
                if (r.getResultCode() == Activity.RESULT_OK) launchCropper();
            });

    ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
                if (r.getResultCode() == Activity.RESULT_OK && r.getData() != null) {
                    imageUri = r.getData().getData();
                    launchCropper();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityUploadCarImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initClicks();
        setupScratches();
        updateUploadButtonState();
    }

    private void initClicks() {
        binding.llFront.setOnClickListener(v -> pick(FRONT));
        binding.llBack.setOnClickListener(v -> pick(BACK));
        binding.llLeft.setOnClickListener(v -> pick(LEFT));
        binding.llRight.setOnClickListener(v -> pick(RIGHT));

        binding.llFrontInt.setOnClickListener(v -> pick(INT_FRONT));
        binding.llInt2.setOnClickListener(v -> pick(INT_BACK));

        binding.llMeter.setOnClickListener(v -> pick(METER));
        binding.llToolkit.setOnClickListener(v -> pick(TOOLKIT));
        binding.llSpare.setOnClickListener(v -> pick(SPARE));

        binding.btnUpload.setOnClickListener(v -> {
            if (!isValid()) return;
            callUploadCarImagesApi();
        });
    }

    private void setupScratches() {
        scratchAdapter = new ScratchImageAdapter(scratchImages, () -> pick(SCRATCH));
        binding.rvScratches.setLayoutManager(
                new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        binding.rvScratches.setAdapter(scratchAdapter);
    }

    private void pick(int t) {
        type = t;
        imagePickerDialog();
    }

    private void imagePickerDialog() {
        dialog = new Dialog(this, R.style.my_dialog);
        dialog.setContentView(R.layout.image_selection_dialog);
        dialog.show();

        dialog.findViewById(R.id.ivCamera).setOnClickListener(v -> checkCamera());
        dialog.findViewById(R.id.ivGallery).setOnClickListener(v -> checkGallery());
        dialog.findViewById(R.id.tvCancel).setOnClickListener(v -> dialog.dismiss());
    }

    private void checkCamera() {
        dialog.dismiss();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
        openCamera();
    }

    private void checkGallery() {
        dialog.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_GALLERY);
        }
        openGallery();
    }

    private void openCamera() {
        try {
            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File f = File.createTempFile("IMG_" + ts, ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", f);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        galleryLauncher.launch(i);
    }

    private void launchCropper() {
        CropImageOptions o = new CropImageOptions();
        o.fixAspectRatio = false;
        o.outputCompressQuality = 60;
        cropImage.launch(new CropImageContractOptions(imageUri, o));
    }

    private void show(android.view.View ll, ImageView iv, File f) {
        ll.setVisibility(GONE);
        iv.setVisibility(VISIBLE);
        Glide.with(this).load(f).into(iv);
    }

    private boolean isValid() {
        return true;/*frontImg != null && backImg != null &&
                leftImg != null && rightImg != null &&
                intFrontImg != null && intBackImg != null &&
                meterImg != null && toolkitImg != null &&
                spareImg != null;*/
    }

    private void updateUploadButtonState() {
        binding.btnUpload.setEnabled(isValid());
        binding.btnUpload.setAlpha(isValid() ? 1f : 0.4f);
    }


    private void callUploadCarImagesApi() {
showLoader();
        RequestBody rbUserId = RequestBody.create(
                okhttp3.MediaType.parse("text/plain"),
                Objects.requireNonNull(getIntent().getStringExtra(Constant.BundleExtras.BOOKING_ID))
        );

        MultipartBody.Part side1 = part("m_bking_side_img1", frontImg);
        MultipartBody.Part side2 = part("m_bking_side_img2", backImg);
        MultipartBody.Part side3 = part("m_bking_side_img3", leftImg);
        MultipartBody.Part side4 = part("m_bking_side_img4", rightImg);

        MultipartBody.Part int1 = part("m_bking_int_img1", intFrontImg);
        MultipartBody.Part int2 = part("m_bking_int_img2", intBackImg);

        MultipartBody.Part meter   = part("m_bking_meter_img", meterImg);
        MultipartBody.Part toolkit = part("m_bking_toolkit_img", toolkitImg);
        MultipartBody.Part spare   = part("m_bking_sphare_tyre", spareImg);

        List<MultipartBody.Part> scratchParts = new ArrayList<>();
        for (File f : scratchImages) {
            scratchParts.add(
                    MultipartBody.Part.createFormData(
                            "m_bking_scratch[]",
                            f.getName(),
                            RequestBody.create(
                                    okhttp3.MediaType.parse("multipart/form-data"), f
                            )
                    )
            );
        }

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        api.uploadSelfCarImages(
                rbUserId,
                side1, side2, side3, side4,
                int1, int2,
                meter, toolkit, spare,
                scratchParts
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                 showError(  "Car images uploaded successfully");
                 finish();
                 hideLoader();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showError( "Upload failed");
                hideLoader();
            }
        });
    }
/*    private MultipartBody.Part part(String key, File file) {
        return MultipartBody.Part.createFormData(
                key,
                file.getName(),
                RequestBody.create(
                        okhttp3.MediaType.parse("multipart/form-data"), file
                )
        );
    }*/
    private MultipartBody.Part part(String key, File file) {
        if (file == null) return null;

        return MultipartBody.Part.createFormData(
                key,
                file.getName(),
                RequestBody.create(
                        okhttp3.MediaType.parse("multipart/form-data"), file
                )
        );
    }

}
