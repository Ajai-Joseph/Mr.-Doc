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
 * Use the {@link PatientLabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientLabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientLabFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    PatientLabAdapter patientLabAdapter;
    String id;
    FirebaseAuth firebaseAuth;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientLabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientLabFragment newInstance(String param1, String param2) {
        PatientLabFragment fragment = new PatientLabFragment();
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
        View view=inflater.inflate(R.layout.fragment_patient_lab, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rvPatientLab);

        id="uploadPDF"+firebaseAuth.getUid();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<PutPdf> options =
                new FirebaseRecyclerOptions.Builder<PutPdf>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(id), PutPdf.class)
                        .build();


        patientLabAdapter=new PatientLabAdapter(options);

        recyclerView.setAdapter(patientLabAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        patientLabAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       patientLabAdapter.stopListening();
    }
}