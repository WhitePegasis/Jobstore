package com.example.jobstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobstore.Modals.JobDetailModal;
import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AvailableJobAdapter extends RecyclerView.Adapter<AvailableJobAdapter.viewholder>{

    ArrayList<JobDetailModal> arraylist;
    Context context;

    public AvailableJobAdapter(ArrayList<JobDetailModal> array, Context context) {
        this.arraylist = array;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.available_job_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        JobDetailModal job= arraylist.get(position);
        holder.companyName.setText(job.getCompanyName());
        holder.jobType.setText(job.getJobType());
        holder.packageAmount.setText(job.getPackageAmount());
        holder.jobLink.setText(job.getJobLink());
        holder.endDate.setText(job.getEndDate());
        holder.deletePost.setContentDescription(job.getJobId());

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    UserDetails userDetails=task.getResult().getValue(UserDetails.class);

                    //if current user is admin unable delete button
                    if(userDetails.getCurrentJobStoreKey().equals(userDetails.getMyStoreKey())){
                        holder.deletePost.setVisibility(View.VISIBLE);
                    }

                    //delete selected post (note: only for admin)
                    holder.deletePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("jobs").child(userDetails.getCurrentJobStoreKey())
                            .child(holder.deletePost.getContentDescription().toString()).removeValue();
                            Toast.makeText(context, "Deleted"+holder.deletePost.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView companyName,packageAmount,jobType,endDate,jobLink,postedBy;
        ImageView upVote,downVote, deletePost;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            companyName=itemView.findViewById(R.id.companyNameTv);
            packageAmount=itemView.findViewById(R.id.packageAmountTv);
            jobType=itemView.findViewById(R.id.jobTypeTv);
            endDate=itemView.findViewById(R.id.endDateTv);
            jobLink=itemView.findViewById(R.id.jobLinkTv);
            postedBy=itemView.findViewById(R.id.postedByTv);
            upVote=itemView.findViewById(R.id.upVoteImg);
            downVote=itemView.findViewById(R.id.downVoteImg);
            deletePost=itemView.findViewById(R.id.deletePostImg);
        }
    }
}
