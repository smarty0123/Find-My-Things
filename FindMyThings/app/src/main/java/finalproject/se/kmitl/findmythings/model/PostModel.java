package finalproject.se.kmitl.findmythings.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SMART on 12/11/2560.
 */

public class PostModel {
    private List<FindThingsPost> findThingsList = new ArrayList<>();
    private List<FoundThingsPost> foundThingsList = new ArrayList<>();
    private List<NewsFeed> newsFeedsList = new ArrayList<>();

    public List<FindThingsPost> getFindThingsList() {
        return findThingsList;
    }

    public void setFindThingsList(List<FindThingsPost> findThingsList) {
        this.findThingsList = findThingsList;
    }

    public List<FoundThingsPost> getFoundThingsList() {
        return foundThingsList;
    }

    public void setFoundThingsList(List<FoundThingsPost> foundThingsList) {
        this.foundThingsList = foundThingsList;
    }

    public List<NewsFeed> getNewsFeedsList() {
        return newsFeedsList;
    }

    public void setNewsFeedsList(List<NewsFeed> newsFeedsList) {
        this.newsFeedsList = newsFeedsList;
    }

    public void addFindThingsList(FindThingsPost findItem){
        this.findThingsList.add(findItem);
    }

    public void addFoundThingsList(FoundThingsPost foundItem){
        this.foundThingsList.add(foundItem);
    }
    public void addNewsFeedList(NewsFeed newsFeedItem){
        this.newsFeedsList.add(newsFeedItem);
    }
}
