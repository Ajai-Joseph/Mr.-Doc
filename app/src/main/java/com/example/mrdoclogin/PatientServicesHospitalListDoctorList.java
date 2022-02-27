package com.example.mrdoclogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class PatientServicesHospitalListDoctorList extends AppCompatActivity {
    RecyclerView recyclerView;
    String pathName,hId;
    PatientServicesHospitalListDoctorListAdapter patientServicesHospitalListDoctorListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_hospital_list_doctor_list);

        recyclerView = (RecyclerView) findViewById(R.id.rvPatientServicesHospitalListDoctorList);


        hId=getIntent().getExtras().getString("HospitalId");
        pathName="Doctors in Hospital "+hId;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<HospitalAcceptedDoctors> options =
                new FirebaseRecyclerOptions.Builder<HospitalAcceptedDoctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName), HospitalAcceptedDoctors.class)
                        .build();
        patientServicesHospitalListDoctorListAdapter = new PatientServicesHospitalListDoctorListAdapter(options);
        recyclerView.setAdapter(patientServicesHospitalListDoctorListAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        patientServicesHospitalListDoctorListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        patientServicesHospitalListDoctorListAdapter.stopListening();
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
        FirebaseRecyclerOptions<HospitalAcceptedDoctors> options =
                new FirebaseRecyclerOptions.Builder<HospitalAcceptedDoctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName).orderByChild("userName").startAt(str).endAt(str + "~"), HospitalAcceptedDoctors.class)
                        .build();
        patientServicesHospitalListDoctorListAdapter = new PatientServicesHospitalListDoctorListAdapter(options);
        patientServicesHospitalListDoctorListAdapter.startListening();
        recyclerView.setAdapter(patientServicesHospitalListDoctorListAdapter);

    }

}