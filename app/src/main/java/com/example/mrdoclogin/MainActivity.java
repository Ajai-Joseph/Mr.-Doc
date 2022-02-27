package com.example.mrdoclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{ //implements AdapterView.OnItemSelectedListener {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;
    private Spinner spinner;
    private FirebaseDatabase firebaseDatabase;
    String choice,item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegistration = (TextView) findViewById(R.id.tvRegister);
        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        //spinner = (Spinner) findViewById(R.id.spinner1);

       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usertype, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Name.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                } else {

                    validate(Name.getText().toString().trim(), Password.getText().toString().trim());
                }

            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
    }


    private void validate(String username, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(username, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                      DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
                      databaseReference.addValueEventListener(new ValueEventListener() {
                      @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                         PatientProfile patientProfile = datasnapshot.getValue(PatientProfile.class);
                         HospitalProfile hospitalProfile=datasnapshot.getValue(HospitalProfile.class);
                         DoctorProfile doctorProfile=datasnapshot.getValue(DoctorProfile.class);

                        item = patientProfile.getUserType();
                        item=hospitalProfile.getUserType();
                        item=doctorProfile.getUserType();
                    switch (item) {
                        case "Patient":
                            Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, PatientActivity.class));
                            finish();
                            break;
                        case "Hospital":
                            Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, HospitalActivity.class));
                            finish();
                            break;

                        case "Doctor":
                            Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, DoctorActivity.class));
                            finish();
                            break;
                    }


                     }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {
                          Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                            }
                    });


                } else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   // @Override
   // public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    //    choice=adapterView.getItemAtPosition(position).toString();
   // }

   // @Override
    //public void onNothingSelected(AdapterView<?> adapterView) {

    //}
}



