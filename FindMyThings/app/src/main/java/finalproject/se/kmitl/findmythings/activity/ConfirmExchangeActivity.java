package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import finalproject.se.kmitl.findmythings.R;

public class ConfirmExchangeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView confirmImage;
    private Button btnConfirm;
    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgress;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private String fragmentType;
    private String key;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_confirm_exchange);
        initInstance();

    }

    private void initInstance() {
        fragmentType = getIntent().getStringExtra("fragmentType");

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        confirmImage = findViewById(R.id.confirmImage);
        confirmImage.setOnClickListener(this);

        mProgress = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        key = getIntent().getStringExtra("key");
    }

    private void startPosting() {
        mProgress.setMessage("กำลังโพสต์...");
        mProgress.show();
        if (mImageUri != null) {
            final StorageReference filePath = mStorage.child("confirmexchange").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    updateFindDB();
                    updateFoundDB();
                    updateNewsFeedDB();
                    mProgress.dismiss();
                    Toast.makeText(ConfirmExchangeActivity.this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
                    goEditPostPage();
                }
            });
        } else {
            Toast.makeText(ConfirmExchangeActivity.this, "กรุณาเพิ่มรูปภาพ", Toast.LENGTH_SHORT).show();
            mProgress.hide();
        }
    }

    private void goEditPostPage() {
        Intent intent = new Intent(ConfirmExchangeActivity.this, EditPostActivity.class);
        intent.putExtra("from", "confirmpage");
        intent.putExtra("image", getIntent().getStringExtra("image"));
        intent.putExtra("title", getIntent().getStringExtra("title"));
        intent.putExtra("desc", getIntent().getStringExtra("desc"));
        intent.putExtra("status", "true");
        intent.putExtra("key",getIntent().getStringExtra("key"));
        intent.putExtra("fragmenttype", getIntent().getStringExtra("fragmenttype"));
        startActivity(intent);
        finish();
    }

    private void updateNewsFeedDB() {
        mDatabase.child("newsfeed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)) {
                    mDatabase.child("newsfeed").child(key).child("status").setValue("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConfirmExchangeActivity.this, databaseError.toException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFoundDB() {
        mDatabase.child("found").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)) {
                    mDatabase.child("found").child(key).child("status").setValue("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConfirmExchangeActivity.this, databaseError.toException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFindDB() {
        mDatabase.child("find").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)) {
                    mDatabase.child("find").child(key).child("status").setValue("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConfirmExchangeActivity.this, databaseError.toException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                confirmImage.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ConfirmExchangeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnConfirm) {
            startPosting();
        } else if (view.getId() == R.id.confirmImage) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }
    }


}
