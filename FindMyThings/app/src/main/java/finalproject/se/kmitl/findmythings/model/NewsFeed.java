package finalproject.se.kmitl.findmythings.model;

import android.net.Uri;

/**
 * Created by SMART on 13/11/2560.
 */

public class NewsFeed {
    private String title;
    private Uri image;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
