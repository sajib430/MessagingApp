package com.example.opme200.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opme200.R;
import com.example.opme200.activity.ChatsDetailActivity;
import com.example.opme200.databinding.SampleUserMainBinding;
import com.example.opme200.model.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdaptor extends RecyclerView.Adapter<UsersAdaptor.viewHolder> {

    Context context;
    ArrayList<UsersModel> usersList;

    public UsersAdaptor(Context context, ArrayList<UsersModel> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_main, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UsersModel usersModel = usersList.get(position);

        holder.binding.tVuserName.setText(usersModel.getUserName());

        if (usersModel.getProfilePic() != null) {
            Picasso.get()
                    .load(usersModel.getProfilePic())
                    .into(holder.binding.profileImage);

        } else {
            holder.binding.profileImage.setImageResource(R.drawable.google);
        }

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + usersModel.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                holder.binding.tVLastMessage.setText(dataSnapshot.child("message").getValue().toString());

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatsDetailActivity.class);
                intent.putExtra("userId", usersModel.getUserId());
                intent.putExtra("profilePic", usersModel.getProfilePic());
                intent.putExtra("userName", usersModel.getUserName());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        SampleUserMainBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleUserMainBinding.bind(itemView);


        }
    }

}
