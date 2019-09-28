package com.arunsinghsaab98.project_feedback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.service.autofill.SaveCallback;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsinghsaab98.project_feedback.model.Complaint;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

public class Comp_Act extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

//    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    private Spinner spinner_timeSlot,spinner_product,spinner_issue;
    private ImageView product_image;
    private Button btn_complain,btn_chooseImage,btn_upload;
    private ArrayAdapter<String>  productAdapter,timeAdapter,issueAdapter;
    ValueEventListener listener;
    private ArrayList<String> timeSlotList,productList,issueList;
    DatabaseReference product_Reference,issue_Reference,timeSlot_Reference;
//    private ProgressDialog progressDialog;

    private HashMap<String,String> imgUrl;
    private long count ;
    private String currentUserId;

    private Uri filePath ;


    private static final int PICK_IMAGE_REQUEST=71;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    FirebaseAuth mAuth;

    private LocationManager locationManager;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


    DatabaseReference mRef = mDatabase.getReference("Complaints");

    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    String userid=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_);
        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }


            if (Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION},3000);
            }




//        firebase initialization

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


//        for location


//        initialising databaseReference
        product_Reference = FirebaseDatabase.getInstance().getReference("Products");
        issue_Reference = FirebaseDatabase.getInstance().getReference("Issues");
        timeSlot_Reference = FirebaseDatabase.getInstance().getReference("TimeSlot");


        spinner_issue = findViewById(R.id.spinnerIssues);
        spinner_product = findViewById(R.id.spinnerProduct);
        spinner_timeSlot = findViewById(R.id.spinnerTimeSlot);
        product_image = findViewById(R.id.product_image);
//        progressDialog = new ProgressDialog(getApplicationContext());

//        mAuth = FirebaseAuth.getInstance();
//        mStorageRef = FirebaseStorage.getInstance().getReference();
//        DatabaseReference product_reference = mDatabase.getReference("Products");

        // Spinner Drop down elements for product
//        List<String> product_categories = new ArrayList<String>();
//        product_categories.add("Refrigirator");
//        product_categories.add("Oven");
//        product_categories.add("Induction");
//        product_categories.add("Mobile");
//        product_categories.add("Laptop");
//        product_categories.add("Washing Machine");

        productList = new ArrayList<>();
        issueList = new ArrayList<>();
        timeSlotList = new ArrayList<>();

        // Creating adapter for spinner
         productAdapter = new ArrayAdapter<String>(this,
                R.layout.bigtext_size_spinner, productList);
         RetrieveProductData();
        spinner_product.setAdapter(productAdapter);



//        product_reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Is better to use a List, because you don't know the size
//                // of the iterator returned by dataSnapshot.getChildren() to
//                // initialize the array
//                final List<String> product = new ArrayList<String>();
//
//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.getValue(String.class);
//                    product.add(areaName);
//                }
//
//                spinner_product = findViewById(R.id.spinnerProduct);
//                ArrayAdapter<String> proAdapter = new ArrayAdapter<String>(Comp_Act.this, android.R.layout.simple_spinner_item, product);
//                proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner_product.setAdapter(proAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        // Spinner Drop down elements for issues

        issueAdapter = new ArrayAdapter<String>(this,R.layout.bigtext_size_spinner,issueList);
        RetrieveIssuesData();
        spinner_issue.setAdapter(issueAdapter);


         timeAdapter = new ArrayAdapter<String>(this,R.layout.bigtext_size_spinner,timeSlotList);
         RetrieveTimeSlotData();
        spinner_timeSlot.setAdapter(timeAdapter);

        // Spinner click listener
        spinner_product.setOnItemSelectedListener(this);
        spinner_issue.setOnItemSelectedListener(this);
        spinner_timeSlot.setOnItemSelectedListener(this);


        btn_chooseImage = findViewById(R.id.chooseImage);
        product_image = findViewById(R.id.product_image);

        btn_complain = findViewById(R.id.btn_complain);

//        checkFilePermissions();

        btn_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},3000);
                }
                else
                {
                    chooseImage();
                }
            }
        });

        btn_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




                uploadImage();
