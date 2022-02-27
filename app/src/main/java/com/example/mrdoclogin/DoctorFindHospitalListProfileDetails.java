package com.example.mrdoclogin;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
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

public class DoctorFindHospitalListProfileDetails extends AppCompatActivity {
    TextView hosName,hosEmail;
    Button hosRequest;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String hname,hemail,hId,dName,dEmail;
    ImageView hosProfilePic;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_find_hospital_list_details);




        hosProfilePic=(ImageView)findViewById(R.id.ivDoctorHospitalProfilePic);
        hosName=(TextView)findViewById(R.id.tvDoctorHospitalProfileName);
        hosEmail=(TextView)findViewById(R.id.tvDoctorHospitalProfileEmail);
        hosRequest=(Button)findViewById(R.id.btnDoctorHospitalRequest);


        hname=getIntent().getExtras().getString("hosname");
        hemail=getIntent().getExtras().getString("hosemail");
        hId=getIntent().getExtras().getString("hosid");
        hosName.setText("Name: "+hname);
        hosEmail.setText("Email: "+hemail);



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Doctors").child(firebaseAuth.getUid());
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(hId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(hosProfilePic);

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DoctorProfile doctorProfile=dataSnapshot.getValue(DoctorProfile.class);
                dName=doctorProfile.getUserName();
                dEmail=doctorProfile.getUserEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorFindHospitalListProfileDetails.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        hosRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pa="Doctor Request for "+hId;
                DatabaseReference myRef=firebaseDatabase.getReference(pa);
               DoctorFindHospitalListProfile doctorFindHospitalListProfile=new DoctorFindHospitalListProfile(dEmail,dName,firebaseAuth.getUid());
                myRef.child(firebaseAuth.getUid()).setValue(doctorFindHospitalListProfile);

                Toast.makeText(DoctorFindHospitalListProfileDetails.this, "Request Send", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(DoctorFindHospitalListProfileDetails.this,DoctorActivity.class));

            }
        });


    }
}