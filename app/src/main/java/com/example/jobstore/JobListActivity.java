package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.widget.Toast;

import com.example.jobstore.Adapters.AvailableJobAdapter;
import com.example.jobstore.Modals.JobDetailModal;
import com.example.jobstore.databinding.ActivityJobListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    AvailableJobAdapter jobAdapter;
    ArrayList<JobDetailModal> jobList;
    ActivityJobListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityJobListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        jobList =new ArrayList<>();
        jobAdapter=new AvailableJobAdapter(jobList,this);
        binding.recyclerView.setAdapter(jobAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    JobDetailModal job = datasnapshot.getValue(JobDetailModal.class);
                    //if(!auth.getCurrentUser().getUid().equals(user.getUserid())){
                    jobList.add(job);
                    // }
                }
                jobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JobListActivity.this, "Error loading...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}