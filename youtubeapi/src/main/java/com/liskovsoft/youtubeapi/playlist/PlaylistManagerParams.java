package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class PlaylistManagerParams {
    private static final String PLAYLISTS_INFO_QUERY = "\"videoIds\":[\"%s\"]";
    private static final String ADD_TO_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"addedVideoId\":\"%s\",\"action\":\"ACTION_ADD_VIDEO\"}]";
    private static final String REMOVE_FROM_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"removedVideoId\":\"%s\",\"action\":\"ACTION_REMOVE_VIDEO_BY_VIDEO_ID\"}]";
    private static final String SAVE_REMOVE_PLAYLIST_QUERY = "\"target\":{\"playlistId\":\"%s\"}";

    public static String getPlaylistsInfoQuery(String videoId) {
        String queryTemplate = String.format(PLAYLISTS_INFO_QUERY, videoId);
        return ServiceHelper.createQuery(queryTemplate);
    }

    public static String getAddToPlaylistQuery(String playlistId, String videoId) {
        String queryTemplate = String.format(ADD_TO_PLAYLISTS_QUERY, playlistId, videoId);
        return ServiceHelper.createQuery(queryTemplate);
    }

    public static String getRemoveFromPlaylistsQuery(String playlistId, String videoId) {
        String queryTemplate = String.format(REMOVE_FROM_PLAYLISTS_QUERY, playlistId, videoId);
        return ServiceHelper.createQuery(queryTemplate);
    }

    public static String getSaveRemovePlaylistQuery(String playlistId) {
        String queryTemplate = String.format(SAVE_REMOVE_PLAYLIST_QUERY, playlistId);
        return ServiceHelper.createQuery(queryTemplate);
    }
}
