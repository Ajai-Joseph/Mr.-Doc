package com.example.mrdoclogin;

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
 * Use the {@link HospitalDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HospitalDoctorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HospitalDoctorFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    HospitalDoctorAdapter hospitalDoctorAdapter;
    String pathName;
    FirebaseAuth firebaseAuth;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HospitalDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HospitalDoctorFragment newInstance(String param1, String param2) {
        HospitalDoctorFragment fragment = new HospitalDoctorFragment();
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
        View view=inflater.inflate(R.layout.fragment_hospital_doctor, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rvHospitalDoctor);

        firebaseAuth=FirebaseAuth.getInstance();
        pathName="Doctors in Hospital "+firebaseAuth.getUid();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<HospitalAcceptedDoctors> options =
                new FirebaseRecyclerOptions.Builder<HospitalAcceptedDoctors>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(pathName), HospitalAcceptedDoctors.class)
                        .build();
        hospitalDoctorAdapter=new HospitalDoctorAdapter(options);
        recyclerView.setAdapter(hospitalDoctorAdapter);
        return view;


    }
    @Override
    public void onStart() {
        super.onStart();
        hospitalDoctorAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        hospitalDoctorAdapter.stopListening();
    }

}