
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

public class PatientServicesDoctorListProfileDetails extends AppCompatActivity {


    TextView docName,docEmail;
    ImageView docPic;
    Button docRequestHome;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    String pName,pEmail,dname,demail,dId,hosId,dchat,pa,comPatientPath,comDoctorPath,comHospitalPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_doctor_list_profile_details);

        docPic=(ImageView)findViewById(R.id.ivPatientDoctorProfilePic);
        docName=(TextView)findViewById(R.id.tvPatientDoctorProfileName);
        docEmail=(TextView)findViewById(R.id.tvPatientDoctorProfileEmail);

        docRequestHome=(Button)findViewById(R.id.btnPatientDoctorRequestHome);

        dname=getIntent().getExtras().getString("docname");
        demail=getIntent().getExtras().getString("docemail");
        dId=getIntent().getExtras().getString("docid");
        hosId=getIntent().getExtras().getString("hosid");


        docName.setText("Name: "+dname);
        docEmail.setText("Email: "+demail);



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(dId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(docPic);

            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference("Patients").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientProfile patientProfile=dataSnapshot.getValue(PatientProfile.class);
                pName=patientProfile.getUserName();
                pEmail=patientProfile.getUserEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PatientServicesDoctorListProfileDetails.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });


        docRequestHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(PatientServicesDoctorListProfileDetails.this,PatientServicesDoctorHomeRequestDateTime.class);
                intent.putExtra("Docid",dId);
                intent.putExtra("Hosid",hosId);
                intent.putExtra("Docname",dname);
                startActivity(intent);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patientsevicesdoctorlistprofile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.patientDoctorChat:
                dchat="Message for "+dId;
                DatabaseReference myRef=firebaseDatabase.getReference(dchat);
                PatientServicesDoctorChat patientServicesDoctorChat=new PatientServicesDoctorChat(pEmail,pName,firebaseAuth.getUid());
                myRef.child(firebaseAuth.getUid()).setValue(patientServicesDoctorChat);

                Intent intent = new Intent(PatientServicesDoctorListProfileDetails.this, ChatActivity.class);
                intent.putExtra("chatname",dname);
                intent.putExtra("recieverid",dId);
                startActivity(intent);
                break;
            case R.id.patientCompleted:

                comPatientPath="Completed Services of "+firebaseAuth.getUid();
                DatabaseReference databaseReference2=firebaseDatabase.getReference(comPatientPath);
                CompletedPatients completedPatients=new CompletedPatients(dname,demail,dId);
                databaseReference2.child(dId).setValue(completedPatients);

                comHospitalPath="Completed Services of "+hosId;
                DatabaseReference databaseReference3=firebaseDatabase.getReference(comHospitalPath);
                CompletedPatientsHospital completedPatientsHospital=new CompletedPatientsHospital(pName,pEmail,dname,firebaseAuth.getUid());
                databaseReference3.child(firebaseAuth.getUid()).setValue(completedPatientsHospital);

                comDoctorPath="Completed Services of "+dId;
                DatabaseReference databaseReference4=firebaseDatabase.getReference(comDoctorPath);
                CompletedPatientsDoctor completedPatientsDoctor=new CompletedPatientsDoctor(pName,pEmail,firebaseAuth.getUid());
                databaseReference4.child(firebaseAuth.getUid()).setValue(completedPatientsDoctor);

                Toast.makeText(PatientServicesDoctorListProfileDetails.this, "Services Completed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PatientServicesDoctorListProfileDetails.this,PatientActivity.class));

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}