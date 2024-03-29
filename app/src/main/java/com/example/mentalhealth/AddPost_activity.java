package com.example.mentalhealth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mentalhealth.Fragments.BlogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddPost_activity extends AppCompatActivity {
    Toolbar actionBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;


    //permissions constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE=300;
    private static final int IMAGE_PICK_GALLERY_CODE=400;
    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    EditText titleEt, descriptionEt;
    ImageView imageIv;
    Button uploadBtn;

    //user info
    String name, email, uid, dp;

    // image picked will be same in the Uri
    Uri image_rui= null ;

    //progress bar
    ProgressDialog  pd;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_addpost);


        //actionBar.setTitle("Add new post");
        //enable back button in actionbar
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        //init permisiions arrays
        cameraPermissions= new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();
        checkUserStatus();

        //actionBar.setSubtitle(email);

        //get some info of current user to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("MyUsers");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    name = ""+ ds.child("name").getValue();
                    email = ""+ ds.child("email").getValue();
                    dp = ""+ ds.child("image").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //init views
        titleEt=findViewById(R.id.pTitleEt);
        descriptionEt=findViewById(R.id.pDecriptionEt);
        imageIv=findViewById(R.id.pImageIv);
        uploadBtn=findViewById(R.id.pUploadBtn);

        //get image from camera gallery on click
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImagePickDialog();

            }
        });

        //upload Button click listner
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data
                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(AddPost_activity.this, "Enter title ..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(description)){
                    Toast.makeText(AddPost_activity.this, "Enter description ..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_rui==null) {
                    //post with no image
                    uploadData(title, description, "noImage");
                }
                else {
                    //post with image
                    uploadData(title, description, String.valueOf(image_rui));

                }

            }
        });


    }
    private void uploadData(String title, String description, String uri) {
        pd.setMessage("Publishing post");
        pd.show();

        //for post-image, post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;
        if(!uri.equals("noImage")) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is ulpoaded to firebase storage, now we get its url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri= uriTask.getResult().toString();
                            if(uriTask.isSuccessful()) {
                                //url is received upload post to firebase database
                                HashMap<Object,String> hashmap = new HashMap<>();
                                //put post info
                                hashmap.put("uid", uid);
                                hashmap.put("uName",name);
                                hashmap.put("uEmail",email);
                                hashmap.put("uDp",dp);
                                hashmap.put("pId",timeStamp);
                                hashmap.put("pTitle",title);
                                hashmap.put("pDescription", description);
                                hashmap.put("pImage",downloadUri);
                                hashmap.put("pTime",timeStamp);

                                //path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this ref
                                ref.child(timeStamp).setValue(hashmap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added post in databse
                                                pd.dismiss();
                                                Toast.makeText(AddPost_activity.this, "Post published", Toast.LENGTH_SHORT).show();
                                                //reset views
                                                titleEt.setText("");
                                                descriptionEt.setText("");
                                                imageIv.setImageURI(null);
                                                image_rui= null;
                                                Intent intent2 = new Intent(AddPost_activity.this, BlogFragment.class);
                                                startActivity(intent2);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in databse
                                                pd.dismiss();
                                                Toast.makeText(AddPost_activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            pd.dismiss();
                            Toast.makeText(AddPost_activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            //post without image
            //url is received upload post to firebase database
            HashMap<Object,String> hashmap = new HashMap<>();
            //put post info
            hashmap.put("uid", uid);
            hashmap.put("uName",name);
            hashmap.put("uEmail",email);
            hashmap.put("uDp",dp);
            hashmap.put("pId",timeStamp);
            hashmap.put("pTitle",title);
            hashmap.put("pDescription", description);
            hashmap.put("pImage","noImage");
            hashmap.put("pTime",timeStamp);

            //path to store post data
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in this ref
            ref.child(timeStamp).setValue(hashmap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added post in databse
                            pd.dismiss();
                            Toast.makeText(AddPost_activity.this, "Post published", Toast.LENGTH_SHORT).show();
                            titleEt.setText("");
                            descriptionEt.setText("");
                            imageIv.setImageURI(null);
                            image_rui= null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed adding post in databse
                            pd.dismiss();
                            Toast.makeText(AddPost_activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }

    }
    private void showImagePickDialog() {
        //options(camera,gallery) to show dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    //camera clicked
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }
                if(which==1) {
                    //gallery clicked
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }

                }

            }
        });

        //crate and show dialog
        builder.create().show();
    }

    private void pickFromCamera() {
        //intent oto pick imag from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
    startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }


    private void pickFromGallery() {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        //return true if enabled false if not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        //return true if enabled false if not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result;
    }

    private void requestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);
    }



    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            email= user.getEmail();
            uid = user.getUid();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //this method is called when user press Allow or Deny from permission request dialog
        //we handle here permission cases (allowed denied)
        switch( requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera and Storage both permissions are necessary", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }

            }
        break;
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted) {
                        pickFromGallery();
                    }
                    else {
                        //permission denied
                        Toast.makeText(this, "Storage permissions is necessary", Toast.LENGTH_SHORT).show();

                    }
                }
                else {


                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this method will be called after picking image from camera or gallery
        if(resultCode== RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery , get uri of image
                image_rui = data.getData();
                //set to imageview
                imageIv.setImageURI(image_rui);
            }
            else if (requestCode== IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri from image
                imageIv.setImageURI(image_rui);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

