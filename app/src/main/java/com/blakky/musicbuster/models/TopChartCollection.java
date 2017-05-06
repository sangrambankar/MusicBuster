package com.blakky.musicbuster.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sangrambankar on 5/5/17.
 */

public class TopChartCollection {

    @SerializedName("collection")
    private List<TopChartCollectionItem> chartitems;

    public List<TopChartCollectionItem> getChartitems() {
        return chartitems;
    }

    public void setChartitems(List<TopChartCollectionItem> chartitems) {
        this.chartitems = chartitems;
    }

}
