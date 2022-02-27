package com.example.mrdoclogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorNewServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorNewServicesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorNewServicesFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    DoctorNewServicesHomeRequestAdapter doctorNewServicesHomeRequestAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String dName,temp;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorNewServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorNewServicesFragment newInstance(String param1, String param2) {
        DoctorNewServicesFragment fragment = new DoctorNewServicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        View view=inflater.inflate(R.layout.fragment_doctor_new_services, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rvDoctorNewServices);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        


       String paname="Home Requests for "+firebaseAuth.getUid();
        FirebaseRecyclerOptions<PatientServicesDoctorHomeRequestProfile> options =
                new FirebaseRecyclerOptions.Builder<PatientServicesDoctorHomeRequestProfile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(paname), PatientServicesDoctorHomeRequestProfile.class)
                        .build();
        doctorNewServicesHomeRequestAdapter=new DoctorNewServicesHomeRequestAdapter(options);
        recyclerView.setAdapter(doctorNewServicesHomeRequestAdapter);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        doctorNewServicesHomeRequestAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        doctorNewServicesHomeRequestAdapter.stopListening();
    }


}



