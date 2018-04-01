package com.example.android.yos_1202154119_modul6;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView mImageView;
    Button mBtnSelect;
    EditText mETTitle;
    EditText mETCaption;
    FloatingActionButton mBtnUpload;
    ProgressBar mProgressBar;
    Uri mImgUri;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;

    StorageTask mUploadTask;

    String userID;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        FirebaseUser FUser = FirebaseAuth.getInstance().getCurrentUser();

        //get ID dan email di UploadModel
        userID = FUser.getUid();
        email = FUser.getEmail();

        mImageView = (ImageView) findViewById(R.id.imageview);
        mBtnSelect = (Button) findViewById(R.id.btnSelect);
        mETTitle = (EditText) findViewById(R.id.postTitle);
        mETCaption = (EditText) findViewById(R.id.postCaption);
        mBtnUpload = (FloatingActionButton) findViewById(R.id.fab);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    public void chooseFile(View view) {
        openFileChooser();
    }

    private void openFileChooser() {
        //menjalankan explorer image dengan intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImgUri = data.getData();
            //gambar yang dipilih akan diproses untuk ditampilkan
            Picasso.get().load(mImgUri).into(mImageView);
        }
    }

    private void uploadFile(){
        //jika bernilai null maka menjalankan perintah untuk upload gambar
        if (mImgUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImgUri));
            mUploadTask = fileReference.putFile(mImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 100);
                            Toast.makeText(UploadActivity.this, "Upload success", Toast.LENGTH_LONG).show();
                            UploadModel upload = new UploadModel(
                                    mETTitle.getText().toString().trim(),
                                    mETCaption.getText().toString(),
                                    userID, taskSnapshot.getDownloadUrl().toString(), email);
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Select the file", Toast.LENGTH_SHORT).show();
        }
    }
    //untuk mendapatkan ekstensi dari file gambar yang diupload
    private String getFileExtension(Uri uri) {
        ContentResolver CR = getContentResolver();
        MimeTypeMap MTM = MimeTypeMap.getSingleton();
        return MTM.getExtensionFromMimeType(CR.getType(uri));
    }
}
