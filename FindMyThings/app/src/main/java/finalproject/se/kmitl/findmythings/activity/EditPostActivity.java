package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.HashMap;
import java.util.Map;

import finalproject.se.kmitl.findmythings.R;


public class EditPostActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar = null;
    private NavigationView navigationView = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView tvDisplayName;
    private TextView tvEmail;

    private DatabaseReference child;
    private EditText etTitle;
    private EditText etDescription;
    private ImageView postImage;
    private EditText tvContact;

    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private Button btnConfirm;

    private ProgressDialog mProgress;

    private StorageReference mStorage;
    private String key;
    private String fragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        initInstance();
        setNavigation();
        setAppBar();
        initInformation();
    }


    private void setAppBar() {
        setSupportActionBar(mToolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_open);

        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.allPost:
                        Intent intent = new Intent(EditPostActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myPost:
                        item.setChecked(true);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(EditPostActivity.this, LoginActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    private void initInstance() {
        mToolbar = findViewById(R.id.main_page_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        postImage = findViewById(R.id.postImage);
        tvContact = findViewById(R.id.tvContact);
        btnConfirm = findViewById(R.id.btnConfirm);

        mProgress = new ProgressDialog(this);

        fragmentType = getIntent().getStringExtra("fragmenttype");

        postImage.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance().getReference();

    }

    private void setNavigation() {
        View headerView = navigationView.getHeaderView(0);
        tvDisplayName = headerView.findViewById(R.id.displayName);
        tvEmail = headerView.findViewById(R.id.email);

        if (FirebaseAuth.getInstance().getUid() != null) {
            child = FirebaseDatabase.getInstance().getReference().child("user_profile");
            child.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().toString().equals("displayname")) {
                                String name = (String) child.getValue();
                                tvDisplayName.setText(name);
                            } else if (child.getKey().toString().equals("email")) {
                                String email = (String) child.getValue();
                                tvEmail.setText(email);
                            } else if (child.getKey().toString().equals("phone")) {
                                String phoneNum = (String) child.getValue();
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void initInformation() {
        etTitle.setText(getIntent().getStringExtra("title"));
        etDescription.setText(getIntent().getStringExtra("desc"));
        Glide.with(this).load(Uri.parse(getIntent().getStringExtra("image"))).into(postImage);
        tvContact.setText(getIntent().getStringExtra("phone"));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.postImage) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        } else if (view.getId() == R.id.btnConfirm) {
            startUpdate();
        }
    }

    private void startUpdate() {
        mProgress.setMessage("กำลังแก้ไข...");
        mProgress.show();
        final StorageReference filePath = mStorage.child(fragmentType).child(mImageUri.getLastPathSegment());
        filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                Map<String, Object> map = new HashMap<>();
                key = getIntent().getStringExtra("key");
                map.put(key, "");
                FirebaseDatabase.getInstance().getReference().child(fragmentType).updateChildren(map);
                DatabaseReference message_key = FirebaseDatabase.getInstance().getReference().child(fragmentType).child(key);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("title", etTitle.getText().toString().trim());
                map2.put("image", downloadUri.toString());
                map2.put("desc", etDescription.getText().toString().trim());
                map2.put("date", getIntent().getStringExtra("date"));
                map2.put("key", FirebaseAuth.getInstance().getUid());
                message_key.updateChildren(map2);

                Map<String, Object> map3 = new HashMap<>();
                map3.put(key, "");
                FirebaseDatabase.getInstance().getReference().child("newsfeed").updateChildren(map3);
                DatabaseReference message_key2 = FirebaseDatabase.getInstance().getReference().child("newsfeed").child(key);
                Map<String, Object> map4 = new HashMap<>();
                map4.put("title", etTitle.getText().toString().trim());
                map4.put("image", downloadUri.toString());
                map4.put("desc", etDescription.getText().toString().trim());
                map4.put("date", getIntent().getStringExtra("date"));
                map4.put("key", FirebaseAuth.getInstance().getUid());
                message_key2.updateChildren(map4);

                mProgress.dismiss();
                Toast.makeText(EditPostActivity.this, "แก้ไขเรียบร้อย", Toast.LENGTH_SHORT).show();
                goToPostDescription();

            }
        });

    }

    private void goToPostDescription() {
        Intent intent = new Intent(EditPostActivity.this, PostDescription.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            postImage.setImageURI(mImageUri);
        }
    }
}
