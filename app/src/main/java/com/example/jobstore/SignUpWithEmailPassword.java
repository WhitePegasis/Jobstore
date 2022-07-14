package com.example.jobstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jobstore.Modals.UserDetails;
import com.example.jobstore.databinding.ActivityOptionsAfterSignInBinding;
import com.example.jobstore.databinding.ActivitySignUpWithEmailPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpWithEmailPassword extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivitySignUpWithEmailPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySignUpWithEmailPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //on sign up button click
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking is username is not empty
                if(binding.inputUsername.getText().toString().isEmpty()){
                    binding.inputUsername.setError("Username can't be empty");
                }
                else if(binding.inputPassword.getText().toString().isEmpty()){
                    binding.inputPassword.setError("Enter a password");
                }
                //checking if password and confirm password value is same
                else if(binding.inputPassword.getText().toString() != binding.inputCnfPassword.getText().toString()){
                    binding.inputPassword.setError("Password mismatch");
                }
                else{

                    // try to register the user
                    registerUser(binding.inputEmail.getText().toString(),
                            binding.inputPassword.getText().toString());
                }
            }
        });


        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUpWithEmailPassword.this, LogInWithEmailPassword.class);
                startActivity(intent);
            }
        });

    }

    void registerUser(String email, String password){
        auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpWithEmailPassword.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                //    Toast.makeText(SignUpWithEmailPassword.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user= auth.getCurrentUser();

                                    try {
                                        //add user detais to firebase realtime database
                                        addUserToDatabase(user);
                                    } catch (Exception e) {
                                        Toast.makeText(SignUpWithEmailPassword.this, "Error", Toast.LENGTH_SHORT).show();
                                    }


                                    Intent intent= new Intent(SignUpWithEmailPassword.this, OptionsAfterSignIn.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK); //we are using flag so that user cannot comeback to this activity after successful registration

                                    startActivity(intent);
                                    finish();//to close register activity
                                }
                                else{
                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e) {
                                        binding.inputEmail.setError("Too weak");
                                    }catch(FirebaseAuthUserCollisionException e){
                                        binding.inputEmail.setError("User is already registered with this email");
                                    }catch(Exception e){
                                        Toast.makeText(SignUpWithEmailPassword.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
    }

    void addUserToDatabase(FirebaseUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("user");
        UserDetails details=new UserDetails(user.getDisplayName(), user.getEmail());
        Toast.makeText(SignUpWithEmailPassword.this, "Name: "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
        ref.child(auth.getUid()).setValue(details);
    }
}