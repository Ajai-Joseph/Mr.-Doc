package com.example.mrdoclogin;
 import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.TextView;
 import android.widget.Toast;

        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;
 import com.squareup.picasso.Picasso;

public class PdfActivity extends AppCompatActivity {
    EditText pdfName;
    TextView patientName,patientEmail,patientDoctorName;
    Button btnUploadPdf;
    ImageView patientPic;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;

    String hName,hEmail,pId,pathname,pName,pEmail,pDoctorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        pName=getIntent().getExtras().getString("patientName");
        pEmail=getIntent().getExtras().getString("patientEmail");
         pId=getIntent().getExtras().getString("patientId");
         pDoctorName=getIntent().getExtras().getString("patientDocName");

         pathname="uploadPDF"+pId;

         patientPic=(ImageView)findViewById(R.id.ivPdfPatientProfilePic);
        pdfName=(EditText)findViewById(R.id.etPdf);
        patientName=(TextView)findViewById(R.id.tvPdfPatientName);
        patientEmail=(TextView)findViewById(R.id.tvPdfPatientEmail);
        btnUploadPdf=(Button)findViewById(R.id.btnUpload);
        patientDoctorName=(TextView)findViewById(R.id.tvPdfPatientDoctorName);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference(pathname);

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(pId).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(patientPic);

            }
        });
         patientName.setText(pName);
         patientEmail.setText(pEmail);
         patientDoctorName.setText(pDoctorName);

        btnUploadPdf.setEnabled(true);

        DatabaseReference databaseReference=firebaseDatabase.getReference("Hospitals").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalProfile hospitalProfile=dataSnapshot.getValue(HospitalProfile.class);
                hName=hospitalProfile.getUserName();
                hEmail=hospitalProfile.getUserEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PdfActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        pdfName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }


        });


    }
    private void selectPDF() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            btnUploadPdf.setEnabled(true);
            pdfName.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            btnUploadPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }
    private void uploadPDFFileFirebase(Uri data)
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("File loading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("upload"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri=uriTask.getResult();

                PutPdf putPdf=new PutPdf(pdfName.getText().toString(),uri.toString(),hName,hEmail,firebaseAuth.getUid());
                databaseReference.child(databaseReference.push().getKey()).setValue(putPdf);
                Toast.makeText(PdfActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded..."+(int)progress+ "%");
            }
        });
    }
}