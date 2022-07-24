package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.databinding.ActivityOptionsAfterSignInBinding;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OptionsAfterSignIn extends AppCompatActivity {
    ActivityOptionsAfterSignInBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private UserDetails userDetails;
    private EditText inputJobStoreId;
    private Button enterBtn,cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOptionsAfterSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        getUserDetail();



        binding.createNewJobStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getEmail(), Toast.LENGTH_SHORT).show();
               createNewStore();

            }
        });

        binding.enterExistingJobStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  getUserDetail();
                enterJobStore();
            }
        });

    }

    private void enterJobStore() {

        //check if entered jobstore exist, if yes update its currentjobstorekey with entered one, send him to the joblist page of respective key
        dialogBuilder= new AlertDialog.Builder(this);
        final View jobStoreEnterPopupView = getLayoutInflater().inflate(R.layout.popup_enterstore_form,null);
        inputJobStoreId=(EditText) jobStoreEnterPopupView.findViewById(R.id.inputJobStoreId);
        enterBtn=(Button) jobStoreEnterPopupView.findViewById(R.id.enterBtn);
        cancelBtn=(Button) jobStoreEnterPopupView.findViewById(R.id.cancelBtn);

        dialogBuilder.setView(jobStoreEnterPopupView);
        dialog=dialogBuilder.create();
        dialog.show();

        //entering jobstore
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("user").child(auth.getUid()).child("currentJobStoreKey").setValue(inputJobStoreId.getText().toString());
                gotoJobListActivity();
            }
        });

        //on cancel button click just dismiss the dialog box
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OptionsAfterSignIn.this, "Canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private Boolean createNewStore() {
        DatabaseReference myRef = database.getReference().child("jobs").child(userDetails.getCurrentJobStoreKey()).push();
        String key = myRef.getKey();

        // check if current user already has a job store if not put this jobstore id to it mystorekey, also to its currentjobstorekey
        if(userDetails.getMyStoreKey().isEmpty()){
            userDetails.setMyStoreKey(key);
            userDetails.setCurrentJobStoreKey(key);
            database.getReference().child("user").child(auth.getUid())
                    .child("myStoreKey").setValue(key);
            database.getReference().child("user").child(auth.getUid())
                    .child("currentJobStoreKey").setValue(key);

            gotoJobListActivity();
        }
        else{
            return false;
        }
        //create an empty new jobstore node under this key

        //send him to joblist activity
        //*********************************************************************

        return true;
    }

    private void getUserDetail() {

        database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails=snapshot.getValue(UserDetails.class);
                checkUser(userDetails);
                Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   /* private void getUserDetail() {

        database.getReference().child("user").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userDetails=task.getResult().getValue(UserDetails.class);
                    checkUser(userDetails);
                    Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    private void checkUser(UserDetails ud){
        this.userDetails=ud;
        Toast.makeText(this, "Current key: "+ud.getCurrentJobStoreKey(), Toast.LENGTH_SHORT).show();
        if(!ud.getCurrentJobStoreKey().isEmpty()){
            gotoJobListActivity();
        }
    }

    private void gotoJobListActivity(){
        Intent intent= new Intent(OptionsAfterSignIn.this, JobListActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK); //we are using flag so that user cannot comeback to this activity after successful registration

        startActivity(intent);
        finish();//to close this activity
    }

}