package finalproject.se.kmitl.findmythings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.activity.MainActivity;
import finalproject.se.kmitl.findmythings.activity.PostDescription;
import finalproject.se.kmitl.findmythings.adapter.holder.NewsFeedHolder;
import finalproject.se.kmitl.findmythings.model.NewsFeed;

/**
 * Created by SMART on 13/11/2560.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedHolder> {
    private final Context context;
    private List<NewsFeed> data;

    public NewsFeedAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<NewsFeed> data) {
        this.data = data;
    }

    @Override
    public NewsFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.news_feed_row, parent, false);
        NewsFeedHolder holder = new NewsFeedHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsFeedHolder holder, final int position) {
        Glide.with(context).load(data.get(position).getImage()).into(holder.imageView);
        holder.postDate.setText("โพสต์วันที่ " + data.get(position).getDate());
        holder.postTitle.setText(data.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDescription.class);
                intent.putExtra("from", "newsfeed");
                intent.putExtra("key", data.get(position).getKey());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
