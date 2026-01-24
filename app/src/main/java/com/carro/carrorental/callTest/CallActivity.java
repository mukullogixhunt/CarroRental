package com.carro.carrorental.callTest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.carro.carrorental.R;
import com.carro.carrorental.utils.Constant;
import com.carro.carrorental.utils.PreferenceUtils;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment;

public class CallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        addCallFragment();
    }

    public void addCallFragment() {
        long appID = 274926722;
        String appSign = "e30060a0eb5d0abdf5b03df48369e0b9120a6e1f963aee5a94976b54231a2732";

        String userId = PreferenceUtils.getString(Constant.PreferenceConstant.USER_ID, CallActivity.this);
        String callID = "callID_1";
        String userID = "userID_" + userId;
        String userName = "userName";

        // You can also use GroupVideo/GroupVoice/OneOnOneVoice to make more types of calls.
        ZegoUIKitPrebuiltCallConfig config = ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();

        ZegoUIKitPrebuiltCallFragment fragment = ZegoUIKitPrebuiltCallFragment.newInstance(
                appID, appSign, userID, userName, callID, config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}