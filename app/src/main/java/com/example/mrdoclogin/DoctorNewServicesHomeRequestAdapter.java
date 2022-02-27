package com.example.mrdoclogin;
 import android.content.Context;
        import android.content.Intent;
        import android.graphics.ColorSpace;
 import android.net.Uri;
 import android.speech.tts.TextToSpeech;
 import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

 import com.bumptech.glide.Glide;
 import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;

 import java.util.Locale;

 import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorNewServicesHomeRequestAdapter extends FirebaseRecyclerAdapter<PatientServicesDoctorHomeRequestProfile,DoctorNewServicesHomeRequestAdapter.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    public DoctorNewServicesHomeRequestAdapter(@NonNull FirebaseRecyclerOptions<PatientServicesDoctorHomeRequestProfile> options) {

        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull final PatientServicesDoctorHomeRequestProfile model) {

        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(model.getUserId()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.get().load(uri).fit().centerCrop().into(pic);
                Glide.with(holder.pic.getContext()).load(uri).into(holder.pic);
            }
        });
        holder.name.setText(model.getUserName());

        holder.email.setText(model.getUserEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Intent intent = new Intent(view.getContext(),DoctorNewServicesHomeRequestDetails.class );
                intent.putExtra("PatientName",model.getUserName());
                intent.putExtra("PatientEmail",model.getUserEmail());
                intent.putExtra("PatientAddress",model.getUserAddress());
                intent.putExtra("PatientDate",model.getUserDate());
                intent.putExtra("PatientTime",model.getUserTime());
                intent.putExtra("PatientId",model.getUserId());

                view.getContext().startActivity(intent);
            }
        });








    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorrequesthome,parent,false);
        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        CircleImageView pic;
        TextView name,email;




        public myViewholder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.tvDoctorRequestName);
            email=(TextView)itemView.findViewById(R.id.tvDoctorRequestEmail);
            pic=(CircleImageView)itemView.findViewById(R.id.ivDoctorNewServicesPatientListPic);







        }
    }



}
