package finalproject.se.kmitl.findmythings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import finalproject.se.kmitl.findmythings.R;

/**
 * Created by SMART on 13/11/2560.
 */

public class FoundThingsHolder extends RecyclerView.ViewHolder{
    public TextView postTitle;
    public FoundThingsHolder(View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.postTitle);
    }
}
