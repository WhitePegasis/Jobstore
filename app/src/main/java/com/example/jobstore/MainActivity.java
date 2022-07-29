package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.databinding.ActivitySignUpWithEmailPasswordBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private UserDetails userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Opening jobStore ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();//displays the progress bar

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        Intent intent;
        if(user!=null){
            getUserDetail();
        }
        else{

            intent = new Intent(MainActivity.this, SignUpWithEmailPassword.class);
            startActivity(intent);
        }


    }

    private void getUserDetail() {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails=snapshot.getValue(UserDetails.class);
                checkUser(userDetails);
                //Toast.makeText(OptionsAfterSignIn.this, "got user details: "+userDetails.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkUser(UserDetails ud){
        this.userDetails=ud;
        //Toast.makeText(this, "Current key: "+ud.getCurrentJobStoreKey(), Toast.LENGTH_SHORT).show();
        if(!ud.getCurrentJobStoreKey().isEmpty()){
            gotoJobListActivity();
        }
        else{
            Toast.makeText(this, "user previous detail is empty ", Toast.LENGTH_SHORT).show();
            //if user has not entered jobStore previously send him to OptionsAfterSignIn activity
            Intent intent = new Intent(MainActivity.this, OptionsAfterSignIn.class);
            startActivity(intent);
        }
    }

    private void gotoJobListActivity(){
        Intent intent= new Intent(MainActivity.this, JobListActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK); //we are using flag so that user cannot comeback to this activity after successful registration

        startActivity(intent);
        finish();//to close this activity
    }
}