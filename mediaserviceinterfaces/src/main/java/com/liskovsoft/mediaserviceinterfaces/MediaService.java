package com.liskovsoft.mediaserviceinterfaces;

public interface MediaService {
    SignInService getSignInService();
    RemoteService getRemoteService();
    MediaGroupService getMediaGroupService();
    MediaItemService getMediaItemService();
    LiveChatService getLiveChatService();
    CommentsService getCommentsService();
    void invalidateCache();
    void refreshCacheIfNeeded();
    boolean isOldStreamsEnabled();
    void enableOldStreams(boolean enable);
}
