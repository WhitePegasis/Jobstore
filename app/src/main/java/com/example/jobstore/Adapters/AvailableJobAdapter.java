package com.example.jobstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
    private ItemClickListener clickListener;

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
        String cName,jType,pAmount,jLink,eDate;

        cName=job.getCompanyName();
        jType=job.getJobType();
        pAmount=job.getPackageAmount();
        jLink=job.getJobLink();
        eDate=job.getEndDate();
        holder.companyName.setText(cName);

        String jobTypeString = "<b>" + "Job Type: "+"</b> " + jType;
        holder.jobType.setText(Html.fromHtml(jobTypeString));

        String packageAmountString = "<b>" + "Package: "+"</b> " +pAmount;
        holder.packageAmount.setText(Html.fromHtml(packageAmountString));

        String jobLinkString = "<b>" + "Job Apply Link: "+"</b> " +jLink;
        holder.jobLink.setText(Html.fromHtml(jobLinkString));

        String endDateString = "<b>" + "Last Date to Apply: "+"</b> " +eDate ;
        holder.endDate.setText(Html.fromHtml(endDateString));

        holder.deletePost.setContentDescription(job.getJobId());

        //setting onlick listener in delete button
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

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView companyName,packageAmount,jobType,endDate,jobLink,postedBy;
        ImageView upVote,downVote, deletePost, shareImg;
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
            shareImg=itemView.findViewById(R.id.shareImg);
            shareImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
