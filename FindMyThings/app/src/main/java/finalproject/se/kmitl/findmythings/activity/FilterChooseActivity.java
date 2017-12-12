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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import finalproject.se.kmitl.findmythings.R;

public class FilterChooseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar = null;
    private NavigationView navigationView = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView tvDisplayName;
    private TextView tvEmail;
    private ImageView profileImage;
    private DatabaseReference child;
    private DatePicker datePicker;
    private Spinner thingsTypeSpinner;
    private Spinner postTypeSpinner;
    private String thingsType;
    private String postType;
    private String formatedDate;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_filter_choose);
        initInstance();
        setNavigation();
        setAppBar();
        setupThingsTypeSpinner();
        setupPostTypeSpinner();
    }

    private void initInstance() {
        mToolbar = findViewById(R.id.main_page_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        datePicker = findViewById(R.id.datePicker);
        thingsTypeSpinner = findViewById(R.id.thingsTypeSpinner);
        postTypeSpinner = findViewById(R.id.postTypeSpinner);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    private void setupThingsTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.things_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thingsTypeSpinner.setAdapter(adapter);
        thingsTypeSpinner.setOnItemSelectedListener(this);
    }

    private void setupPostTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postTypeSpinner.setAdapter(adapter);
        postTypeSpinner.setOnItemSelectedListener(this);
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
                            } else if (child.getKey().toString().equals("profilepic")) {
                                String img = (String) child.getValue();
                                Glide.with(getApplicationContext()).load(Uri.parse(img)).into(profileImage);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FilterChooseActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(FilterChooseActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myPost:
                        Intent myPostIntent = new Intent(FilterChooseActivity.this, MyPostActivity.class);
                        myPostIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myPostIntent);
                        finish();
                        item.setChecked(true);
                        break;
                    case R.id.accountSetting:
                        Intent settingIntent = new Intent(FilterChooseActivity.this, EditAccountActivity.class);
                        settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settingIntent);
                        finish();
                        item.setChecked(true);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(FilterChooseActivity.this, LoginActivity.class));
                        finish();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    private void getDatePicker() {
        int   day  = datePicker.getDayOfMonth();
        int   month= datePicker.getMonth();
        int   year = datePicker.getYear()-1900;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        formatedDate = sdf.format(new Date(year, month, day));

        //You can parse the String back to Date object by calling
        //Date date = sdf.parse(formatedDate);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.thingsTypeSpinner) {
            if (adapterView.getItemAtPosition(i).toString().equals("มือถือ")) {
                thingsType = "mobile";
            } else if (adapterView.getItemAtPosition(i).toString().equals("กุญแจ")) {
                thingsType = "key";
            } else if (adapterView.getItemAtPosition(i).toString().equals("บัตรนักศึกษา")) {
                thingsType = "student card";
            } else if (adapterView.getItemAtPosition(i).toString().equals("กระเป๋า")) {
                thingsType = "bag";
            } else {
                thingsType = "other";
            }
        } else if (spinner.getId() == R.id.postTypeSpinner) {
            if (adapterView.getItemAtPosition(i).toString().equals("เจอสิ่งของ")) {
                postType = "found";
            } else if (adapterView.getItemAtPosition(i).toString().equals("หาสิ่งของ")) {
                postType = "find";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnConfirm){
            getDatePicker();
            Intent intent = new Intent(FilterChooseActivity.this, FilteredPostActivity.class);
            intent.putExtra("thingstype", thingsType);
            intent.putExtra("posttype", postType);
            intent.putExtra("date", formatedDate);
            startActivity(intent);
        }
    }
}
