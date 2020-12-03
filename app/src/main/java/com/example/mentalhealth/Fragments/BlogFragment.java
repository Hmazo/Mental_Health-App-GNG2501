package com.example.mentalhealth.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mentalhealth.Adapter_Post;
import com.example.mentalhealth.AddPost_activity;
import com.example.mentalhealth.MainActivity;
import com.example.mentalhealth.ModelPost;
import com.example.mentalhealth.R;
import com.example.mentalhealth.loginScreen;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //variables
    Toolbar toolbar_add_post;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    Adapter_Post adapter_post;
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    public BlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlogFragment newInstance(String param1, String param2) {
        BlogFragment fragment = new BlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_blog, container, false);
        drawerLayout=view.findViewById(R.id.drawerlayout);
        navigationView =(NavigationView)view.findViewById(R.id.nav_view);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar_add_post, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);

        //recyclerView and its properties
        recyclerView = view.findViewById(R.id.postrecycleview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        //show newest post first, for this laod from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recycleview
        recyclerView.setLayoutManager(layoutManager);

        //init post list
        postList= new ArrayList<>();
        loadPosts();


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_add_post);
        // Inflate the layout for this fragment
        ImageView add_post_btn = (ImageView) view.findViewById(R.id.add_post_btn);
        add_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Launching new Activity on hitting the image
                    Intent j = new Intent(getActivity().getApplicationContext(), AddPost_activity.class);
                    startActivity(j);
                    // End intent


            }
        });

        return view;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelPost modelPost= ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    //adapter
                    adapter_post = new Adapter_Post(getActivity(), postList);
                    // set adapter to recycleview
                    recyclerView.setAdapter(adapter_post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "erreor", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Allow opening and closing the navigator bar
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent1= new Intent(getActivity(), Fragment.class);
                startActivity(intent1);
                break;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent AppStart = new Intent(getActivity(), loginScreen.class);
                startActivity(AppStart);
                break;
        }
        return true ;
    }



}