package finalproject.se.kmitl.findmythings.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.SectionPagerAdapter;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private ViewPager mViewPager;

    private TabLayout mTabLayout;
    private SectionPagerAdapter mSectionPagerAdapter;

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
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout = findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
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
