package com.example.opme200.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.opme200.MainActivity;
import com.example.opme200.R;
import com.example.opme200.adaptor.ChatsAdaptor;
import com.example.opme200.databinding.ActivityChatsDetailBinding;
import com.example.opme200.model.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class ChatsDetailActivity extends AppCompatActivity {
    ActivityChatsDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    //ChatsDetailActivity=CDl (shortForm)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.tVusernameCahtDetail.setText(userName);

        if (profilePic != null) {
            Picasso.get().load(profilePic)
                    .into(binding.profileImageDetail);
        } else {
            binding.profileImageDetail.setImageResource(R.drawable.google);
        }

        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });


        final ArrayList<MessageModel> modelArrayList = new ArrayList<>();
        final ChatsAdaptor adaptor = new ChatsAdaptor(modelArrayList, ChatsDetailActivity.this);
        binding.recyChatsDetail.setAdapter(adaptor);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyChatsDetail.setLayoutManager(layoutManager);


        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelArrayList.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                            modelArrayList.add(messageModel);
                        }
                        adaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.iMbtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.eTvwritesms.getText().toString();
                final MessageModel messageModel = new MessageModel(senderId, message);
                messageModel.setTimestamp(new Date().getTime());
                binding.eTvwritesms.setText("");

                database.getReference().child("chats").child(senderRoom)
                        .push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats").child(receiverRoom)
                                        .push()
                                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });

            }
        });


    }
}