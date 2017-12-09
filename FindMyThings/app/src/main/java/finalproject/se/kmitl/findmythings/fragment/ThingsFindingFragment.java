package finalproject.se.kmitl.findmythings.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.Iterator;

import finalproject.se.kmitl.findmythings.activity.CreateNewPostActivity;
import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.activity.MainActivity;
import finalproject.se.kmitl.findmythings.adapter.FindThingsAdapter;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;
import finalproject.se.kmitl.findmythings.model.PostModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThingsFindingFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton btnNewPost;
    private DatabaseReference mDatabase;
    private FindThingsAdapter findThingsAdapter;
    private RecyclerView recyclerView;
    private PostModel postModel;
    private FindThingsPost findThingsPost;
    private String desc;
    private String image;
    private String title;

    public ThingsFindingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the nav_header_main for this fragment\
        View view = inflater.inflate(R.layout.fragment_things_finding, container, false);
        initInstance(view);
        storeFindPost();
        setupRecyclerView();
        return view;
    }


    private void initInstance(View view) {

        recyclerView = view.findViewById(R.id.findThingsList);
        btnNewPost = view.findViewById(R.id.fabFinding);
        btnNewPost.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("find");
        postModel = new PostModel();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupRecyclerView() {
        findThingsAdapter = new FindThingsAdapter(getActivity());
        findThingsAdapter.setData(postModel.getFindThingsList());
        recyclerView.setAdapter(findThingsAdapter);
    }

    private void storeFindPost() {

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);
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
            desc = (String) ((DataSnapshot) i.next()).getValue();
            image = (String) ((DataSnapshot) i.next()).getValue();
            String userKey = (String) ((DataSnapshot) i.next()).getValue();
            title = (String) ((DataSnapshot) i.next()).getValue();
            String key = dataSnapshot.getKey();
            findThingsPost = new FindThingsPost();
            findThingsPost.setTitle(title);
            findThingsPost.setKey(key);
            postModel.addFindThingsList(findThingsPost);
        }
        int position = findThingsAdapter.getItemCount() - 1;
        if (position >= 0) {
            recyclerView.smoothScrollToPosition(position);
        }
        findThingsAdapter.notifyItemRangeChanged(0, findThingsAdapter.getItemCount());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabFinding) {
            Intent intent = new Intent(getActivity(), CreateNewPostActivity.class);
            intent.putExtra("fragmenttype", "find");
            getActivity().getFragmentManager().popBackStack();
            startActivity(intent);
        }
    }
}
