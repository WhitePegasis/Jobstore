package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jobstore.databinding.ActivityLogInWithEmailPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LogInWithEmailPassword extends AppCompatActivity {
    ActivityLogInWithEmailPasswordBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogInWithEmailPasswordBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();

        binding.registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LogInWithEmailPassword.this, SignUpWithEmailPassword.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.inputUsername.getText().toString();
                String password=binding.inputPassword.getText().toString();

                //check if email is correct, or empty
                //.....
                //checking if username is not empty
                if(binding.inputUsername.getText().toString().isEmpty()){
                    binding.inputUsername.setError("Username can't be empty");
                }
                else if(binding.inputPassword.getText().toString().isEmpty()){
                    binding.inputPassword.setError("Enter your password");
                }
                else{
                    loginUser(email,password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent= new Intent(LogInWithEmailPassword.this, OptionsAfterSignIn.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK); //we are using flag so that user cannot comeback to this activity after successful registration
                    Toast.makeText(LogInWithEmailPassword.this,"Successful",Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                    finish();//to close register activity
                }
                else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        binding.inputUsername.setError("Wrong username");
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        binding.inputPassword.setError("Wrong Password");
                    }catch(Exception e){
                        Toast.makeText(LogInWithEmailPassword.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}