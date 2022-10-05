package com.example.opme200.adaptor;

import android.content.Context;
import android.media.effect.EffectFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opme200.R;
import com.example.opme200.databinding.ReceiverSampleBinding;
import com.example.opme200.databinding.SenderSampleBinding;
import com.example.opme200.model.MessageModel;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatsAdaptor extends RecyclerView.Adapter{

    ArrayList<MessageModel>messageList;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatsAdaptor(ArrayList<MessageModel> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_sample,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.receiver_sample,parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (messageList.get(position).getSmsUId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=messageList.get(position);

        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).binding.tVsendSms.setText(messageModel.getMessage());

        }else {
            ((ReceiverViewHolder)holder).binding.tVsmsReceive.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        ReceiverSampleBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReceiverSampleBinding.bind(itemView);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        SenderSampleBinding binding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SenderSampleBinding.bind(itemView);
        }
    }

}
