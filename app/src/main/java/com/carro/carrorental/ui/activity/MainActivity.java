package com.carro.carrorental.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityMainBinding;
import com.carro.carrorental.model.LoginModel;
import com.carro.carrorental.ui.common.BaseActivity;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    public static NavController navController = null;
    public static MainActivity mainActivity;
    LoginModel loginModel = new LoginModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });        mainActivity = this;
        getUserPreferences();
    }

    private void getUserPreferences() {

        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, MainActivity.this);
        loginModel = new Gson().fromJson(userData, LoginModel.class);


        setUpToolBar(binding.toolbar,this,loginModel.getmCustImg());

        initialization();
    }

    private void initialization() {





        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_nav_fragment);
        assert navHost != null;
        navController = navHost.getNavController();
        // NavigationUI.setupWithNavController(binding.navBottom, navController);


        if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
            String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
            String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
            String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
            String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);

            String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
            String lngpick  = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
            String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
            String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);

            Bundle bundle = new Bundle();
            bundle.putString("address_type", address_type);
            bundle.putString("address_main_pick", address_main_pick);
            bundle.putString("address_main_drop", address_main_drop);
            bundle.putString("address_from", address_from);

            bundle.putString("lat_pick", latpick);
            bundle.putString("lat_drop", latDrop);
            bundle.putString("lng_pick", lngpick);
            bundle.putString("lng_drop", lngDrop);

            switch (address_from) {
                case "1":
                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
                    //navigateToFragment(R.id.nav_one_way, bundle);
                    break;
                case "2":
                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
                    // navigateToFragment(R.id.nav_round_trip, bundle);
                    break;
                case "3":
                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
                    //navigateToFragment(R.id.nav_hourly, bundle);
                    break;
                case "4":
                    binding.navBottom.setSelectedItemId(R.id.nav_flight);
                    // navigateToFragment(R.id.nav_flight, bundle);
                    break;
                case "5":
                    navigateToFragment(R.id.nav_flight, bundle);
                    break;
                default:
                    Log.d("SwitchToNavigate", "SwitchValue: " + address_from);
                    break;
            }
        }

        // Set manual listeners for each menu item since IDs are different
        binding.navBottom.setOnItemSelectedListener(item -> {
            Bundle bundle = new Bundle();
            if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
                String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
                String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
                String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
                String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);

                String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
                String lngpick = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
                String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
                String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);

                bundle.putString("address_type", address_type);
                bundle.putString("address_main_pick", address_main_pick);
                bundle.putString("address_main_drop", address_main_drop);
                bundle.putString("address_from", address_from);

                bundle.putString("lat_pick", latpick);
                bundle.putString("lat_drop", latDrop);
                bundle.putString("lng_pick", lngpick);
                bundle.putString("lng_drop", lngDrop);

            }
            int itemId = item.getItemId();
            if (itemId == R.id.nav_one_way) {  // Menu ID
                navController.navigate(R.id.nav_one_way,bundle);  // Fragment ID
                return true;
            } else if (itemId == R.id.nav_round_trip) {  // Menu ID
                navController.navigate(R.id.nav_round_trip,bundle);  // Fragment ID
                return true;
            } else if (itemId == R.id.nav_hourly) {  // Menu ID
                navController.navigate(R.id.nav_hourly,bundle);  // Fragment ID
                return true;
            } else if (itemId == R.id.nav_flight) {  // Menu ID
                navController.navigate(R.id.nav_flight,bundle);  // Fragment ID
                return true;
            }
            return false;
        });


        if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
            String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
            String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
            String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
            String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);

            String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
            String lngpick  = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
            String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
            String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);

            Bundle bundle = new Bundle();
            bundle.putString("address_type", address_type);
            bundle.putString("address_main_pick", address_main_pick);
            bundle.putString("address_main_drop", address_main_drop);
            bundle.putString("address_from", address_from);

            bundle.putString("lat_pick", latpick);
            bundle.putString("lat_drop", latDrop);
            bundle.putString("lng_pick", lngpick);
            bundle.putString("lng_drop", lngDrop);

            switch (address_from) {
                case "1":
                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
//                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
                    //navigateToFragment(R.id.nav_one_way, bundle);
                    break;
                case "2":
//                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
                    // navigateToFragment(R.id.nav_round_trip, bundle);
                    break;
                case "3":
                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
//                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
                    //navigateToFragment(R.id.nav_hourly, bundle);
                    break;
                case "4":
                    binding.navBottom.setSelectedItemId(R.id.nav_flight);
                    // navigateToFragment(R.id.nav_flight, bundle);
                    break;
                case "5":
                    navigateToFragment(R.id.nav_flight, bundle);
                    break;
                default:
                    Log.d("SwitchToNavigate", "SwitchValue: " + address_from);
                    break;
            }
        }

    }

    public void navigateToFragment(int id) {
        navController.navigate(id);
    }

    public void navigateToFragment(int id, Bundle bundle) {
        navController.navigate(id, bundle);


    }

    public void removeFromBackStack(int fragmentCard) {
        navController.popBackStack(fragmentCard, true);
    }




}

































