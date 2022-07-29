package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobstore.Adapters.AvailableJobAdapter;
import com.example.jobstore.Adapters.ItemClickListener;
import com.example.jobstore.Modals.JobDetailModal;
import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.databinding.ActivityJobListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class JobListActivity extends AppCompatActivity implements ItemClickListener {

    FirebaseDatabase database;
    FirebaseAuth auth;
    AvailableJobAdapter jobAdapter;
    ArrayList<JobDetailModal> jobList;
    ActivityJobListBinding binding;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText companyName,packageValue, jobType, lastDate, jobLink;
    private Button postBtn, cancelBtn;
    private UserDetails userDetails;
    private DatePickerDialog picker;


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
        jobAdapter.setClickListener(this);

        //getting user detail
        getUserDetail();


        //add a new job
        binding.addNewJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewJobPostDialog();
            }
        });


    }

    private void getJobListFromDatabase() {
        database.getReference().child("jobs").child(userDetails.getCurrentJobStoreKey()).addValueEventListener(new ValueEventListener() {
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

    public void createNewJobPostDialog(){
        dialogBuilder= new AlertDialog.Builder(this);
        final View jobPostPopupView = getLayoutInflater().inflate(R.layout.popup_newjob_entry_form,null);

        companyName=(EditText) jobPostPopupView.findViewById(R.id.inputCompanyName);
        packageValue=(EditText) jobPostPopupView.findViewById(R.id.inputPackageAmount);
        jobType=(EditText) jobPostPopupView.findViewById(R.id.inputJobType);
        lastDate=(EditText) jobPostPopupView.findViewById(R.id.inputEndDate);
        jobLink=(EditText) jobPostPopupView.findViewById(R.id.inputJobLink);

        lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();

                int day= calendar.get(Calendar.DAY_OF_MONTH);
                int month= calendar.get(Calendar.MONTH);
                int year= calendar.get(Calendar.YEAR);

                picker= new DatePickerDialog(JobListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        lastDate.setText(d+"/"+(m+1)+"/"+y);
                    }
                },year,month,day);

                picker.show();
            }

        });

        postBtn=(Button) jobPostPopupView.findViewById(R.id.postBtn);
        cancelBtn=(Button) jobPostPopupView.findViewById(R.id.cancelBtn);

        dialogBuilder.setView(jobPostPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        //adding new job to database
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

    private void getUserDetail() {

        database.getReference().child("user").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userDetails=task.getResult().getValue(UserDetails.class);

                    //get all job list from db
                    getJobListFromDatabase();


                }
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

        DatabaseReference ref = database.getReference("jobs").child(userDetails.getCurrentJobStoreKey()).push();
        JobDetailModal details=new JobDetailModal(companyName.getText().toString(),packageValue.getText().toString(),
                jobType.getText().toString(),lastDate.getText().toString(),jobLink.getText().toString(),0,0,
                auth.getUid(),ref.getKey());

        ref.setValue(details);

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
                FirebaseDatabase.getInstance().getReference("user").child(auth.getUid()).child("currentJobStoreKey").setValue("");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(JobListActivity.this,LogInWithEmailPassword.class));
                break;

            case R.id.shareJobId:

                FirebaseDatabase database= FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userDetails=snapshot.getValue(UserDetails.class);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, userDetails.getCurrentJobStoreKey());
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        //Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(JobListActivity.this, "Error fetching job id", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.shareYourJobId:
                database= FirebaseDatabase.getInstance();
                auth=FirebaseAuth.getInstance();
                database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userDetails=snapshot.getValue(UserDetails.class);

                        if(userDetails.getMyStoreKey().isEmpty()){
                            Toast.makeText(JobListActivity.this, "You don't have a Job store yet.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, userDetails.getMyStoreKey());
                            sendIntent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                        }

                        //Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(JobListActivity.this, "Error fetching your job id", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    @Override
    public void onClick(View view, int position) {
        //sharing text
        JobDetailModal job=jobList.get(position);
        String message="Company Name: "+job.getCompanyName()+"\nJob Type: "+job.getCompanyName()+"\nPackage: "+job.getPackageAmount()+
                "\nJob Link: "+job.getJobLink()+"\nLast Date to apply: "+ job.getEndDate();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}