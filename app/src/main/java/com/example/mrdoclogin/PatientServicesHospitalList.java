package com.example.mrdoclogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class PatientServicesHospitalList extends AppCompatActivity {
    RecyclerView recyclerView;
    PatientServicesHospitalListAdapter patientServicesHospitalListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_hospital_list);


        recyclerView = (RecyclerView) findViewById(R.id.rvPatientServicesHospitalList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<HospitalProfile> options =
                new FirebaseRecyclerOptions.Builder<HospitalProfile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hospitals"), HospitalProfile.class)
                        .build();
        patientServicesHospitalListAdapter = new PatientServicesHospitalListAdapter(options);
        recyclerView.setAdapter(patientServicesHospitalListAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        patientServicesHospitalListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        patientServicesHospitalListAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<HospitalProfile> options =
                new FirebaseRecyclerOptions.Builder<HospitalProfile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hospitals").orderByChild("userName").startAt(str).endAt(str + "~"), HospitalProfile.class)
                        .build();
        patientServicesHospitalListAdapter = new PatientServicesHospitalListAdapter(options);
        patientServicesHospitalListAdapter.startListening();
        recyclerView.setAdapter(patientServicesHospitalListAdapter);

    }
}