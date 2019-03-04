package com.example.whatsupcolombo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddEventActivity extends AppCompatActivity {

    private static final String TAG = "AddLocationActivity";

    //error that woul put out if the user do not have the right version of google play services
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private EditText mTitleTv, mDescriptionTv;
    private ImageView mImamgeImg;
    private Button mSubmitBtn;
    private Button pAdmappBTN;
    private TextView pLocationTv;

    //folder path to Firebase Storage
    String mStoragePath = "Image_Uploads/";
    //root path fore firebase database
    String mDatabasePAth = "Data";
    String strlocation;

    //creating URI
    Uri mFilepath;

    //creating storage and database reference
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    //progress dialog
    ProgressDialog mProgressDialog;

    //image Request code for choosing image
    int IMAGE_REQUEST_CODE = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        setupUi();
        if (isSErvicesOK()) {
            init();
        }


        //image click to load image
        mImamgeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create Intent
                Intent intent = new Intent();
                //setting intent type as image to select image from device
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);

            }
        });

        //button clik to submit data to database
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call method to upload data to firebase
                uploadDataToFirebase();
            }
        });
        //assign Firebasestoreage instance to storage reference object
        mStorageReference = FirebaseStorage.getInstance().getReference();

        //asssign firebasedatabase instance with root database name
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePAth);

        //progress daialog
        mProgressDialog = new ProgressDialog(AddEventActivity.this);
    }

    private void uploadDataToFirebase() {
        //check if filepath is empty or not
        if (mFilepath != null) {
            //setting progressbar title
            mProgressDialog.setTitle("uploading");
            //show progress dialog
            mProgressDialog.show();
            //create second storage reference
            StorageReference storageReference2 = mStorageReference.child(mStoragePath + System.currentTimeMillis() + "." + getFileExtention(mFilepath));

            //adding addOnSuccessListener to Storaqgereference
            storageReference2.putFile(mFilepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get Title
                    String mEventTitle = mTitleTv.getText().toString().trim();
                    //get description
                    String mDescrition = mDescriptionTv.getText().toString().trim();
                    String mLocationTv = pLocationTv.getText().toString().trim();
                    //hide prgressbar
                    mProgressDialog.dismiss();
                    //show toast that image is uploaded
                    Toast.makeText(AddEventActivity.this, "uploaded successfully", Toast.LENGTH_LONG).show();
                    ;
                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(mEventTitle, mDescrition, taskSnapshot.getStorage().getDownloadUrl().toString(), mEventTitle.toLowerCase(), strlocation);
                    Log.d("loggg", taskSnapshot.toString());
                    //getting image upload id
                    String imageUploadID = mDatabaseReference.push().getKey();
                    //add image upload id child elemnet to database reference
                    mDatabaseReference.child(imageUploadID).setValue(imageUploadInfo);

                }
                //if something went wrong like network failure
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //hide progress dialg
                    mProgressDialog.dismiss();
                    //show error toadt
                    Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.setTitle("Uploading");

                }
            });
        } else {

            Toast.makeText(AddEventActivity.this, "please select image", Toast.LENGTH_SHORT).show();
        }

    }

    //method to get the selected image file extension
    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setupUi() {
        mTitleTv = (EditText) findViewById(R.id.pTitleEt);
        mDescriptionTv = (EditText) findViewById(R.id.pDescriptionEt);
        mImamgeImg = (ImageView) findViewById(R.id.pImageIV);
        mSubmitBtn = (Button) findViewById(R.id.pSubmitBtn);
        pAdmappBTN = (Button) findViewById(R.id.pAdmappBTN);
        pLocationTv = (TextView) findViewById(R.id.pLocationTv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if(resultCode == RESULT_OK) {
                assert data != null;
                strlocation = data.getStringExtra("LocationValue");
                String userlocation = data.getStringExtra("uservalue");
                pLocationTv.setText(userlocation);
            }
        }

       else if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            mFilepath = data.getData();

            try {
                //getting selected image into bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mFilepath);
                //setting bitmap to imageview
                mImamgeImg.setImageBitmap(bitmap);
                mImamgeImg.setImageURI(data.getData());


            } catch (Exception e) {
                Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AddEventActivity.this, "select an image", Toast.LENGTH_SHORT).show();
            Log.d("ds", data.getData().toString());
        }
    }

    private void init() {
        pAdmappBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AddEventActivity.this, MapSelectActivity.class);
                startActivityForResult(new Intent(getApplicationContext(),MapSelectActivity.class),101);
            }
        });
    }


    public boolean isSErvicesOK() {
        Log.d(TAG, "isSErvicesOK: check google service verison");
        int available = (int) GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddEventActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // user can make map request
            Log.d(TAG, "isSErvicesOK: google play srvices ok");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //error okkorde but is fixanle
            Log.d(TAG, "isSErvicesOK: error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddEventActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            //eroor occured aunfixable
            Toast.makeText(this, "You cannot load Map right now", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