//
//                String user_name = user.getEmail();
//                String time = spinner_timeSlot.getSelectedItem().toString();
//                String product = spinner_product.getSelectedItem().toString();
//                String issue = spinner_issue.getSelectedItem().toString();
//
////                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("storage ref url in string");
////                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                    @Override
////                    public void onSuccess(Uri uri) {
////                        //do your stuff- uri.toString() will give you download URL\\
////                    }
////                });
//
////                StorageReference dwndImgUrlRef = FirebaseStorage.getInstance().getReferenceFromUrl("images/"+user.getEmail());
////                dwndImgUrlRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Uri> task) {
////                         imgUrl = task.toString();
////                    }
////                });
//
////                StorageReference ref = storageReference.child("images/"+user.getEmail());
////                String downloadUrl =  ref.getDownloadUrl().toString();
////                String downloadUrl = imgUrl;
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//////                question_Score.child(String.format("%s_%s", Common.CurrentUser.getUserName()
//////                        ,Common.CategoryId))
//////                        .setValue(new QuestionScore(String.format("%s_%s",Common.CurrentUser.getUserName(),
//////                                Common.CategoryId),
//////                                Common.CurrentUser.getUserName(),
//////                                String.valueOf(score),
//////                                Common.CategoryId,
//////                                Common.categoryName
//////                        ));
////
////
////                mRef.child(String.format("%s_%s",user.getEmail(),product))
////                        .setValue( new Complaint(String.format("%s_%s",user.getEmail(),
////                               ),
////                                user_name,
////                                time,
////                                product,
////                                issue
////                                ));
//
//                final Complaint complaint = new Complaint(user_name,time,product,issue,imgUrl);
//                mAuth  = FirebaseAuth.getInstance();
//                currentUserId = mAuth.getCurrentUser().toString();
//                mRef.child(userid).child("complaint"+count++).setValue(complaint);
//                count =1;

//method to get count of no. of childs
//                mRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists())
//                        {
//                            count =  (int) dataSnapshot.getChildrenCount();
//                        }else
//                        {
//                            count =0;
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                String sCount = count+"";

//                mRef.child(userid).child("complaint"+sCount).setValue(complaint);



                Toast.makeText(Comp_Act.this, "done " , Toast.LENGTH_SHORT).show();
            }
        });

//        btn_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage();
//            }
//        });

    }



    private void chooseImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"selectImage"),PICK_IMAGE_REQUEST);
//        startActivityForResult(intent,ImageBack);

    }


//    private void checkFilePermissions() {
//
//        if (Build.VERSION.SDK_INT >= 23)
//        {
//            int permissionCheck = Comp_Act.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
//            permissionCheck += Comp_Act.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
//            if (permissionCheck != 0)
//            {
//                this.requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE, });
//            }
//
//        }
//
//    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        Toast.makeText(getApplicationContext(),item,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    private void captureImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CAMERA_REQUEST_CODE);
//        }
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {

                filePath = data.getData();
                try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                product_image.setImageBitmap(bitmap);


            }
                catch (IOException e)
            {
                e.printStackTrace();
            }


        }

//        if (requestCode == ImageBack )
//        if (resultCode == RESULT_OK)
//        {
//            Uri ImageData = data.getData();
//            StorageReference ref = storageReference.child("image"+ImageData.getLastPathSegment());
//            ref.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(Comp_Act.this, "Uploaded..", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.signout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.sign_out)
       {
           FirebaseAuth.getInstance().signOut();
           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
           startActivity(intent);
           finish();
       }

        return super.onOptionsItemSelected(item);


    }

    private void uploadImage() {
        if (filePath != null)
        {
            try {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading....");
                progressDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            final StorageReference ref = storageReference.child("images/"+user.getEmail()+UUID.randomUUID().toString());


//                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
//                StorageReference ref = storageReferenceProfilePic.child("YourPath" + "/" + "ImageName" + ".jpg");
//                StorageReference ref = storageReference.child("complainImages/").child("image"+filePath+".jpg");

//                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        progressDialog.dismiss();
//                        Toast.makeText(Comp_Act.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                        Toast.makeText(Comp_Act.this, "Not Uploaded"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                    }
//                });

                product_image.setDrawingCacheEnabled(true);
                product_image.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) product_image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = ref.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Comp_Act.this, "Not Uploaded"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
//                        Toast.makeText(Comp_Act.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                DatabaseReference countRef = mRef.child(userid);
                                countRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        count = dataSnapshot.getChildrenCount();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                    Uri downloadUrl = task.getResult();

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                    User user1 = new User();
                                    String user_name = user.getEmail();
                                    String time = spinner_timeSlot.getSelectedItem().toString();
                                    String product = spinner_product.getSelectedItem().toString();
                                    String issue = spinner_issue.getSelectedItem().toString();

//                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("storage ref url in string");
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        //do your stuff- uri.toString() will give you download URL\\
//                    }
//                });

//                StorageReference dwndImgUrlRef = FirebaseStorage.getInstance().getReferenceFromUrl("images/"+user.getEmail());
//                dwndImgUrlRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                         imgUrl = task.toString();
//                    }
//                });

//                StorageReference ref = storageReference.child("images/"+user.getEmail());
//                String downloadUrl =  ref.getDownloadUrl().toString();
//                String downloadUrl = imgUrl;
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////                question_Score.child(String.format("%s_%s", Common.CurrentUser.getUserName()
////                        ,Common.CategoryId))
////                        .setValue(new QuestionScore(String.format("%s_%s",Common.CurrentUser.getUserName(),
////                                Common.CategoryId),
////                                Common.CurrentUser.getUserName(),
////                                String.valueOf(score),
////                                Common.CategoryId,
////                                Common.categoryName
////                        ));
//
//
//                mRef.child(String.format("%s_%s",user.getEmail(),product))
//                        .setValue( new Complaint(String.format("%s_%s",user.getEmail(),
//                               ),
//                                user_name,
//                                time,
//                                product,
//                                issue
//                                ));DatabaseReference mRef = mDatabase.getReference("Complaints");




                                    final Complaint complaint = new Complaint(user_name, time, product, issue, downloadUrl.toString());
                                    mAuth = FirebaseAuth.getInstance();
                                    currentUserId = mAuth.getCurrentUser().toString();
                                    mRef.child(userid).child("complaint" + ++count).setValue(complaint);
                                    Toast.makeText(Comp_Act.this, " "+count, Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");


                    }
                });


