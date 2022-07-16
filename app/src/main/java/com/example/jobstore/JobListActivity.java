package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobstore.Adapters.AvailableJobAdapter;
import com.example.jobstore.Modals.JobDetailModal;
import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.databinding.ActivityJobListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    AvailableJobAdapter jobAdapter;
    ArrayList<JobDetailModal> jobList;
    ActivityJobListBinding binding;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText companyName,packageValue, jobType, lastDate, jobLink;
    private Button postBtn, cancelBtn;
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

        database.getReference().child("jobs").addValueEventListener(new ValueEventListener() {
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


        binding.addNewJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewJobPostDialog();
            }
        });



    }

    public void createNewJobPostDialog(){
        dialogBuilder= new AlertDialog.Builder(this);
        final View jobPostPopupView = getLayoutInflater().inflate(R.layout.popup_newjob_entry_form,null);

        companyName=(EditText) jobPostPopupView.findViewById(R.id.inputCompanyName);
        packageValue=(EditText) jobPostPopupView.findViewById(R.id.inputPackageAmount);
        jobType=(EditText) jobPostPopupView.findViewById(R.id.inputJobType);
        lastDate=(EditText) jobPostPopupView.findViewById(R.id.inputEndDate);
        jobLink=(EditText) jobPostPopupView.findViewById(R.id.inputJobLink);

        postBtn=(Button) jobPostPopupView.findViewById(R.id.postBtn);
        cancelBtn=(Button) jobPostPopupView.findViewById(R.id.cancelBtn);

        dialogBuilder.setView(jobPostPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAllFieldsAreFilledProperly()){
                    addNewJobToDatabase();
                    Toast.makeText(JobListActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        //on cancel button click just dismiss the dialog box
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(JobListActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private boolean checkAllFieldsAreFilledProperly() {

        if(companyName.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter company name", Toast.LENGTH_SHORT).show();
        }
        else if(packageValue.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter package amount", Toast.LENGTH_SHORT).show();
        }

        else if(jobType.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter job type", Toast.LENGTH_SHORT).show();
        }

        else if(lastDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter end date to apply", Toast.LENGTH_SHORT).show();
        }

        else if(jobLink.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Job apply link", Toast.LENGTH_SHORT).show();
        }
        else{
            return true;
        }

        return false;
    }

    //pushing new job details to firebase
    private void addNewJobToDatabase() {

        DatabaseReference ref = database.getReference("jobs");
        JobDetailModal details=new JobDetailModal(companyName.getText().toString(),packageValue.getText().toString(),
                jobType.getText().toString(),lastDate.getText().toString(),jobLink.getText().toString(),0,0,
                auth.getUid());

        ref.push().setValue(details);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(JobListActivity.this,LogInWithEmailPassword.class));
                break;

            case R.id.settings:
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }
}