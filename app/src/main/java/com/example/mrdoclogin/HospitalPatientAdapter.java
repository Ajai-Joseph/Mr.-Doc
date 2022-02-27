package com.example.mrdoclogin;

        import android.content.Intent;
        import android.net.Uri;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
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

public class HospitalPatientAdapter extends FirebaseRecyclerAdapter<PatientsOfHospital,HospitalPatientAdapter.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    public HospitalPatientAdapter(@NonNull FirebaseRecyclerOptions<PatientsOfHospital> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull PatientsOfHospital model) {
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
        holder.HPChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ChatActivity.class );
                intent.putExtra("chatname",model.getUserName());
                intent.putExtra("recieverid",model.getUserId());

                view.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(view.getContext(),PdfActivity.class);
                intent.putExtra("patientName",model.getUserName());
                intent.putExtra("patientEmail",model.getUserEmail());
                intent.putExtra("patientId",model.getUserId());
                intent.putExtra("patientDocName",model.getDocName());
                view.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitalpatientadapter,parent,false);
        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        CircleImageView pic;
        TextView name,email;
        Button HPChat;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.tvHospitalFragmentPatientName);
            email=(TextView)itemView.findViewById(R.id.tvHospitalFragmentPatientEmail);
            pic=(CircleImageView) itemView.findViewById(R.id.ivHospitalFragmentPatientPic);
            HPChat=(Button)itemView.findViewById(R.id.btnHospitalPatientChat);
            firebaseAuth=FirebaseAuth.getInstance();




        }
    }
}