//                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//
//                        if (!task.isSuccessful()) {
//                            Uri downloadUrl = task.getResult();
//
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                            String user_name = user.getEmail();
//                            String time = spinner_timeSlot.getSelectedItem().toString();
//                            String product = spinner_product.getSelectedItem().toString();
//                            String issue = spinner_issue.getSelectedItem().toString();
//
////                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("storage ref url in string");
////                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                    @Override
////                    public void onSuccess(Uri uri) {
////                        //do your stuff- uri.toString() will give you download URL\\
////                    }
////                });
//
////                StorageReference dwndImgUrlRef = FirebaseStorage.getInstance().getReferenceFromUrl("images/"+user.getEmail());
////                dwndImgUrlRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Uri> task) {
////                         imgUrl = task.toString();
////                    }
////                });
//
////                StorageReference ref = storageReference.child("images/"+user.getEmail());
////                String downloadUrl =  ref.getDownloadUrl().toString();
////                String downloadUrl = imgUrl;
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//////                question_Score.child(String.format("%s_%s", Common.CurrentUser.getUserName()
//////                        ,Common.CategoryId))
//////                        .setValue(new QuestionScore(String.format("%s_%s",Common.CurrentUser.getUserName(),
//////                                Common.CategoryId),
//////                                Common.CurrentUser.getUserName(),
//////                                String.valueOf(score),
//////                                Common.CategoryId,
//////                                Common.categoryName
//////                        ));
////
////
////                mRef.child(String.format("%s_%s",user.getEmail(),product))
////                        .setValue( new Complaint(String.format("%s_%s",user.getEmail(),
////                               ),
////                                user_name,
////                                time,
////                                product,
////                                issue
////                                ));
//
//                            final Complaint complaint = new Complaint(user_name, time, product, issue, downloadUrl.toString());
//                            mAuth = FirebaseAuth.getInstance();
//                            currentUserId = mAuth.getCurrentUser().toString();
//                            mRef.child(userid).child("complaint" + count++).setValue(complaint);
//
//                        }
//                        else
//                        {
//                            Toast.makeText(Comp_Act.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

//                // Get the data from an ImageView as bytes
//                imageView.setDrawingCacheEnabled(true);
//                imageView.buildDrawingCache();
//                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] data = baos.toByteArray();
//
//                UploadTask uploadTask = mountainsRef.putBytes(data);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                        // ...
//                    }
//                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }


    public void RetrieveProductData()
    {



        listener = product_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productList.clear();
                productAdapter.notifyDataSetChanged();

                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    productList.add(item.getValue().toString());
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void RetrieveIssuesData()
    {

        listener = issue_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                issueList.clear();
                issueAdapter.notifyDataSetChanged();

                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    issueList.add(item.getValue().toString());
                }
                issueAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void RetrieveTimeSlotData()
    {

        listener = timeSlot_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timeSlotList.clear();
                timeAdapter.notifyDataSetChanged();
                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    timeSlotList.add(item.getValue().toString());
                }
                timeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
