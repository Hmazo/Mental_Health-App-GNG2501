package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mentalhealth.Fragments.BlogFragment;
import com.example.mentalhealth.Fragments.GameFragment;
import com.example.mentalhealth.Fragments.ProfilFragment;
import com.example.mentalhealth.Fragments.ProgretFragment;
import com.example.mentalhealth.Fragments.RechercherFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private TabLayout tableLayout;
    private ViewPager viewPager;

    private FirebaseUser userr;
    private DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tableLayout =findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);



        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new BlogFragment(),"Blog");
        viewPagerAdapter.addFragment(new GameFragment(),"Game");
        viewPagerAdapter.addFragment(new RechercherFragment(), "Search");
        viewPagerAdapter.addFragment(new ProgretFragment(),"Progres");
        viewPagerAdapter.addFragment(new ProfilFragment(),"Profil");



        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);


    }



    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragements;
        private ArrayList<String> titles;

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragements.get(position);
        }

        @Override
        public int getCount() {
            return fragements.size();
        }

         ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragements = new ArrayList<>();
            this. titles = new ArrayList<>();
        }

        public void addFragment(Fragment fragment, String title){
            this.fragements.add(fragment);
            this.titles.add(title);
        }

        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }
    }
    public User getCurrentUser(){
        final User[] userA = {new User()};
        userr = FirebaseAuth.getInstance().getCurrentUser();
        myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userr.getUid());

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userA[0] = snapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userA[0];
    }

}