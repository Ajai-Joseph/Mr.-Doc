package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class PatientServicesDoctorHomeRequestDateTime extends AppCompatActivity {

    TextView patientDate,patientTime;
    EditText patientAddress;
    Button RequestHome;
    String address,pa,pathName,dname,dId,hosId,pName,pEmail,pDate,pTime;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    int Hour,Minute;
    DatePickerDialog.OnDateSetListener setListener;
    TextToSpeech t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services_doctor_home_request_date_time);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        patientAddress=(EditText)findViewById(R.id.etPatientAddress);
        patientDate=(TextView)findViewById(R.id.tvPatientDate);
        patientTime=(TextView) findViewById(R.id.tvPatientTime);
        RequestHome=(Button)findViewById(R.id.btnPatientHomeRequest);

        t=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    t.setLanguage(Locale.ENGLISH);
                }
            }
        });

        dname=getIntent().getExtras().getString("Docname");
        dId=getIntent().getExtras().getString("Docid");
        hosId=getIntent().getExtras().getString("Hosid");

        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);



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
                Toast.makeText(PatientServicesDoctorHomeRequestDateTime.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        patientDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(PatientServicesDoctorHomeRequestDateTime.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        patientDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });


        patientTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(PatientServicesDoctorHomeRequestDateTime.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Hour=hourOfDay;
                        Minute=minute;
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(0,0,0,Hour,Minute);
                        patientTime.setText(DateFormat.format("hh:mm aa",calendar1));
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(Hour,Minute);
                timePickerDialog.show();
            }
        });

        RequestHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address=patientAddress.getText().toString();
                pDate=patientDate.getText().toString();
                pTime=patientTime.getText().toString();

                pa="Home Requests for "+dId;
                DatabaseReference myRef=firebaseDatabase.getReference(pa);
                PatientServicesDoctorHomeRequestProfile patientServicesDoctorHomeRequestProfile=new PatientServicesDoctorHomeRequestProfile(pEmail,pName,firebaseAuth.getUid(),address,pDate,pTime);
                myRef.child(firebaseAuth.getUid()).setValue(patientServicesDoctorHomeRequestProfile);


               pathName="Patients in Hospital "+hosId;
                DatabaseReference databaseReference1=firebaseDatabase.getReference(pathName);
                PatientsOfHospital patientsOfHospital=new PatientsOfHospital(pEmail,pName,firebaseAuth.getUid(),dname);
                databaseReference1.child(firebaseAuth.getUid()).setValue(patientsOfHospital);


                t.speak("You have requested for home service on "+pDate+"at time "+pTime,TextToSpeech.QUEUE_FLUSH,null);
                Toast.makeText(PatientServicesDoctorHomeRequestDateTime.this, "Request Send", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}