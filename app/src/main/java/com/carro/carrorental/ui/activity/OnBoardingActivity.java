package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityOnBoardingBinding;
import com.carro.carrorental.ui.adapter.OnBoardingAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;


    OnBoardingAdapter onBoardingAdapter;
    ViewPager viewPager;
    private static final long AUTO_SCROLL_DELAY = 3000; // Auto scroll delay in milliseconds
    private Handler handler;
    private Runnable autoScrollRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewPager = binding.vpOnBoarding;
        onBoardingAdapter = new OnBoardingAdapter(OnBoardingActivity.this);
        viewPager.setAdapter(onBoardingAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        binding.dotsIndicator.setViewPager(viewPager);

        handler = new Handler();

        // Initialize auto scroll runnable
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= onBoardingAdapter.getCount()) {
                    nextItem = 0;
                }
                viewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };

        // Start auto scrolling
        startAutoScroll();

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });


    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
         /*   if (position > 0) {
                binding.btnBack.setVisibility(View.VISIBLE);
            } else {
                binding.btnBack.setVisibility(View.INVISIBLE);
            }
            if (position == 2) {
                binding.btnNext.setText("Start");
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem() {
        return viewPager.getCurrentItem();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacks(autoScrollRunnable);
    }

    private void startAutoScroll() {
        // Remove existing callbacks to prevent duplicate callbacks
        handler.removeCallbacks(autoScrollRunnable);
        // Schedule auto scroll
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        // Remove callbacks to stop auto scroll
        handler.removeCallbacks(autoScrollRunnable);
    }
}