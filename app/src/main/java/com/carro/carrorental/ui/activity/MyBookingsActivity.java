package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityMyBookingsBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.adapter.FlightTabAdapter;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.ui.fragment.myBookings.CurrentFragment;
import com.carro.carrorental.ui.fragment.myBookings.HistoryFragment;
import com.carro.carrorental.ui.fragment.myBookings.UpcomingFragment;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

public class MyBookingsActivity extends BaseActivity {

    ActivityMyBookingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyBookingsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Register a callback for back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(MyBookingsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // closes MyBookingsActivity
            }
        });


        initiateTabLayout();
        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, this);
        LoginModel loginModel = new Gson().fromJson(userData, LoginModel.class);
        setUpToolBar(binding.toolbar, this, loginModel.getmCustImg());

    }

    private void initiateTabLayout() {



        binding.tab.setupWithViewPager(binding.viewpager);
        FlightTabAdapter tabAdapter = new FlightTabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        CurrentFragment currentFragment = new CurrentFragment();

        HistoryFragment historyFragment = new HistoryFragment();

        UpcomingFragment upcomingFragment = new UpcomingFragment();

        tabAdapter.clearFragment();

        tabAdapter.addFragment(currentFragment, "Current");
        tabAdapter.addFragment(upcomingFragment, "Upcoming");
        tabAdapter.addFragment(historyFragment, "History");

        binding.viewpager.setAdapter(tabAdapter);

        if(getIntent().hasExtra(Constant.IS_FROM)){
            binding.viewpager.setCurrentItem(1);
        }

//        if (AllMentorModuleFragment.allMentorModuleFragment != null){
//            AllMentorModuleFragment.allMentorModuleFragment.courseDetailsModel = courseDetailsModel;
//            if(AllMentorModuleFragment.allMentorModuleFragment.moduleAdapter!= null){
//                AllMentorModuleFragment.allMentorModuleFragment.setDataToRecycle();
//            }
//        }

    }


}