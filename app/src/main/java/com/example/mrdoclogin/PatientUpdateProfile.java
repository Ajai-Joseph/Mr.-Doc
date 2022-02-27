package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PatientUpdateProfile extends AppCompatActivity {
    private EditText newPatientName,newPatientEmail;
    private Button patientSave;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private ImageView updateProfilePic;
    private static int PICK_IMAGE=123;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    Uri imagePath;
    String itemType;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                updateProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_update_profile);

        newPatientName=(EditText)findViewById(R.id.etPatientNameUpdate);
        newPatientEmail=(EditText)findViewById(R.id.etPatientEmailUpdate);
        patientSave=(Button)findViewById(R.id.btnPatientSave);
        updateProfilePic=(ImageView)findViewById(R.id.ivPatientProfileUpdate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference=firebaseDatabase.getReference("Patients").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                PatientProfile patientProfile =datasnapshot.getValue(PatientProfile.class);
                newPatientName.setText(patientProfile.getUserName());
                newPatientEmail.setText(patientProfile.getUserEmail());
                itemType= patientProfile.getUserType();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientUpdateProfile.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(updateProfilePic);

            }
        });

        patientSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=newPatientEmail.getText().toString();
                firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


                String name=newPatientName.getText().toString();


                StorageReference imageReference=storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
                UploadTask uploadTask=imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientUpdateProfile.this, "Upload failed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(PatientUpdateProfile.this, "Upload successful", Toast.LENGTH_SHORT).show();

                    }
                });
                PatientProfile patientProfile =new PatientProfile(email,name,itemType,firebaseAuth.getUid(),imagePath.toString());
                databaseReference.setValue(patientProfile);
                finish();
            }
        });
        updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"), PICK_IMAGE);
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