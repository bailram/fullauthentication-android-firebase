package com.bailram.fullauthentication.utility;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bailram.fullauthentication.fragment.LoginFragment;
import com.bailram.fullauthentication.fragment.SignupFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numberOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int numberOfTabs) {
        super(fm, behavior);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new LoginFragment();
            case 1:
                return new SignupFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
