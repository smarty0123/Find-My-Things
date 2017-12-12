package finalproject.se.kmitl.findmythings.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Iterator;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.NewsFeedAdapter;
import finalproject.se.kmitl.findmythings.model.NewsFeed;
import finalproject.se.kmitl.findmythings.model.PostModel;

public class MyPostActivity extends AppCompatActivity {
    private Toolbar mToolbar = null;
    private NavigationView navigationView = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView tvDisplayName;
    private TextView tvEmail;

    private DatabaseReference child;

    private Query mDatabase;

    private RecyclerView recyclerView;
    private PostModel postModel;

    private NewsFeedAdapter newsFeedAdapter;
    private NewsFeed newsFeed;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_my_post);
        initInstance();
        setNavigation();
        setAppBar();
        storeNewsFeed();
        setupRecyclerView();
    }

    private void initInstance() {
        mToolbar = findViewById(R.id.main_page_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        recyclerView = findViewById(R.id.myPostRecycler);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("newsfeed").orderByChild("key").equalTo(FirebaseAuth.getInstance().getUid());
        postModel = new PostModel();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setNavigation() {
        View headerView = navigationView.getHeaderView(0);
        tvDisplayName = headerView.findViewById(R.id.displayName);
        tvEmail = headerView.findViewById(R.id.email);
        profileImage = headerView.findViewById(R.id.profile_image);
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
                            }else if(child.getKey().toString().equals("profilepic")){
                                String img = (String) child.getValue();
                                Glide.with(MyPostActivity.this).load(Uri.parse(img)).into(profileImage);
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
                        Intent intent = new Intent(MyPostActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myPost:
                        Intent myPostIntent = new Intent(MyPostActivity.this, MyPostActivity.class);
                        myPostIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myPostIntent);
                        finish();
                        item.setChecked(true);
                        break;
                    case R.id.accountSetting:
                        Intent settingIntent = new Intent(MyPostActivity.this, EditAccountActivity.class);
                        settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settingIntent);
                        finish();
                        item.setChecked(true);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MyPostActivity.this, LoginActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        newsFeedAdapter = new NewsFeedAdapter(this.getApplicationContext());
        newsFeedAdapter.setData(postModel.getNewsFeedsList());
        recyclerView.setAdapter(newsFeedAdapter);
    }

    private void storeNewsFeed() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onGetChild(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String date = (String) ((DataSnapshot) i.next()).getValue();
            String desc = (String) ((DataSnapshot) i.next()).getValue();
            String image = (String) ((DataSnapshot) i.next()).getValue();
            String userKey = (String) ((DataSnapshot) i.next()).getValue();
            String title = (String) ((DataSnapshot) i.next()).getValue();
            String type = (String) ((DataSnapshot) i.next()).getValue();
            String key = dataSnapshot.getKey();
            newsFeed = new NewsFeed();
            newsFeed.setDate(date);
            newsFeed.setTitle(title);
            newsFeed.setImage(Uri.parse(image));
            newsFeed.setKey(key);
            postModel.addNewsFeedList(newsFeed);
        }
        int position = newsFeedAdapter.getItemCount() - 1;
        if (position >= 0) {
            recyclerView.smoothScrollToPosition(position);
        }
        newsFeedAdapter.notifyItemRangeChanged(0, newsFeedAdapter.getItemCount());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyPostActivity.this, MainActivity.class));
        finish();
    }
}
