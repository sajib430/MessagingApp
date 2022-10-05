package com.example.opme200.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.opme200.MainActivity;
import com.example.opme200.R;
import com.example.opme200.databinding.ActivitySettingsBinding;
import com.example.opme200.model.UsersModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivityResultLauncher<String> imageProfile;


    //SettingsActivity=SsA beautiful
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UsersModel usersModel = snapshot.getValue(UsersModel.class);

                Picasso.get()
                        .load(usersModel.getProfilePic())
                        .into(binding.iMprofileImageSsA);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.iMbackArrowSsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        binding.iMplusSsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProfile.launch("image/*");
            }
        });

        imageProfile = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                final StorageReference storageReference = storage.getReference().child("profilePicture")
                        .child(String.valueOf(auth.getUid()));

                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                                        .child("profileImage").setValue(uri.toString());
                            }
                        });
                    }
                });

            }
        });


    }
}