package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PatientProfileActivity extends AppCompatActivity {
    private ImageView patientProfilePic;
    private TextView patientProfileName,patientProfileEmail;
    private Button patientProfileUpdate,patientChangePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        patientProfilePic=(ImageView)findViewById(R.id.ivPatientProfilePic);
        patientProfileName=(TextView)findViewById(R.id.tvPatientProfileName);

        patientProfileEmail=(TextView)findViewById(R.id.tvPatientProfileEmail);
        patientProfileUpdate=(Button)findViewById(R.id.btnPatientProfileUpdate);
        patientChangePassword=(Button)findViewById(R.id.btnPatientChangePassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();


        DatabaseReference databaseReference=firebaseDatabase.getReference("Patients").child(firebaseAuth.getUid());

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(patientProfilePic);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                PatientProfile patientProfile =datasnapshot.getValue(PatientProfile.class);
                patientProfileName.setText("Name: "+ patientProfile.getUserName());
                patientProfileEmail.setText("Email: "+ patientProfile.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        patientProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientProfileActivity.this, PatientUpdateProfile.class));
            }
        });
        patientChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientProfileActivity.this,UpdatePassword.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId()))
        {
            case android.R.id.home:  onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}