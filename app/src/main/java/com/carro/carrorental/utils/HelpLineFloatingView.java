package com.carro.carrorental.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.carro.carrorental.R;

public class HelpLineFloatingView extends FrameLayout {

    private static final long DELAY_MS = 3000;
    private static final int PEEK_DP = 120; // slide amount

    private TextView helpBtn;
    private boolean isPeeked = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable peekRunnable = this::peekOut;

    private String phoneNumber = "18001020802";

    public HelpLineFloatingView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HelpLineFloatingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HelpLineFloatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END | Gravity.BOTTOM;
        setLayoutParams(params);

        // 🔒 SAME SHAPE, SAME STYLE AS YOUR CODE
        helpBtn = new TextView(context);
        helpBtn.setText("  24×7 Helpline");
        helpBtn.setTextColor(getResources().getColor(R.color.white));
        helpBtn.setTextSize(14f);
        helpBtn.setGravity(Gravity.CENTER_VERTICAL);
        helpBtn.setPadding(dp(20), dp(12), dp(24), dp(12));
        helpBtn.setBackgroundResource(R.drawable.bg_helpline_oval);
        helpBtn.setElevation(dp(6));

        helpBtn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_mobile, 0, 0, 0
        );
        helpBtn.setCompoundDrawablePadding(dp(8));

        addView(helpBtn);

        helpBtn.setOnClickListener(v -> {
            if (isPeeked) {
                slideBackAndCall();
            } else {
                dial();
            }
        });

        schedulePeek();
    }

    /* ---------------- LOGIC ---------------- */

    private void schedulePeek() {
        handler.removeCallbacks(peekRunnable);
        handler.postDelayed(peekRunnable, DELAY_MS);
    }

    private void peekOut() {
        if (isPeeked) return;
        isPeeked = true;

        // Wait until layout is measured
        helpBtn.post(() -> {
            int buttonWidth = helpBtn.getWidth();

            // Slide out only 65% of the button width
            float slideDistance = buttonWidth * 0.70f;

            helpBtn.animate()
                    .translationX(slideDistance)
                    .setDuration(300)
                    .start();
        });
    }


    private void slideBackAndCall() {
        isPeeked = false;

        helpBtn.animate()
                .translationX(0)
                .setDuration(250)
                .withEndAction(() -> {
                    schedulePeek();
                    //dial();
                })
                .start();
    }

    private void dial() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        getContext().startActivity(intent);
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }
}
