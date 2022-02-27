package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class DoctorRegistration extends AppCompatActivity {

    private EditText doctorName,doctorPassword,doctorEmail;
    private ImageView doctorProfilePic;
    private Button doctorRegButton;
    private TextView doctorLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    String name,email,password,type;
    private static int PICK_IMAGE=123;
    private StorageReference storageReference;
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                doctorProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        doctorName=(EditText)findViewById(R.id.etDoctorName);
        doctorPassword=(EditText)findViewById(R.id.etDoctorPassword);
        doctorEmail=(EditText)findViewById(R.id.etDoctorEmail);
        doctorRegButton=(Button)findViewById(R.id.btnDoctorRegister);
        doctorLogin=(TextView)findViewById(R.id.tvDoctorLogin);
        doctorProfilePic=(ImageView)findViewById(R.id.ivDoctorProfile);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();

        doctorProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"), PICK_IMAGE);
            }
        });
        doctorRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    String doctor_email=doctorEmail.getText().toString().trim();
                    String doctor_password=doctorPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(doctor_email,doctor_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendDoctorData();
                                firebaseAuth.signOut();
                                Toast.makeText(DoctorRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(DoctorRegistration.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(DoctorRegistration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        doctorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DoctorRegistration.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private Boolean validate()
    {
        Boolean result=false;
        name=doctorName.getText().toString();
        password=doctorPassword.getText().toString();
        email=doctorEmail.getText().toString();
        type="Doctor";


        if(name.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result=true;
        }
        return result;
    }
    private void sendDoctorData()
    {
        DoctorProfile doctorProfile =new DoctorProfile(email,name,type,firebaseAuth.getUid());
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("Doctors");
        myRef.child(firebaseAuth.getUid()).setValue(doctorProfile);

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.setValue(doctorProfile);


        StorageReference imageReference=storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask=imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorRegistration.this, "Upload failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorRegistration.this, "Upload successful", Toast.LENGTH_SHORT).show();

            }
        });

    }
}