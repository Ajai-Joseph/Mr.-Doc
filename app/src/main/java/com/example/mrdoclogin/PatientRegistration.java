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

public class PatientRegistration extends AppCompatActivity {

    private EditText patientName,patientPassword,patientEmail;
    private ImageView patientProfilePic;
    private Button patientRegButton;
    private TextView patientLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    String name,email,password,type,m;
    private static int PICK_IMAGE=123;
    private StorageReference storageReference;
    Uri imagePath,uri;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null)
        {
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                patientProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patientName=(EditText)findViewById(R.id.etPatientName);
        patientPassword=(EditText)findViewById(R.id.etPatientPassword);
        patientEmail=(EditText)findViewById(R.id.etPatientEmail);
        patientRegButton=(Button)findViewById(R.id.btnPatientRegister);
        patientLogin=(TextView)findViewById(R.id.tvPatientLogin);
        patientProfilePic=(ImageView)findViewById(R.id.ivPatientProfile);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();

        patientProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"), PICK_IMAGE);
            }
        });



        patientRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    String patient_email=patientEmail.getText().toString().trim();
                    String patient_password=patientPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(patient_email,patient_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendPatientData();
                                firebaseAuth.signOut();
                                Toast.makeText(PatientRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PatientRegistration.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(PatientRegistration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        patientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PatientRegistration.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private Boolean validate()
    {
        Boolean result=false;
        name=patientName.getText().toString();
        password=patientPassword.getText().toString();
        email=patientEmail.getText().toString();
        type="Patient";


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
    private void sendPatientData()
    {

        StorageReference imageReference=storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask=imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientRegistration.this, "Upload failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


            }
        });
        PatientProfile patientProfile =new PatientProfile(email,name,type,firebaseAuth.getUid(),"jhkgjhgg");
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("Patients");
        myRef.child(firebaseAuth.getUid()).setValue(patientProfile);

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.setValue(patientProfile);

    }
}