//package com.logixhunt.carrorental.ui.activity;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//
//import androidx.activity.EdgeToEdge;
//import androidx.activity.OnBackPressedCallback;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.GravityCompat;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.bumptech.glide.Glide;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.android.material.navigation.NavigationView;
//import com.google.gson.Gson;
//import com.logixhunt.carrorental.R;
//import com.logixhunt.carrorental.databinding.ActivityMainBinding;
//import com.logixhunt.carrorental.databinding.LogoutBottomDialogBinding;
//import com.logixhunt.carrorental.model.LoginModel;
//import com.logixhunt.carrorental.utils.Constant;
//import com.logixhunt.carrorental.utils.ImagePathDecider;
//import com.logixhunt.carrorental.utils.PreferenceUtils;
//
//public class MainActivity extends AppCompatActivity {
//
//    ActivityMainBinding binding;
//    private AppBarConfiguration appBarConfiguration;
//    public static NavController navController = null;
//    public static MainActivity mainActivity;
//    LoginModel loginModel = new LoginModel();
//    Dialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);
//        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });        mainActivity = this;
//        getUserPreferences();
//    }
//
//    private void getUserPreferences() {
//
//        String userData = PreferenceUtils.getString(Constant.PreferenceConstant.USER_DATA, MainActivity.this);
//        loginModel = new Gson().fromJson(userData, LoginModel.class);
//
//        if (!loginModel.getmCustImg().isEmpty()){
//            Glide.with(MainActivity.this)
//                    .load(ImagePathDecider.getUserImagePath()+loginModel.getmCustImg())
//                    .error(R.drawable.img_no_profile)
//                    .into(binding.ivUser);
//        }
//
//        binding.ivUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,ProfileDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//        initialization();
//    }
//
//    private void initialization() {
//
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    if (navController.getCurrentDestination() != null &&
//                            navController.getCurrentDestination().getId() == R.id.nav_dashboard) {
//                        // Do nothing or maybe move task to back
//                        // moveTaskToBack(true);
//                    } else {
//                        // Allow system default back press
//                        setEnabled(false); // Disable this callback
//                        getOnBackPressedDispatcher().onBackPressed();
//                    }
//                }
//            }
//        });
//
//        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_nav_fragment);
//        assert navHost != null;
//        navController = navHost.getNavController();
//       // NavigationUI.setupWithNavController(binding.navBottom, navController);
//
//
//        if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
//            String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
//            String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
//            String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
//            String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);
//
//            String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
//            String lngpick  = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
//            String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
//            String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);
//
//            Bundle bundle = new Bundle();
//            bundle.putString("address_type", address_type);
//            bundle.putString("address_main_pick", address_main_pick);
//            bundle.putString("address_main_drop", address_main_drop);
//            bundle.putString("address_from", address_from);
//
//            bundle.putString("lat_pick", latpick);
//            bundle.putString("lat_drop", latDrop);
//            bundle.putString("lng_pick", lngpick);
//            bundle.putString("lng_drop", lngDrop);
//
//            switch (address_from) {
//                case "1":
//                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
//                    //navigateToFragment(R.id.nav_one_way, bundle);
//                    break;
//                case "2":
//                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
//                    // navigateToFragment(R.id.nav_round_trip, bundle);
//                    break;
//                case "3":
//                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
//                    //navigateToFragment(R.id.nav_hourly, bundle);
//                    break;
//                case "4":
//                    binding.navBottom.setSelectedItemId(R.id.nav_flight);
//                    // navigateToFragment(R.id.nav_flight, bundle);
//                    break;
//                case "5":
//                    navigateToFragment(R.id.nav_flight, bundle);
//                    break;
//                default:
//                    Log.d("SwitchToNavigate", "SwitchValue: " + address_from);
//                    break;
//            }
//        }
//
//        // Set manual listeners for each menu item since IDs are different
//        binding.navBottom.setOnItemSelectedListener(item -> {
//            Bundle bundle = new Bundle();
//            if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
//                String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
//                String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
//                String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
//                String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);
//
//                String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
//                String lngpick = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
//                String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
//                String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);
//
//                bundle.putString("address_type", address_type);
//                bundle.putString("address_main_pick", address_main_pick);
//                bundle.putString("address_main_drop", address_main_drop);
//                bundle.putString("address_from", address_from);
//
//                bundle.putString("lat_pick", latpick);
//                bundle.putString("lat_drop", latDrop);
//                bundle.putString("lng_pick", lngpick);
//                bundle.putString("lng_drop", lngDrop);
//
//            }
//            int itemId = item.getItemId();
//            if (itemId == R.id.nav_one_way) {  // Menu ID
//                navController.navigate(R.id.nav_one_way,bundle);  // Fragment ID
//                return true;
//            } else if (itemId == R.id.nav_round_trip) {  // Menu ID
//                navController.navigate(R.id.nav_round_trip,bundle);  // Fragment ID
//                return true;
//            } else if (itemId == R.id.nav_hourly) {  // Menu ID
//                navController.navigate(R.id.nav_hourly,bundle);  // Fragment ID
//                return true;
//            } else if (itemId == R.id.nav_flight) {  // Menu ID
//                navController.navigate(R.id.nav_flight,bundle);  // Fragment ID
//                return true;
//            }
//            return false;
//        });
//
//
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//        navController = Navigation.findNavController(this, R.id.bottom_nav_fragment);
//        appBarConfiguration = new AppBarConfiguration.Builder(
//                navController.getGraph()
//        ).setDrawerLayout(drawer).build();
//
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    MainActivity.this, drawer, binding.toolbar,
//                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//            drawer.addDrawerListener(toggle);
//            toggle.syncState();
//
//            // change drawer icon
//            toggle.setDrawerIndicatorEnabled(false);
//            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu_24, getTheme());
//            toggle.setHomeAsUpIndicator(drawable);
//            toggle.setToolbarNavigationClickListener(v -> {
//                if (drawer.isDrawerVisible(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//            });
//        });
//
//
///*        binding.navView.getMenu().findItem(R.id.nav_one_way).setOnMenuItemClickListener(menuItem -> {
//            navigateToFragment(R.id.nav_one_way);
//            binding.navBottom.setSelectedItemId(R.id.nav_one_way);
//
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_round_trip).setOnMenuItemClickListener(menuItem -> {
//            navigateToFragment(R.id.nav_round_trip);
//            binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_hourly).setOnMenuItemClickListener(menuItem -> {
//            navigateToFragment(R.id.nav_hourly);
//            binding.navBottom.setSelectedItemId(R.id.nav_hourly);
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_flight).setOnMenuItemClickListener(menuItem -> {
//            navigateToFragment(R.id.nav_flight);
//            binding.navBottom.setSelectedItemId(R.id.nav_flight);
//            return false;
//        });*/
//
//
//
//
//        binding.navView.getMenu().findItem(R.id.nav_my_bookings).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, MyBookingsActivity.class);
//            startActivity(intent);
//            return false;
//        });
//
//        binding.navView.getMenu().findItem(R.id.nav_notifications).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, NotificationActivity.class);
//            startActivity(intent);
//            return false;
//        });
//
//        binding.navView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, ProfileDetailActivity.class);
//            startActivity(intent);
//            return false;
//        });
//
//        binding.navView.getMenu().findItem(R.id.nav_home).setOnMenuItemClickListener(menuItem -> {
//            finish();
//            return false;
//        });
//
//        binding.navView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
//            logoutBottomDialog();
//            return false;
//        });
///*        binding.navView.getMenu().findItem(R.id.nav_self_drive).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, SelfDriveActivity.class);
//            startActivity(intent);
//            return false;
//        });*/
//        binding.navView.getMenu().findItem(R.id.nav_rate).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, RateUsActivity.class);
//            startActivity(intent);
//            return false;
//        });
///*        binding.navView.getMenu().findItem(R.id.nav_offers).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            return false;
//        });*/
///*        binding.navView.getMenu().findItem(R.id.nav_packages).setOnMenuItemClickListener(menuItem -> {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
//            return false;
//        });*/
//        binding.navView.getMenu().findItem(R.id.nav_t_c).setOnMenuItemClickListener(menuItem -> {
//            Constant.WEBVIEW_TITLE = getString(R.string.terms_condition);
//            Constant.WEBVIEW_URL = getString(R.string.terms_and_condition_url);
//            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//            startActivity(intent);
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_policy).setOnMenuItemClickListener(menuItem -> {
//            Constant.WEBVIEW_TITLE = getString(R.string.privacy_policy);
//            Constant.WEBVIEW_URL = getString(R.string.privacy_policy_url);
//            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//            startActivity(intent);
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_contact_us).setOnMenuItemClickListener(menuItem -> {
//            Constant.WEBVIEW_TITLE = getString(R.string.contact_us);
//            Constant.WEBVIEW_URL = getString(R.string.contact_us_url);
//            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//            startActivity(intent);
//            return false;
//        });
//        binding.navView.getMenu().findItem(R.id.nav_about_us).setOnMenuItemClickListener(menuItem -> {
//            Constant.WEBVIEW_TITLE = getString(R.string.about_us);
//            Constant.WEBVIEW_URL = getString(R.string.about_us_url);
//            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//            startActivity(intent);
//            return false;
//        });
//        binding.ivNotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        if (getIntent().hasExtra(Constant.BundleExtras.ADDRESS_TYPE) && getIntent().hasExtra(Constant.BundleExtras.ADDRESS_FROM)) {
//            String address_type = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_TYPE);
//            String address_from = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_FROM);
//            String address_main_pick = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_PICK);
//            String address_main_drop = getIntent().getStringExtra(Constant.BundleExtras.ADDRESS_MAIN_DROP);
//
//            String latpick = getIntent().getStringExtra(Constant.BundleExtras.LAT_PICK);
//            String lngpick  = getIntent().getStringExtra(Constant.BundleExtras.LNG_PICK);
//            String latDrop = getIntent().getStringExtra(Constant.BundleExtras.LAT_DROP);
//            String lngDrop = getIntent().getStringExtra(Constant.BundleExtras.LNG_DROP);
//
//            Bundle bundle = new Bundle();
//            bundle.putString("address_type", address_type);
//            bundle.putString("address_main_pick", address_main_pick);
//            bundle.putString("address_main_drop", address_main_drop);
//            bundle.putString("address_from", address_from);
//
//            bundle.putString("lat_pick", latpick);
//            bundle.putString("lat_drop", latDrop);
//            bundle.putString("lng_pick", lngpick);
//            bundle.putString("lng_drop", lngDrop);
//
//            switch (address_from) {
//                case "1":
//                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
////                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
//                    //navigateToFragment(R.id.nav_one_way, bundle);
//                    break;
//                case "2":
////                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
//                    binding.navBottom.setSelectedItemId(R.id.nav_one_way);
//                   // navigateToFragment(R.id.nav_round_trip, bundle);
//                    break;
//                case "3":
//                    binding.navBottom.setSelectedItemId(R.id.nav_round_trip);
////                    binding.navBottom.setSelectedItemId(R.id.nav_hourly);
//                    //navigateToFragment(R.id.nav_hourly, bundle);
//                    break;
//                case "4":
//                    binding.navBottom.setSelectedItemId(R.id.nav_flight);
//                   // navigateToFragment(R.id.nav_flight, bundle);
//                    break;
//                case "5":
//                    navigateToFragment(R.id.nav_flight, bundle);
//                    break;
//                default:
//                    Log.d("SwitchToNavigate", "SwitchValue: " + address_from);
//                    break;
//            }
//        }
//
//    }
//
//    public void navigateToFragment(int id) {
//        navController.navigate(id);
//    }
//
//    public void navigateToFragment(int id, Bundle bundle) {
//        navController.navigate(id, bundle);
//
//
//    }
//
//    public void removeFromBackStack(int fragmentCard) {
//        navController.popBackStack(fragmentCard, true);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_fragment);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//
//
//    private void logoutBottomDialog() {
//        LogoutBottomDialogBinding logoutBottomDialogBinding;
//        logoutBottomDialogBinding = LogoutBottomDialogBinding.inflate(getLayoutInflater());
//
//        dialog = new BottomSheetDialog(MainActivity.this);
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.setContentView(logoutBottomDialogBinding.getRoot());
//        dialog.show();
//
//        logoutBottomDialogBinding.ivCut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        logoutBottomDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        logoutBottomDialogBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                PreferenceUtils.setBoolean(Constant.PreferenceConstant.IS_LOGIN, false, MainActivity.this);
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        });
//
//
//    }
//}