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

public class DoctorProfileActivity extends AppCompatActivity {
    private ImageView doctorProfilePic;
    private TextView doctorProfileName,doctorProfileEmail;
    private Button doctorProfileUpdate,doctorChangePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        doctorProfilePic=(ImageView)findViewById(R.id.ivDoctorProfilePic);
        doctorProfileName=(TextView)findViewById(R.id.tvDoctorProfileName);

        doctorProfileEmail=(TextView)findViewById(R.id.tvDoctorProfileEmail);
        doctorProfileUpdate=(Button)findViewById(R.id.btnDoctorProfileUpdate);
        doctorChangePassword=(Button)findViewById(R.id.btnDoctorChangePassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        DatabaseReference databaseReference=firebaseDatabase.getReference("Doctors").child(firebaseAuth.getUid());

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(doctorProfilePic);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                DoctorProfile doctorProfile =datasnapshot.getValue(DoctorProfile.class);
                doctorProfileName.setText("Name: "+ doctorProfile.getUserName());
                doctorProfileEmail.setText("Email: "+ doctorProfile.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        doctorProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorProfileActivity.this, DoctorUpdateProfile.class));
            }
        });
        doctorChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorProfileActivity.this,UpdatePassword.class));
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