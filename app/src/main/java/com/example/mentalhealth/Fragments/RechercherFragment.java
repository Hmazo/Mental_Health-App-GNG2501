package com.example.mentalhealth.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mentalhealth.R;
import com.example.mentalhealth.SearchAdapter;
import com.example.mentalhealth.User;
import com.example.mentalhealth.VerticalSpaceItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RechercherFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<User> usersList;


    public RechercherFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_rechercher, container, false);

        recyclerView = view.findViewById(R.id.userSearchRecycler);


        usersList = new ArrayList<>();
        getUsers();


        return view;
    }

    public void getUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("MyUsers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if(!firebaseUser.getUid().equals(user.getUserID())) {
                                usersList.add(user);
                            }

                            Toast.makeText(getContext(), "size : "+usersList.size(), Toast.LENGTH_LONG).show();
                            SearchAdapter searchAdapter = new SearchAdapter(getContext());
                            searchAdapter.setListUser(usersList);
                            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(0);

                            recyclerView.setAdapter(searchAdapter);
                            recyclerView.addItemDecoration(verticalSpaceItemDecoration);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}