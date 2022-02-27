package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HospitalActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;


    TabLayout tabLayout;
    ViewPager2 pager2;
    HospitalFragmentAdapter hospitalFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        firebaseAuth= FirebaseAuth.getInstance();

        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        pager2=(ViewPager2)findViewById(R.id.view_pager2);

        FragmentManager fm=getSupportFragmentManager();
        hospitalFragmentAdapter=new HospitalFragmentAdapter(fm,getLifecycle());
        pager2.setAdapter(hospitalFragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("HOME"));
        tabLayout.addTab(tabLayout.newTab().setText("PATIENTS"));
        tabLayout.addTab(tabLayout.newTab().setText("DOCTORS"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hospitalmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.doctorRequests: startActivity(new Intent(HospitalActivity.this,HospitalDoctorRequestsList.class));
            break;
            case R.id.logoutMenu:  firebaseAuth.signOut();
                    finish();
                startActivity(new Intent(HospitalActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.profileMenu: startActivity(new Intent(HospitalActivity.this, HospitalProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}