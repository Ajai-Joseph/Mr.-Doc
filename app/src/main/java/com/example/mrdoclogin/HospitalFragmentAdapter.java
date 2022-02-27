
package com.example.mrdoclogin;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.lifecycle.Lifecycle;
        import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HospitalFragmentAdapter extends FragmentStateAdapter {
    public HospitalFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1: return new HospitalPatientFragment();
            case 2: return new HospitalDoctorFragment();
        }
        return new HospitalHomeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
