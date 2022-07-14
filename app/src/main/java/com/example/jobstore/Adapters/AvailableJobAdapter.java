package com.example.jobstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobstore.Modals.JobDetailModal;
import com.example.jobstore.R;

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
        JobDetailModal user= arraylist.get(position);
        holder.companyName.setText(user.getCompanyName());
        holder.jobType.setText(user.getJobType());
        holder.packageAmount.setText(user.getPackageAmount());
        holder.jobLink.setText(user.getJobLink());
        holder.endDate.setText(user.getEndDate());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView companyName,packageAmount,jobType,endDate,jobLink,postedBy;
        ImageView upVote,downVote;
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
        }
    }
}
