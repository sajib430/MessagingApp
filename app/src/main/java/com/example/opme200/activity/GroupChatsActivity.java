package com.example.opme200.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.opme200.MainActivity;
import com.example.opme200.adaptor.ChatsAdaptor;
import com.example.opme200.databinding.ActivityGroupChatsBinding;
import com.example.opme200.model.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatsActivity extends AppCompatActivity {
    ActivityGroupChatsBinding binding;


    //GroupChatsActivity=GCsA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().hide();
        binding.imageViewBackGCsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupChatsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModel>messageModelArrayList=new ArrayList<>();

        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.tVusernameGCsA.setText("Friends Chats");


        final ChatsAdaptor adaptor=new ChatsAdaptor(messageModelArrayList,this);
        binding.recyChatsGCsA.setAdapter(adaptor);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recyChatsGCsA.setLayoutManager(layoutManager);


        database.getReference().child("groupChats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel messageModels=dataSnapshot.getValue(MessageModel.class);
                    messageModelArrayList.add(messageModels);
                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.iMbtnSendGCsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=binding.eTvwritesmsGCsA.getText().toString();
                final MessageModel messageModel=new MessageModel(senderId,message);
                messageModel.setTimestamp(new Date().getTime());
                binding.eTvwritesmsGCsA.setText("");

                database.getReference().child("groupChats")
                        .push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

            }
        });



    }
}