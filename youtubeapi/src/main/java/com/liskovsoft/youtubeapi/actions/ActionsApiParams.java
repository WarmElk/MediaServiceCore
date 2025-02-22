package com.liskovsoft.youtubeapi.actions;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class ActionsApiParams {
    private static final String VIDEO_ID_TEMPLATE = "\"target\":{\"videoId\":\"%s\"}";
    /**
     * params is needed for mobile notifications
     */
    private static final String CHANNEL_ID_TEMPLATE = "\"channelIds\":[\"%s\"],\"params\":\"%s\"";

    public static String getLikeActionQuery(String videoId) {
        String likeTemplate = String.format(VIDEO_ID_TEMPLATE, videoId);
        return ServiceHelper.createQuery(likeTemplate);
    }

    public static String getSubscribeActionQuery(String channelId, String params) {
        String channelTemplate = String.format(CHANNEL_ID_TEMPLATE, channelId, params != null ? params : "");
        return ServiceHelper.createQuery(channelTemplate);
    }
}
