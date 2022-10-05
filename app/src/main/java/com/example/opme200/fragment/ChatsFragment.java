package com.example.opme200.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.opme200.R;
import com.example.opme200.adaptor.UsersAdaptor;
import com.example.opme200.databinding.FragmentChatsBinding;
import com.example.opme200.model.UsersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {
    FragmentChatsBinding binding;
    ArrayList<UsersModel>usersList=new ArrayList<>();
    FirebaseDatabase database;




    public ChatsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater, container,false);

        database=FirebaseDatabase.getInstance();

        UsersAdaptor adaptor=new UsersAdaptor(getContext(),usersList);
        binding.recyChats.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.recyChats.setLayoutManager(layoutManager);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    UsersModel usersModel=dataSnapshot.getValue(UsersModel.class);
                    usersModel.setUserId(dataSnapshot.getKey());

                    usersList.add(usersModel);

                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }
}