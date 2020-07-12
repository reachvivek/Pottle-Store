package com.negociscorp.pottlestore.adapters.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.negociscorp.pottlestore.ui.fragments.FragmentFoodstore;
import com.negociscorp.pottlestore.ui.fragments.FragmentGreenstore;
import com.negociscorp.pottlestore.ui.fragments.FragmentHygienestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Code written by Vivek Kumar Singh on 11/07/2020.
 */

public class DashboardAdapter extends FragmentStateAdapter {

    public DashboardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentFoodstore();
            case 1:
                return new FragmentHygienestore();
            default:
                return new FragmentGreenstore();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
