package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DoctorNewServicesHomeRequestDetails extends AppCompatActivity {

    TextView patientName,patientEmail,patientAddress,patientDate,patientTime;
    String pName,pEmail,pAddress,pDate,pTime,pId;
    ImageView patientPic;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_new_services_home_request_details);

        firebaseStorage=FirebaseStorage.getInstance();

        patientPic=(ImageView)findViewById(R.id.ivDoctorNewServicesPatientDetailsPic);
        patientName=(TextView)findViewById(R.id.tvDoctorNewServicesPatientDetailsName);
        patientEmail=(TextView)findViewById(R.id.tvDoctorNewServicesPatientDetailsEmail);
        patientAddress=(TextView)findViewById(R.id.tvDoctorNewServicesPatientDetailsAddress);
        patientDate=(TextView)findViewById(R.id.tvDoctorNewServicesPatientDetailsDate);
        patientTime=(TextView)findViewById(R.id.tvDoctorNewServicesPatientDetailsTime);



        pName=getIntent().getExtras().getString("PatientName");
        pEmail=getIntent().getExtras().getString("PatientEmail");
        pAddress=getIntent().getExtras().getString("PatientAddress");
        pDate=getIntent().getExtras().getString("PatientDate");
        pTime=getIntent().getExtras().getString("PatientTime");
        pId=getIntent().getExtras().getString("PatientId");


        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(pId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(patientPic);

            }
        });
        patientName.setText(pName);
        patientEmail.setText(pEmail);
        patientAddress.setText(pAddress);
        patientDate.setText(pDate);
        patientTime.setText(pTime);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctorhomerequestpatientdetails,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.doctorHomeRequestPatientDetailsChat:   Intent intent = new Intent(DoctorNewServicesHomeRequestDetails.this, ChatActivity.class);
                                                             intent.putExtra("chatname",pName);
                                                             intent.putExtra("recieverid",pId);
                                                             startActivity(intent);
                                                             break;



        }
        return super.onOptionsItemSelected(item);
    }
}