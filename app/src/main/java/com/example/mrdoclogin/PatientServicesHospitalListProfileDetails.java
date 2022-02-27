package com.example.mrdoclogin;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.squareup.picasso.Picasso;

public class PatientServicesHospitalListProfileDetails extends AppCompatActivity {
    TextView hosName,hosEmail;
    ImageView hosPic;
   Button hosChat;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    String hname,hemail,hId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_hospital_list_profile_details);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        hosPic=(ImageView)findViewById(R.id.ivPatientHospitalProfilePic);
        hosName=(TextView)findViewById(R.id.tvPatientHospitalProfileName);
        hosEmail=(TextView)findViewById(R.id.tvPatientHospitalProfileEmail);
        hosChat=(Button)findViewById(R.id.btnPatientHospitalChat);


        hname=getIntent().getExtras().getString("hosname");
        hemail=getIntent().getExtras().getString("hosemail");
        hId=getIntent().getExtras().getString("hosid");
        hosName.setText("Name: "+hname);
        hosEmail.setText("Email: "+hemail);




        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(hId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(hosPic);

            }
        });

        hosChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientServicesHospitalListProfileDetails.this, ChatActivity.class);
                intent.putExtra("chatname",hname);
                intent.putExtra("recieverid",hId);
                startActivity(intent);
                // startActivity(new Intent(PatientServicesDoctorListProfileDetails.this,ChatActivity.class));
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patientserviceshospitallistprofile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.patientServicesHospitalListFindDoctors:   Intent intent = new Intent(PatientServicesHospitalListProfileDetails.this, PatientServicesHospitalListDoctorList.class);
                intent.putExtra("HospitalId",hId);
                startActivity(intent);
                break;



        }
        return super.onOptionsItemSelected(item);
    }
}