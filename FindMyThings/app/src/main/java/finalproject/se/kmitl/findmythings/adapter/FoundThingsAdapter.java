package finalproject.se.kmitl.findmythings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.activity.PostDescription;
import finalproject.se.kmitl.findmythings.adapter.holder.FindThingsHolder;
import finalproject.se.kmitl.findmythings.adapter.holder.FoundThingsHolder;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;
import finalproject.se.kmitl.findmythings.model.FoundThingsPost;

/**
 * Created by SMART on 13/11/2560.
 */

public class FoundThingsAdapter extends RecyclerView.Adapter<FoundThingsHolder> {
    private final Context context;
    private List<FoundThingsPost> data;

    public FoundThingsAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<FoundThingsPost> data) {
        this.data = data;
    }

    @Override
    public FoundThingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.foundthings_row, parent, false);
        FoundThingsHolder holder = new FoundThingsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FoundThingsHolder holder, final int position) {
        holder.postTitle.setText(data.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDescription.class);
                intent.putExtra("from", "found");
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
