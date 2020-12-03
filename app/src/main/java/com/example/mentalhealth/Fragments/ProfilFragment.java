package com.example.mentalhealth.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentalhealth.AddPost_activity;
import com.example.mentalhealth.Home;
import com.example.mentalhealth.R;
import com.example.mentalhealth.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class ProfilFragment extends Fragment {
    private TextView profilName, posteCount, abonneCount, abonnementCount;
    private ImageView profilImage;
    private FirebaseUser userr;
    private Uri muri;
    private DatabaseReference myref;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String url;






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
        myref = FirebaseDatabase.getInstance().getReference("MyUsers");
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilImage.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                   choosePicture();

                                               }
                                               else

                                               {
                                                   ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                                               }
                                           }
                                       });




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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userA[0];
    }
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); //fetch files
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            choosePicture();}

        else {
            Toast.makeText(getActivity().getApplicationContext(), "Please provide permission", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null ) {
            muri= data.getData();
            Toast.makeText(getActivity().getApplicationContext(), "A Picture has been selected ", Toast.LENGTH_SHORT).show();


            profilImage.setImageURI(muri);
            final String filename = System.currentTimeMillis() + "";
            storageReference.child("Uploads").child(filename).putFile(muri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            myref.child("profilPicture").setValue(url);


                        }
                    });

        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Please select a Picture ", Toast.LENGTH_SHORT).show();

        }

    }

}