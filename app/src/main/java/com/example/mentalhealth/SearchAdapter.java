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

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    private ArrayList<User> listUser = new ArrayList<>();

    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recherche_item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @NonNull



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameSearchResult.setText(listUser.get(position).getName());
        holder.imgSearchResult.setImageResource(R.drawable.account_icon);
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, VisitProfil.class);
                i.putExtra("userID", listUser.get(position).getUserID());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public void setListUser(ArrayList<User> listUser) {
        this.listUser = listUser;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameSearchResult;
        private ImageView imgSearchResult;
        private ConstraintLayout click;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameSearchResult= itemView.findViewById(R.id.searchResultText);
            imgSearchResult = itemView.findViewById(R.id.searchResultPic);
            click = itemView.findViewById(R.id.searchParent);


        }
    }
}
