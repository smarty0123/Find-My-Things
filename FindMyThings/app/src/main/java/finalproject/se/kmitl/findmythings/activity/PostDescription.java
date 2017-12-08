package finalproject.se.kmitl.findmythings.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finalproject.se.kmitl.findmythings.R;

public class PostDescription extends AppCompatActivity {

    private String key;
    private DatabaseReference mDatabase;

    private TextView tvTitle;
    private ImageView postImage;
    private TextView tvDesription;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_description);
        tvTitle = findViewById(R.id.tvTitle);
        postImage = findViewById(R.id.postImage);
        tvDesription = findViewById(R.id.tvDescription);
        if(getIntent().getStringExtra("from").equals("newsfeed")){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("newsfeed");
        }else if(getIntent().getStringExtra("from").equals("find")){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("find");
        }else{
            mDatabase = FirebaseDatabase.getInstance().getReference().child("found");
        }

        key = getIntent().getStringExtra("key");

        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                String image = (String) dataSnapshot.child("image").getValue();
                String description = (String) dataSnapshot.child("desc").getValue();

                Glide.with(PostDescription.this).load(Uri.parse(image)).into(postImage);
                tvTitle.setText(title);
                tvDesription.setText(description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
