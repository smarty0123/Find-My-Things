package finalproject.se.kmitl.findmythings.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finalproject.se.kmitl.findmythings.R;

public class PostDescription extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar = null;
    private NavigationView navigationView = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private String key;
    private DatabaseReference mDatabase;
    private DatabaseReference userProfileDB;
    private DatabaseReference child;

    private TextView tvTitle;
    private ImageView postImage;
    private TextView tvDesription;
    private TextView tvContact;
    private TextView tvDisplayName;
    private TextView tvEmail;
    private String keyUser;
    private Button btnDeletePost;
    private Button btnEditPost;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);
        initInstance();
        setNavigation();
        if (getIntent().getStringExtra("from").equals("newsfeed")) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("newsfeed");
        } else if (getIntent().getStringExtra("from").equals("find")) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("find");
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("found");
        }

        userProfileDB = FirebaseDatabase.getInstance().getReference().child("user_profile");
        userProfileDB.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phoneNumber = (String) dataSnapshot.child("phone").getValue();
                tvContact.setText(phoneNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setSupportActionBar(mToolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_open);

        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.allPost:
                        Intent intent = new Intent(PostDescription.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myPost:
                        //item.setChecked(true);
                        //break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(PostDescription.this, LoginActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        key = getIntent().getStringExtra("key");

        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = (String) dataSnapshot.child("title").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();
                    String description = (String) dataSnapshot.child("desc").getValue();
                    keyUser = (String) dataSnapshot.child("key").getValue();
                    Glide.with(PostDescription.this).load(Uri.parse(image)).into(postImage);
                    tvTitle.setText(title);
                    tvDesription.setText(description);
                    if (keyUser.equals(FirebaseAuth.getInstance().getUid())) {
                        btnDeletePost.setVisibility(View.VISIBLE);
                        btnEditPost.setVisibility(View.VISIBLE);
                    }
                }else{
                    startActivity(new Intent(PostDescription.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    private void initInstance() {
        tvTitle = findViewById(R.id.tvTitle);
        postImage = findViewById(R.id.postImage);
        tvDesription = findViewById(R.id.tvDescription);
        tvContact = findViewById(R.id.tvContact);
        btnDeletePost = findViewById(R.id.btnDeletePost);
        btnEditPost = findViewById(R.id.btnEditPost);
        mToolbar = findViewById(R.id.main_page_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        btnDeletePost.setOnClickListener(this);
        btnEditPost.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDeletePost) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(PostDescription.this);
            builder1.setMessage("คุณต้องการลบโพสต์ใช่หรือไม่");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "ใช่",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseDatabase.getInstance().getReference().child("newsfeed").child(key).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("find").child(key).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("found").child(key).removeValue();
                            dialog.dismiss();
                        }
                    });

            builder1.setNegativeButton(
                    "ไม่ใช่",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (view.getId() == R.id.btnEditPost) {
            //Do something
        }
    }
}
