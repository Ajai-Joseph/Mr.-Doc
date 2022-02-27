
package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PatientFragmentAdapter extends FragmentStateAdapter {
    public PatientFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1: return new PatientLabFragment();
            case 2: return new PatientCompletedFragment();
        }
        return new PatientServicesFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
