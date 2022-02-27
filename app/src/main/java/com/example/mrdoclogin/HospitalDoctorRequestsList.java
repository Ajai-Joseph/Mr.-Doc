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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class HospitalDoctorRequestsList extends AppCompatActivity {
    RecyclerView recyclerView;
    HospitalDoctorRequestsListAdapter hospitalDoctorRequestsListAdapter;
    String pathName;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_requests_list);

        firebaseAuth=FirebaseAuth.getInstance();

        pathName="Doctor Request for "+firebaseAuth.getUid();
        recyclerView = (RecyclerView) findViewById(R.id.rvHospitalDoctorRequestsList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DoctorFindHospitalListProfile> options =
                new FirebaseRecyclerOptions.Builder<DoctorFindHospitalListProfile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName),DoctorFindHospitalListProfile.class)
                        .build();
        hospitalDoctorRequestsListAdapter = new HospitalDoctorRequestsListAdapter(options);
        recyclerView.setAdapter(hospitalDoctorRequestsListAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        hospitalDoctorRequestsListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hospitalDoctorRequestsListAdapter.stopListening();
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
        FirebaseRecyclerOptions<DoctorFindHospitalListProfile> options =
                new FirebaseRecyclerOptions.Builder<DoctorFindHospitalListProfile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName).orderByChild("userName").startAt(str).endAt(str + "~"), DoctorFindHospitalListProfile.class)
                        .build();
        hospitalDoctorRequestsListAdapter = new HospitalDoctorRequestsListAdapter(options);
        hospitalDoctorRequestsListAdapter.startListening();
        recyclerView.setAdapter(hospitalDoctorRequestsListAdapter);

    }
}