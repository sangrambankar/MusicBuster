package com.blakky.musicbuster.restclient;

import com.blakky.musicbuster.models.BufferTrack;
import com.blakky.musicbuster.models.CollectionSTrack;
import com.blakky.musicbuster.models.STrack;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by sangrambankar on 4/5/17.
 */

public interface IRestClient {

    /**
     * Gets a list of tracks.
     * @param keyword Song or Artist that users put in the search view.
     * @param offset  Amount of tracks in a page.
     * @param client_id Developer ID that allows make use of the SoundCloud Api.
     * @return an {@link rx.Observable} with a response mapped to {@link java.util.List} of {@link STrack}.
     */
    @GET(RestUtils.TRACK_URL)
    Observable<CollectionSTrack> getTracks(@Query(RestUtils.QUERY) String keyword,
                                           @Query(RestUtils.LIMIT) int offset,
                                           @Query(RestUtils.ID) String client_id);


    /**
     * Gets a list of top tracks.
     * @param client_id Developer ID that allows make use of the SoundCloud Api.
     * @return an {@link rx.Observable} with a response mapped to {@link java.util.List} of {@link STrack}.
     */
    @GET(RestUtils.TOP_URL)
    Observable<List<STrack>> getTopTracks(@Query(RestUtils.GENRE) String genre, @Query(RestUtils.ID) String client_id);

    /**
     * Gets a url of  tracks.
     * @param id Song ID that allows make use of the SoundCloud Api.
     * @return an {@link rx.Observable} with a response mapped to {@link java.util.List} of {@link STrack}.
     */
    @GET(RestUtils.STREAM_URL)
    Observable<BufferTrack> getURLTrack(@Path("id") String id);
}
