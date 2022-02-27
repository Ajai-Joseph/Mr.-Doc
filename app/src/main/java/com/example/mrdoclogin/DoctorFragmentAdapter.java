
package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DoctorFragmentAdapter extends FragmentStateAdapter {
    public DoctorFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1: return new DoctorChatsFragment();
            case 2: return new DoctorCompletedServicesFragment();
        }
        return new DoctorNewServicesFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
