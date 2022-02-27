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


public class PatientServicesDoctorList extends AppCompatActivity {
    RecyclerView recyclerView;
    PatientServicesDoctorListAdapter patientServicesDoctorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_doctor_list);


        recyclerView = (RecyclerView) findViewById(R.id.rvPatientServicesDoctorList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<AcceptedDoctors> options =
                new FirebaseRecyclerOptions.Builder<AcceptedDoctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Accepted Doctors"), AcceptedDoctors.class)
                        .build();
        patientServicesDoctorListAdapter = new PatientServicesDoctorListAdapter(options);
        recyclerView.setAdapter(patientServicesDoctorListAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        patientServicesDoctorListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        patientServicesDoctorListAdapter.stopListening();
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
        FirebaseRecyclerOptions<AcceptedDoctors> options =
                new FirebaseRecyclerOptions.Builder<AcceptedDoctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Accepted Doctors").orderByChild("userName").startAt(str).endAt(str + "~"), AcceptedDoctors.class)
                        .build();
        patientServicesDoctorListAdapter = new PatientServicesDoctorListAdapter(options);
        patientServicesDoctorListAdapter.startListening();
        recyclerView.setAdapter(patientServicesDoctorListAdapter);

    }
}