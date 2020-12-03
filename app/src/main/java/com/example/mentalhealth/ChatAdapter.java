package com.example.mentalhealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private ArrayList<ChatModel> listChat = new ArrayList<>();
    private static final int MESSAGE_TYPE_RIGHT =0;
    private static final int MESSAGE_TYPE_LEFT =1;
    private Context context;
    FirebaseUser fUser;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }


    }

    @NonNull



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message.setText(listChat.get(position).getMessage());


    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public void setListUser(ArrayList<ChatModel> listUser) {
        this.listChat = listUser;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView message;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message= itemView.findViewById(R.id.edit_message);



        }
    }

    public int getItemViewType(int position){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(listChat.get(position).getSender().equals(fUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }
        return MESSAGE_TYPE_LEFT;
    }
}
