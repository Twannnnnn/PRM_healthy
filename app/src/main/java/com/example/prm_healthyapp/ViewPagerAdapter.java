package com.example.prm_healthyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new SleepLogFragment(); // Fragment cho Sleep Log
        }
        else {
            return new OtherFragment(); // Fragment khác nếu cần
        }
    }

    @Override
    public int getItemCount() {
        return 1; // Số lượng tab
    }
}