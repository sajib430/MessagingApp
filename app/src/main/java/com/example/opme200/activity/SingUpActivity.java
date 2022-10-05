package com.example.opme200.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.opme200.databinding.ActivitySingUpBinding;
import com.example.opme200.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SingUpActivity extends AppCompatActivity {
    ActivitySingUpBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("We are creating your account...");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String email=binding.eTEmail.getText().toString();
                String password=binding.eTPassword.getText().toString();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        String name=binding.eTName.getText().toString();
                        if (task.isSuccessful()){

                            UsersModel users=new UsersModel(name,email,password);

                            String id=task.getResult().getUser().getUid();

                            database.getReference().child("users").child(id).setValue(users);


                            Toast.makeText(SingUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SingUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        binding.tVSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SingUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });




    }
}