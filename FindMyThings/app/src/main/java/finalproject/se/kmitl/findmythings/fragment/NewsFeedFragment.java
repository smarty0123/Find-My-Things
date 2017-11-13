package finalproject.se.kmitl.findmythings.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.Iterator;

import finalproject.se.kmitl.findmythings.activity.CreateNewPostActivity;
import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.FindThingsAdapter;
import finalproject.se.kmitl.findmythings.adapter.NewsFeedAdapter;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;
import finalproject.se.kmitl.findmythings.model.NewsFeed;
import finalproject.se.kmitl.findmythings.model.PostModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton btnNewPost;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private PostModel postModel;
    private String desc;
    private String image;
    private String title;
    private NewsFeedAdapter newsFeedAdapter;
    private NewsFeed newsFeed;
    private ProgressDialog mProgress;

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        initInstance(view);
        setupRecyclerView();
        storeNewsFeed();
        return view;
    }

    private void initInstance(View view) {
        recyclerView = view.findViewById(R.id.newsFeedList);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("newsfeed");
        postModel = new PostModel();
        mProgress = new ProgressDialog(getActivity());
    }

    private void setupRecyclerView() {
        newsFeedAdapter = new NewsFeedAdapter(getActivity());
        newsFeedAdapter.setData(postModel.getNewsFeedsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
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
            desc = (String) ((DataSnapshot) i.next()).getValue();
            image = (String) ((DataSnapshot) i.next()).getValue();
            title = (String) ((DataSnapshot) i.next()).getValue();
            newsFeed = new NewsFeed();
            newsFeed.setTitle(title);
            newsFeed.setImage(Uri.parse(image));
            postModel.addNewsFeedList(newsFeed);
        }
        int position = newsFeedAdapter.getItemCount() - 1;
        if(position >= 0){
            recyclerView.smoothScrollToPosition(position);
        }
        newsFeedAdapter.notifyItemRangeChanged(0, newsFeedAdapter.getItemCount());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabNewsFeed) {
            Intent intent = new Intent(getActivity(), CreateNewPostActivity.class);
            intent.putExtra("type", "newsfeed");
            startActivity(intent);
        }
    }

}
