package com.example.jobstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.jobstore.databinding.ActivitySignUpWithEmailPasswordBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct= GoogleSignIn.getLastSignedInAccount(this);
        finish();
        Intent intent;
        if(acct!=null){
            intent = new Intent(MainActivity.this, OptionsAfterSignIn.class);
        }
        else{
            intent = new Intent(MainActivity.this, ActivityGoogleSignIn.class);
        }
        startActivity(intent);*/

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(user!=null){
            Intent intent=new Intent(MainActivity.this, JobListActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent=new Intent(MainActivity.this, SignUpWithEmailPassword.class);
            startActivity(intent);
        }

    }
}