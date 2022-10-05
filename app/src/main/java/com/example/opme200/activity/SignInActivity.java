package com.example.opme200.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.opme200.MainActivity;
import com.example.opme200.R;
import com.example.opme200.databinding.ActivitySignInBinding;
import com.example.opme200.model.UsersModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    private SignInClient oneTapClient;
    GoogleSignInClient mGoogleSignInClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("We are creating your account...");

        if (auth.getCurrentUser()==null){
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String email=binding.eTEmailIN.getText().toString();
                String password=binding.eTPasswordIN.getText().toString();

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        binding.tVClickSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SingUpActivity.class);
                startActivity(intent);
            }
        });


        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);


        binding.btnGoogleIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }

    int RC_SIGN_IN=65;

    private void signIn(){
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_ONE_TAP:
//                try {
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken !=  null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(TAG, "Got ID token.");
//                    }
//                } catch (ApiException e) {
//                    // ...
//                }
//                break;
//        }


        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);

                try {
                    GoogleSignInAccount account=task.getResult(ApiException.class);
                    Log.d("TAG","firebase "+ account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());

                } catch (ApiException e) {
                    e.printStackTrace();
                }

        }


    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
//                            upDateUI(user);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this, "Sign with google", Toast.LENGTH_SHORT).show();

                            UsersModel usersModel=new UsersModel();
                            usersModel.setUserId(user.getUid());
                            usersModel.setUserName(user.getDisplayName());
                            usersModel.setProfilePic(user.getPhotoUrl().toString());

                            database.getReference().child("users").child(user.getUid()).setValue(usersModel);

                        }else {
                            Snackbar.make(binding.getRoot(),"Authentication Failed",Snackbar.LENGTH_SHORT).show();
//                            upDateUI(null);
                        }
                    }
                });
    }

}