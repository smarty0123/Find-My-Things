package finalproject.se.kmitl.findmythings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import finalproject.se.kmitl.findmythings.R;

/**
 * Created by SMART on 12/11/2560.
 */

public class FindThingsHolder extends RecyclerView.ViewHolder{
    public TextView postTitle;
    public FindThingsHolder(View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.postTitle);
    }
}
