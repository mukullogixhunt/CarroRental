package com.carro.carrorental.ui.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    /**
     * to show error messages only in Snackbar
     */
    protected void showError(String msg) {
        ((BaseActivity) getActivity()).showError(msg);
    }

    /**
     * Show Loader
     */
    protected void showLoader() {
        try {
            ((BaseActivity) getActivity()).showLoader();
        } catch (Exception e) {

        }
    }


    /**
     * Show alert
     */
    public void showAlert(String msg) {
        ((BaseActivity) getActivity()).showAlert(msg);
    }

    public void log_d(String className, String message) {
        ((BaseActivity) getActivity()).log_d(className, message);
    }

    public void log_e(String className, String message, Exception e) {
        ((BaseActivity) getActivity()).log_e(className, message, e);
    }

    /**
     * Hide Loader
     */
    protected void hideLoader() {
        try {
            ((BaseActivity) getActivity()).hideLoader();
        } catch (Exception e) {

        }
    }



}
