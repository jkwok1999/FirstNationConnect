package com.example.firstnationconnect;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.UUID;

public class NewImageReplyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tvReplyNewContent;
    private TextView tvReplyPostName;
    private ImageView ivNewReplyImage;
    private Button btSubmitImageReply;
    private Uri mainImageURI = null;

    private String stringID;
    private String topic;
    private String mainPostName;
    private String mainPostID;
    private String reply;

    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;
    private String imageFileName;

    private String TAG = "NewPostActivity";

    private FusedLocationProviderClient fusedLocationClient;
    private static final int IMAGE_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image_reply);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
            mainPostName = intent.getStringExtra("PostName");
            mainPostID = intent.getStringExtra("PostID");
            reply = intent.getStringExtra("Reply");
        }

        tvReplyNewContent = findViewById(R.id.tvNewReplyContent);

        if (reply != null) {
            tvReplyNewContent.setText(reply);
        }

        tvReplyPostName = findViewById(R.id.tvImageReplyPostName);
        tvReplyPostName.setText("Replying to '" + mainPostName + "'");

        ivNewReplyImage = findViewById(R.id.ivNewReplyImage);
        ivNewReplyImage.setOnClickListener(this);
        btSubmitImageReply = findViewById(R.id.btSubmitImageReply);
        btSubmitImageReply.setOnClickListener(this);

        firestoreDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        UUID newID = UUID.randomUUID();
        stringID = newID.toString();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case (R.id.ivNewReplyImage):

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission
                            (NewImageReplyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(NewImageReplyActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewImageReplyActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
                    } else {
                        /*CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(2, 1)
                                .start(NewImageReplyActivity.this);*/
                        /*Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, IMAGE_PICK_CODE);*/

                        mGetContent.launch("image/*");

                    }
                } else {
                    /*CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(2, 1)
                            .start(NewImageReplyActivity.this);*/
                    mGetContent.launch("image/*");
                }

                break;

            case (R.id.btSubmitImageReply):

                if (tvReplyNewContent.getText().toString().trim().isEmpty()) {
                    Toast.makeText(NewImageReplyActivity.this, "Please ensure post is not empty",
                            Toast.LENGTH_SHORT).show();
                } else if (mainImageURI == null) {
                    Toast.makeText(NewImageReplyActivity.this, "Please ensure you have uploaded a picture",
                            Toast.LENGTH_SHORT).show();
                } else {

                    String replyContent = tvReplyNewContent.getText().toString();

                    long milliseconds = System.currentTimeMillis();
                    Date postDate = new java.util.Date(milliseconds);

                    UUID newID = UUID.randomUUID();
                    String stringID = newID.toString();

                    //Potentially put this in a new method, have a List variable outside which stores the user?
                    DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            User user = documentSnapshot.toObject(User.class);
                            String username = user.getUsername();
                            String profileImage = user.getProfilePic();
                            //String postImageString = mainImageURI.toString();

                            if (ActivityCompat.checkSelfPermission(NewImageReplyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(NewImageReplyActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                // Logic to handle location object

                                                GeoPoint postLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                                                ForumPost newPost = new ForumPost(stringID, topic, mainPostName, replyContent, username, postDate, profileImage, null, postLocation, "Image", imageFileName, null);

                                                firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies").document(stringID).set(newPost);

                                            } else {
                                                //ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},44);

                                                ForumPost newPost = new ForumPost(stringID, topic, mainPostName, replyContent, username, postDate, profileImage, null, null, "Image", imageFileName, null);

                                                firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies").document(stringID).set(newPost);

                                            }

                                            DocumentReference subtopicRef = firestoreDB.collection("Forum/" + topic + "/Subtopic").document(mainPostID);
                                            subtopicRef
                                                    .update("lastReply", stringID)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@android.support.annotation.NonNull Exception e) {
                                                            Log.w(TAG, "Error updating document", e);
                                                        }
                                                    });

                                            Toast.makeText(NewImageReplyActivity.this, "Post was successfully added",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(NewImageReplyActivity.this, PostActivity.class);
                                            intent.putExtra("TopicName",topic);
                                            intent.putExtra("PostName",mainPostName);
                                            intent.putExtra("PostID",mainPostID);
                                            startActivity(intent);
                                        }
                                    });
                        } else {
                            ActivityCompat.requestPermissions(NewImageReplyActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
                            Toast.makeText(NewImageReplyActivity.this, "Please allow location access and try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            break;
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    System.out.println("Checkpoint");
                    mainImageURI = uri;
                    ivNewReplyImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    ivNewReplyImage.setImageURI(mainImageURI);

                    imageFileName = stringID + ".jpg";
                    StorageReference image_path = storageReference.child("forum_post_images").child(imageFileName);
                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {

                            Toast.makeText(NewImageReplyActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        *//*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                ivNewReplyImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivNewReplyImage.setImageURI(mainImageURI);

                imageFileName = stringID + ".jpg";
                StorageReference image_path = storageReference.child("forum_post_images").child(imageFileName);
                image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                        *//**//*image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Toast.makeText(NewImageReplyActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                            }
                        });*//**//*

                        Toast.makeText(NewImageReplyActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*//*

        if (requestCode == IMAGE_PICK_CODE && requestCode == RESULT_OK) {
            System.out.println("Checkpoint");
            mainImageURI = data.getData();
            ivNewReplyImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivNewReplyImage.setImageURI(mainImageURI);

            imageFileName = stringID + ".jpg";
            StorageReference image_path = storageReference.child("forum_post_images").child(imageFileName);
            image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {

                    Toast.makeText(NewImageReplyActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();

                    finish();
                }
            });
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //System.out.println("Button clicked");
                Intent intent = new Intent(NewImageReplyActivity.this, PostActivity.class);
                intent.putExtra("TopicName",topic);
                intent.putExtra("PostName",mainPostName);
                intent.putExtra("PostID",mainPostID);

                if (reply != null) {
                    intent.putExtra("Reply",reply);
                }
                startActivity(intent);
                break;
        }
        return true;
    }
}