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

public class HospitalDoctorRequestsListProfileDetails extends AppCompatActivity {


    TextView docName,docEmail;
    Button docAccept,docReject;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String dname,demail,dId,acceptPath,removePath;
    FirebaseStorage firebaseStorage;
    ImageView docPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_requests_list_profile_details);

        docPic=(ImageView)findViewById(R.id.ivHospitalDoctorRequestsProfilePic);
        docName=(TextView)findViewById(R.id.tvHospitalDoctorRequestsProfileName);
        docEmail=(TextView)findViewById(R.id.tvHospitalDoctorRequestsProfileEmail);
        docAccept=(Button)findViewById(R.id.btnHospitalDoctorRequestAccept);
        docReject=(Button)findViewById(R.id.btnHospitalDoctorRequestReject);

        dname=getIntent().getExtras().getString("docname");
        demail=getIntent().getExtras().getString("docemail");
        dId=getIntent().getExtras().getString("docid");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();



        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(dId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(docPic);

            }
        });
        docName.setText("Name: "+dname);
        docEmail.setText("Email: "+demail);





        docAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptPath="Doctors in Hospital "+firebaseAuth.getUid();
                DatabaseReference myRef=firebaseDatabase.getReference(acceptPath);
                HospitalAcceptedDoctors hospitalAcceptedDoctors=new HospitalAcceptedDoctors(demail,dname,dId,firebaseAuth.getUid());
                myRef.child(dId).setValue(hospitalAcceptedDoctors);


                DatabaseReference databaseReference=firebaseDatabase.getReference("Accepted Doctors");
                AcceptedDoctors acceptedDoctors=new AcceptedDoctors(demail,dname,dId,firebaseAuth.getUid());
                databaseReference.child(dId).setValue(acceptedDoctors);

                removePath="Doctor Request for "+firebaseAuth.getUid();
                DatabaseReference databaseReference1=firebaseDatabase.getReference(removePath);
                databaseReference1.child(dId).removeValue();

                Toast.makeText(HospitalDoctorRequestsListProfileDetails.this, "Accepted", Toast.LENGTH_SHORT).show();

                finish();


            }
        });


        docReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePath="Doctor Request for "+firebaseAuth.getUid();
                DatabaseReference databaseReference1=firebaseDatabase.getReference(removePath);
                databaseReference1.child(dId).removeValue();

                Toast.makeText(HospitalDoctorRequestsListProfileDetails.this, "Rejected", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }
}