package com.example.mentalhealth;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Adapter_Post extends RecyclerView.Adapter<Adapter_Post.Myholder> {

    Context context;
    List<ModelPost> postList;

    public Adapter_Post(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout post_card.xml
        View view= LayoutInflater.from(context).inflate(R.layout.post_card, parent, false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int i) {
        //get data
        String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName= postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        String pId = postList.get(i).getpId();
        String pTitle = postList.get(i).getpTitle();
        String pDescription= postList.get(i).getpDescr();
        String pImage = postList.get(i).getpImage();
        String pTimeSTamp = postList.get(i).getpTime();

        //convert timeSTamp to dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeSTamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
         //set data
        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(pTime);
        holder.pTitleTv.setText(pTitle);
        holder.pDescriptionTv.setText(pDescription);

        //set user dp
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.account_icon).into(holder.uPictureIv);
        }
        catch (Exception e) {

        }
        //set post image
        if(pImage.equals("noImage")){
            //hide imageView
            holder.pImageIv.setVisibility(View.GONE);

        }
        else {


            try {
                Picasso.get().load(pImage).into(holder.pImageIv);
            } catch (Exception e) {

            }
        }
        //handle button clicks
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement late
                Toast.makeText(context, "More",Toast.LENGTH_SHORT).show();
            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement late
                Toast.makeText(context, "Like",Toast.LENGTH_SHORT).show();
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement late
                Toast.makeText(context, "comment",Toast.LENGTH_SHORT).show();
            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement late
                Toast.makeText(context, "share",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ModelPost> getPostList() {
        return postList;
    }

    public void setPostList(List<ModelPost> postList) {
        this.postList = postList;
    }

    //View holder class
    class Myholder extends RecyclerView.ViewHolder {

        //views from post_card.xml
        ImageView uPictureIv, pImageIv;
        TextView uNameTv,pTimeTv, pTitleTv, pDescriptionTv, pLikeTv;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //init view
            uPictureIv= itemView.findViewById(R.id.uPictureIv);
            pImageIv= itemView.findViewById(R.id.pImageTv);
            uNameTv= itemView.findViewById(R.id.uNameTv);
            pTimeTv= itemView.findViewById(R.id.pTimeTv);
            pTitleTv= itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv= itemView.findViewById(R.id.pDescriptionTv);
            pLikeTv= itemView.findViewById(R.id.pLikesTv);
            moreBtn= itemView.findViewById(R.id.moreBtn);
            likeBtn= itemView.findViewById(R.id.likeBtn);
            commentBtn= itemView.findViewById(R.id.commentBtn);
            shareBtn= itemView.findViewById(R.id.shareBtn);


        }
    }
}
