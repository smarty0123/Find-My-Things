package finalproject.se.kmitl.findmythings.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.SectionPagerAdapter;


public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar = null;

    private FirebaseAuth mAuth;
    private DatabaseReference child;

    private ViewPager mViewPager;
    private DrawerLayout drawerLayout;
    private TabLayout mTabLayout;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TextView tvDisplayName;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstance();
    }

    private void initInstance() {
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.main_page_toolbar);
        mViewPager = findViewById(R.id.main_tabPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        mTabLayout = findViewById(R.id.main_tabs);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvDisplayName = headerView.findViewById(R.id.displayName);
        tvEmail = headerView.findViewById(R.id.email);

        if(FirebaseAuth.getInstance().getUid() != null){
            child = FirebaseDatabase.getInstance().getReference().child("user_profile");
            child.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().toString().equals("displayname")) {
                                String name = (String) child.getValue();
                                tvDisplayName.setText(name);
                            }else if(child.getKey().toString().equals("email")){
                                String email = (String) child.getValue();
                                tvEmail.setText(email);
                            }else if(child.getKey().toString().equals("phone")){
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



        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        setSupportActionBar(mToolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_open);

        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.allPost:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myPost:
                        Intent myPostIntent = new Intent(MainActivity.this, MyPostActivity.class);
                        myPostIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myPostIntent);
                        finish();
                        item.setChecked(true);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }


                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

        }

    }

}
