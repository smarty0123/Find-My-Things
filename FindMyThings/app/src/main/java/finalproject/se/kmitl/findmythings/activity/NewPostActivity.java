package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import finalproject.se.kmitl.findmythings.R;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference newsFeedDatabase;
    private ProgressDialog mProgress;
    private ImageButton mSelectImage;
    private EditText mPostDesc;
    private Button btnPost;
    private Uri mImageUri = null;
    private String fragmentType;
    private static final int GALLERY_REQUEST = 1;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initInstance();
    }

    private void initInstance() {
        fragmentType = getIntent().getStringExtra("fragmenttype");
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(fragmentType);
        newsFeedDatabase = FirebaseDatabase.getInstance().getReference().child("newsfeed");
        mSelectImage = findViewById(R.id.selectImage);
        mPostDesc = findViewById(R.id.etDescription);
        btnPost = findViewById(R.id.btnPost);
        mProgress = new ProgressDialog(this);

        btnPost.setOnClickListener(this);
        mSelectImage.setOnClickListener(this);
    }

    private void startPosting() {
        mProgress.setMessage("กำลังโพสต์...");
        mProgress.show();
        final String postDescription = mPostDesc.getText().toString().trim();
        if(mImageUri != null){
            final StorageReference filePath = mStorage.child(fragmentType).child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Map<String, Object> map = new HashMap<>();
                    key = mDatabase.push().getKey();
                    map.put(key, "");
                    mDatabase.updateChildren(map);
                    DatabaseReference message_key = mDatabase.child(key);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("title", getIntent().getStringExtra("title"));
                    map2.put("image", downloadUri.toString());
                    map2.put("desc", postDescription);
                    map2.put("date", getIntent().getStringExtra("date"));
                    map2.put("key", FirebaseAuth.getInstance().getUid());
                    message_key.updateChildren(map2);

                    Map<String, Object> map3 = new HashMap<>();
                    map3.put(key, "");
                    newsFeedDatabase.updateChildren(map3);
                    DatabaseReference message_key2 = newsFeedDatabase.child(key);
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("title", getIntent().getStringExtra("title"));
                    map4.put("image", downloadUri.toString());
                    map4.put("desc", postDescription);
                    map4.put("date", getIntent().getStringExtra("date"));
                    map4.put("key", FirebaseAuth.getInstance().getUid());
                    message_key2.updateChildren(map4);

                    mProgress.dismiss();
                    Toast.makeText(NewPostActivity.this, "โพสต์แล้วจ้าาา", Toast.LENGTH_SHORT).show();
                    goToMain();

                }
            });
        }else{
            mProgress.hide();
        }
    }

    private void goToMain(){
        Intent intent = new Intent(NewPostActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.selectImage) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        } else if (view.getId() == R.id.btnPost) {
            startPosting();
        }
    }

}
