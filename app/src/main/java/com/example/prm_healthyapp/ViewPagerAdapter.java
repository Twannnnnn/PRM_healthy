package com.example.prm_healthyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SleepLogFragment(); // First tab
            case 1:
                return new MealLogFragment(); // Second tab
            default:
                return new SleepLogFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}