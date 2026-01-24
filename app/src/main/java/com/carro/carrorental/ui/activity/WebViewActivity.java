package com.carro.carrorental.ui.activity;

import static android.view.View.VISIBLE;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityWebViewBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

public class WebViewActivity extends BaseActivity {

    ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityWebViewBinding.inflate(getLayoutInflater());
            EdgeToEdge.enable(this);
            setContentView(binding.getRoot());
            if(Constant.WEBVIEW_TITLE.equals(getString(R.string.invoice))){
                binding.toolbar.ivDownload.setVisibility(VISIBLE);
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            initiateWeb();
            binding.toolbar.ivDownload.setOnClickListener(v->{
                downloadInvoice(Constant.WEBVIEW_URL);
            });
        } catch (Exception e) {
            // Handle layout inflation or WebView instantiation errors
            showErrorAndFallback("Failed to initialize WebView: " + e.getMessage());
        }
    }

    private void initiateWeb() {
        try {
            String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
            LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);
            setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

            // Configure WebView settings
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setBuiltInZoomControls(true);
            binding.webView.getSettings().setDisplayZoomControls(false);
            binding.webView.setWebViewClient(new MyWebViewClient());
            binding.webView.setVerticalScrollBarEnabled(true);
            binding.webView.setHorizontalScrollBarEnabled(true);

            // Load the URL
            binding.webView.loadUrl(Constant.WEBVIEW_URL);
        } catch (Exception e) {
            // Handle WebView configuration or URL loading errors
            showErrorAndFallback("Error loading web content: " + e.getMessage());
        }
    }

    private void showErrorAndFallback(String errorMessage) {
        // Show a toast to inform the user
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

        // Show an AlertDialog with options
        new AlertDialog.Builder(this)
                .setTitle("WebView Error")
                .setMessage("Unable to load web content. Would you like to open the URL in a browser instead?")
                .setPositiveButton("Open Browser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEBVIEW_URL));
                            startActivity(browserIntent);
                            finish(); // Close the activity after opening the browser
                        } catch (Exception e) {
                            Toast.makeText(WebViewActivity.this, "No browser found: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Close the activity
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void downloadInvoice(String url) {
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "No invoice URL found", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle("Invoice");
            request.setDescription("Downloading invoice...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Invoice.pdf");

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);

            Toast.makeText(this, "Invoice download started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                view.loadUrl(url);
                return true;
            } catch (Exception e) {
                showErrorAndFallback("Error loading URL: " + e.getMessage());
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                hideLoader();
                super.onPageFinished(view, url);
            } catch (Exception e) {
                showErrorAndFallback("Error completing page load: " + e.getMessage());
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            try {
                showLoader();
                super.onPageStarted(view, url, favicon);
            } catch (Exception e) {
                showErrorAndFallback("Error starting page load: " + e.getMessage());
            }
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            try {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            } catch (Exception e) {
                showErrorAndFallback("Error handling SSL issue: " + e.getMessage());
            }
        }
    }
}