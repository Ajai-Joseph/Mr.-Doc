package com.example.mrdoclogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HospitalPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HospitalPatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HospitalPatientFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    HospitalPatientAdapter hospitalPatientAdapter;
    String pathName;
    FirebaseAuth firebaseAuth;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HospitalPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HospitalPatientFragment newInstance(String param1, String param2) {
        HospitalPatientFragment fragment = new HospitalPatientFragment();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth=FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.fragment_hospital_patient, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rvHospitalPatient);

        pathName="Patients in Hospital "+firebaseAuth.getUid();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<PatientsOfHospital> options =
                new FirebaseRecyclerOptions.Builder<PatientsOfHospital>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName), PatientsOfHospital.class)
                        .build();


        hospitalPatientAdapter=new HospitalPatientAdapter(options);

        recyclerView.setAdapter(hospitalPatientAdapter);
        return view;


    }
    @Override
    public void onStart() {
        super.onStart();
        hospitalPatientAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       hospitalPatientAdapter.stopListening();
    }

    }
