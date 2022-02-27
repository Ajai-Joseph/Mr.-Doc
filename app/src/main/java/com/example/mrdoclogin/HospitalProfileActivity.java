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

public class HospitalProfileActivity extends AppCompatActivity {
    private ImageView hospitalProfilePic;
    private TextView hospitalProfileName,hospitalProfileEmail;
    private Button hospitalProfileUpdate,hospitalChangePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        hospitalProfilePic=(ImageView)findViewById(R.id.ivPatientDoctorProfilePic);
        hospitalProfileName=(TextView)findViewById(R.id.tvHospitalProfileName);

        hospitalProfileEmail=(TextView)findViewById(R.id.tvHospitalProfileEmail);
        hospitalProfileUpdate=(Button)findViewById(R.id.btnHospitalProfileUpdate);
        hospitalChangePassword=(Button)findViewById(R.id.btnHospitalChangePassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        DatabaseReference databaseReference=firebaseDatabase.getReference("Hospitals").child(firebaseAuth.getUid());

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(hospitalProfilePic);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                HospitalProfile hospitalProfile =datasnapshot.getValue(HospitalProfile.class);
                hospitalProfileName.setText("Name: "+ hospitalProfile.getUserName());
                hospitalProfileEmail.setText("Email: "+ hospitalProfile.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HospitalProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        hospitalProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HospitalProfileActivity.this, HospitalUpdateProfile.class));
            }
        });
        hospitalChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HospitalProfileActivity.this,UpdatePassword.class));
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