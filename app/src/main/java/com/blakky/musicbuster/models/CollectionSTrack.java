package com.blakky.musicbuster.models;

import java.util.List;

/**
 * Created by sangrambankar on 5/5/17.
 */

public class CollectionSTrack {
    private List<STrack> collection;
    private String next_href;


    public List<STrack> getCollection() {
        return collection;
    }

    public void setCollection(List<STrack> collection) {
        this.collection = collection;
    }

    public String getNext_href() {
        return next_href;
    }

    public void setNext_href(String next_href) {
        this.next_href = next_href;
    }
}
