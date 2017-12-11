package finalproject.se.kmitl.findmythings.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import finalproject.se.kmitl.findmythings.R;

public class FilteredPostActivity extends AppCompatActivity {

    private TextView thing;
    private TextView post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_post);
        thing = findViewById(R.id.things);
        post = findViewById(R.id.post);
        thing.setText(getIntent().getStringExtra("date"));
        post.setText(getIntent().getStringExtra("posttype"));
    }
}
