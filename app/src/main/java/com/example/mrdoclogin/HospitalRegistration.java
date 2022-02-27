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

public class HospitalRegistration extends AppCompatActivity {

    private EditText hospitalName,hospitalPassword,hospitalEmail;
    private ImageView hospitalProfilePic;
    private Button hospitalRegButton;
    private TextView hospitalLogin;
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
                hospitalProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration);

        hospitalName=(EditText)findViewById(R.id.etHospitalName);
        hospitalPassword=(EditText)findViewById(R.id.etHospitalPassword);
        hospitalEmail=(EditText)findViewById(R.id.etHospitalEmail);
        hospitalRegButton=(Button)findViewById(R.id.btnHospitalRegister);
        hospitalLogin=(TextView)findViewById(R.id.tvHospitalLogin);
        hospitalProfilePic=(ImageView)findViewById(R.id.ivHospitalProfile);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();

        hospitalProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"), PICK_IMAGE);
            }
        });
        hospitalRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    String hospital_email=hospitalEmail.getText().toString().trim();
                    String hospital_password=hospitalPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(hospital_email,hospital_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendHospitalData();
                                firebaseAuth.signOut();
                                Toast.makeText(HospitalRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(HospitalRegistration.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(HospitalRegistration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        hospitalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HospitalRegistration.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private Boolean validate()
    {
        Boolean result=false;
        name=hospitalName.getText().toString();
        password=hospitalPassword.getText().toString();
        email=hospitalEmail.getText().toString();
        type="Hospital";


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
    private void sendHospitalData()
    {
        HospitalProfile hospitalProfile =new HospitalProfile(email,name,type,firebaseAuth.getUid());

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference("Hospitals");
        myRef.child(firebaseAuth.getUid()).setValue(hospitalProfile);

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.setValue(hospitalProfile);

        StorageReference imageReference=storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
        UploadTask uploadTask=imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HospitalRegistration.this, "Upload failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(HospitalRegistration.this, "Upload successful", Toast.LENGTH_SHORT).show();

            }
        });

    }
}