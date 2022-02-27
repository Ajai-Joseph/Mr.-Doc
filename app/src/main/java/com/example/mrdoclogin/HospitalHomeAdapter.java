package com.example.mrdoclogin;

        import android.net.Uri;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.squareup.picasso.Picasso;

        import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalHomeAdapter extends FirebaseRecyclerAdapter<CompletedPatientsHospital,HospitalHomeAdapter.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    public HospitalHomeAdapter(@NonNull FirebaseRecyclerOptions<CompletedPatientsHospital> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull CompletedPatientsHospital model) {
        firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(model.getPatientId()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.get().load(uri).fit().centerCrop().into(pic);
                Glide.with(holder.pic.getContext()).load(uri).into(holder.pic);
            }
        });
        holder.name.setText(model.getPatientName());
        holder.email.setText(model.getPatientEmail());
        holder.docName.setText(model.getDoctorName());

    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitalhomeadapter,parent,false);
        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        CircleImageView pic;
        TextView name,email,docName;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.tvHospitalFragmentHomePatientName);
            email=(TextView)itemView.findViewById(R.id.tvHospitalFragmentHomePatientEmail);
            pic=(CircleImageView)itemView.findViewById(R.id.ivHospitalFragmentHomePatientPic);
            docName=(TextView)itemView.findViewById(R.id.tvHospitalFragmentHomeDoctorName);

            firebaseAuth= FirebaseAuth.getInstance();
            firebaseDatabase= FirebaseDatabase.getInstance();
            firebaseStorage= FirebaseStorage.getInstance();


            DatabaseReference databaseReference=firebaseDatabase.getReference("Doctors").child(firebaseAuth.getUid());



        }
    }
}
