package com.example.mentalhealth.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentalhealth.Home;
import com.example.mentalhealth.R;
import com.example.mentalhealth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfilFragment extends Fragment {
    private TextView profilName, posteCount, abonneCount, abonnementCount;
    private ImageView profilImage;
    private FirebaseUser userr;
    private DatabaseReference myref;





    public ProfilFragment() {
        // Required empty public constructor
        ;
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        profilName = view.findViewById(R.id.ProfilFullName);
        posteCount = view.findViewById(R.id.PorfilPosteCount);
        abonneCount = view.findViewById(R.id.PorfilAbonneCount);
        abonnementCount = view.findViewById(R.id.PorfilAbonnementsCount);
        profilImage = view.findViewById(R.id.ProfilPicture1);



        getCurrentUser();






        return view;
    }

    public User getCurrentUser(){
        final User[] userA = {new User()};
        userr = FirebaseAuth.getInstance().getCurrentUser();
        myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userr.getUid());

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userA[0] = snapshot.getValue(User.class);
                profilName.setText(userA[0].getName());

                posteCount.setText(String.valueOf(userA[0].getPubNumber()));
                abonneCount.setText(String.valueOf(userA[0].getAbonne()));
                abonnementCount.setText(String.valueOf(userA[0].getAbonnements()));
                profilImage.setImageResource(R.drawable.account_icon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userA[0];
    }
}