package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroupService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseManagerParams;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.sharedutils.rx.RxUtils;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import com.liskovsoft.youtubeapi.service.internal.MediaGroupServiceInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupServiceSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupServiceUnsigned;
import com.liskovsoft.youtubeapi.track.TrackingService;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YouTubeMediaGroupService implements MediaGroupService {
    private static final String TAG = YouTubeMediaGroupService.class.getSimpleName();
    private static YouTubeMediaGroupService sInstance;
    private final YouTubeSignInService mSignInService;
    private final TrackingService mTrackingService;
    private MediaGroupServiceInt mMediaGroupManagerReal;

    private YouTubeMediaGroupService() {
        Log.d(TAG, "Starting...");

        mSignInService = YouTubeSignInService.instance();
        mTrackingService = TrackingService.instance();
    }

    public static MediaGroupService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupService();
        }

        return sInstance;
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText);
        List<MediaGroup> groups = YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
        return groups != null && groups.size() > 0 ? groups.get(0) : null;
    }

    @Override
    public MediaGroup getSearch(String searchText, int options) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText, options);
        List<MediaGroup> groups = YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
        return groups != null && groups.size() > 0 ? groups.get(0) : null;
    }

    @Override
    public List<MediaGroup> getSearchAlt(String searchText) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public List<MediaGroup> getSearchAlt(String searchText, int options) {
        checkSigned();

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText, options);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return RxUtils.fromNullable(() -> getSearch(searchText));
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText, int options) {
        return RxUtils.fromNullable(() -> getSearch(searchText, options));
    }

    @Override
    public Observable<List<MediaGroup>> getSearchAltObserve(String searchText) {
        return RxUtils.fromNullable(() -> getSearchAlt(searchText));
    }

    @Override
    public Observable<List<MediaGroup>> getSearchAltObserve(String searchText, int options) {
        return RxUtils.fromNullable(() -> getSearchAlt(searchText, options));
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearchTags(searchText);
    }

    @Override
    public Observable<List<String>> getSearchTagsObserve(String searchText) {
        return RxUtils.fromNullable(() -> getSearchTags(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        GridTab subscriptions = mMediaGroupManagerReal.getSubscriptions();
        return YouTubeMediaGroup.from(subscriptions, MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return RxUtils.fromNullable(this::getSubscriptions);
    }

    @Override
    public MediaGroup getSubscribedChannelsUpdate() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsUpdate();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public MediaGroup getSubscribedChannelsAZ() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsAZ();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public MediaGroup getSubscribedChannelsLastViewed() {
        checkSigned();

        List<GridTab> subscribedChannels = mMediaGroupManagerReal.getSubscribedChannelsLastViewed();

        return YouTubeMediaGroup.fromTabs(subscribedChannels, MediaGroup.TYPE_CHANNEL_UPLOADS);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsUpdateObserve() {
        return RxUtils.fromNullable(this::getSubscribedChannelsUpdate);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsAZObserve() {
        return RxUtils.fromNullable(this::getSubscribedChannelsAZ);
    }

    @Override
    public Observable<MediaGroup> getSubscribedChannelsLastViewedObserve() {
        return RxUtils.fromNullable(this::getSubscribedChannelsLastViewed);
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        SectionTab homeTab = mMediaGroupManagerReal.getHomeTab();

        List<MediaGroup> groups = YouTubeMediaGroup.from(homeTab.getSections(), MediaGroup.TYPE_RECOMMENDED);

        MediaGroup result = null;

        if (!groups.isEmpty()) {
            result = groups.get(0); // first one is recommended
        }

        return result != null && result.isEmpty() ? continueGroup(result) : result; // Maybe a Chip?
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return RxUtils.fromNullable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        GridTab history = mMediaGroupManagerReal.getHistory();
        return YouTubeMediaGroup.from(history, MediaGroup.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return RxUtils.fromNullable(this::getHistory);
    }

    private MediaGroup getGroup(String reloadPageKey, String title, int type) {
        checkSigned();

        GridTabContinuation continuation = mMediaGroupManagerReal.continueGridTab(reloadPageKey);

        return YouTubeMediaGroup.from(continuation, reloadPageKey, title, type);
    }

    @Override
    public MediaGroup getGroup(String reloadPageKey) {
        return getGroup(reloadPageKey, null, MediaGroup.TYPE_UNDEFINED);
    }

    @Override
    public MediaGroup getGroup(MediaItem mediaItem) {
        return getGroup(mediaItem.getReloadPageKey(), mediaItem.getTitle(), mediaItem.getType());
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(MediaItem mediaItem) {
        return RxUtils.fromNullable(() -> getGroup(mediaItem));
    }

    @Override
    public Observable<MediaGroup> getGroupObserve(String reloadPageKey) {
        return RxUtils.fromNullable(() -> getGroup(reloadPageKey, null, MediaGroup.TYPE_UNDEFINED));
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        SectionTab tab = mMediaGroupManagerReal.getHomeTab();

        List<MediaGroup> result = new ArrayList<>();

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), MediaGroup.TYPE_HOME);

        if (groups.isEmpty()) {
            Log.e(TAG, "Home group is empty");
        }

        // Chips?
        for (MediaGroup group : groups) {
            if (group.isEmpty()) {
                continueGroup(group);
            }
        }

        while (!groups.isEmpty()) {
            result.addAll(groups);
            SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

            if (continuation == null) {
                break;
            }

            nextPageKey = continuation.getNextPageKey();
            groups = YouTubeMediaGroup.from(continuation.getSections(), MediaGroup.TYPE_HOME);
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getHomeTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_HOME);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getMusicTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_MUSIC);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getNewsTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_NEWS);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getGamingTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_GAMING);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(String channelId) {
        return getChannelObserve(channelId, null);
    }

    private Observable<List<MediaGroup>> getChannelObserve(String channelId, String params) {
        return RxUtils.create(emitter -> {
            checkSigned();

            // Special type of channel that could be found inside Music section (see Liked row More button)
            if (BrowseManagerParams.isGridChannel(channelId)) {
                GridTab gridChannel = mMediaGroupManagerReal.getGridChannel(channelId);

                emitGroups(emitter, gridChannel, MediaGroup.TYPE_CHANNEL_UPLOADS);
            } else {
                SectionList channel = mMediaGroupManagerReal.getChannel(channelId, params);

                emitGroups(emitter, channel, MediaGroup.TYPE_CHANNEL);
            }
        });
    }

    @Override
    public Observable<List<MediaGroup>> getChannelObserve(MediaItem item) {
        return getChannelObserve(item.getChannelId(), item.getParams());
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionTab tab, int type) {
        if (tab == null) {
            String msg = String.format("emitGroups: BrowseTab of type %s is null", type);
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
            return;
        }

        Log.d(TAG, "emitGroups: begin emitting BrowseTab of type %s...", type);

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), type);

        if (groups.isEmpty()) {
            String msg = "Media group is empty: " + type;
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
        } else {
            while (!groups.isEmpty()) {
                for (MediaGroup group : groups) { // Preserve positions
                    if (group.isEmpty()) { // Contains Chips (nested sections)?
                        group = continueGroup(group);
                    }

                    if (group != null) {
                        emitter.onNext(new ArrayList<>(Collections.singletonList(group))); // convert immutable list to mutable
                    }
                }

                SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

                if (continuation != null) {
                    nextPageKey = continuation.getNextPageKey();
                    groups = YouTubeMediaGroup.from(continuation.getSections(), type);
                } else {
                    break;
                }
            }

            emitter.onComplete();
        }
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionList sectionList, int type) {
        if (sectionList == null) {
            String msg = "emitGroups: SectionList is null";
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
            return;
        }

        List<MediaGroup> groups = YouTubeMediaGroup.from(sectionList.getSections(), type);

        if (groups.isEmpty()) {
            String msg = "emitGroups: SectionList content is null";
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
        } else {
            emitter.onNext(groups);
            emitter.onComplete();
        }
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, GridTab grid, int type) {
        if (grid == null) {
            String msg = "emitGroups: Grid is null";
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
            return;
        }

        MediaGroup group = YouTubeMediaGroup.from(grid, type);

        if (group == null) {
            String msg = "emitGroups: Grid content is null";
            Log.e(TAG, msg);
            RxUtils.onError(emitter, msg);
        } else {
            emitter.onNext(Collections.singletonList(group));
            emitter.onComplete();
        }
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        String nextKey = YouTubeHelper.extractNextKey(mediaGroup);

        switch (mediaGroup.getType()) {
            case MediaGroup.TYPE_SEARCH:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSearch(nextKey),
                        mediaGroup);
            case MediaGroup.TYPE_HISTORY:
            case MediaGroup.TYPE_SUBSCRIPTIONS:
            case MediaGroup.TYPE_USER_PLAYLISTS:
            case MediaGroup.TYPE_CHANNEL_UPLOADS:
            case MediaGroup.TYPE_UNDEFINED:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueGridTab(nextKey),
                        mediaGroup
                );
            default:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSection(nextKey),
                        mediaGroup
                );
        }
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return RxUtils.fromNullable(() -> continueGroup(mediaGroup));
    }

    private void checkSigned() {
        if (mSignInService.checkAuthHeader()) {
            Log.d(TAG, "User signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupServiceSigned.instance();
            YouTubeMediaGroupServiceUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupServiceUnsigned.instance();
            YouTubeMediaGroupServiceSigned.unhold();
        }
    }

    @Override
    public Observable<List<MediaGroup>> getPlaylistsObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            List<GridTab> tabs = mMediaGroupManagerReal.getPlaylists();

            if (tabs != null && tabs.size() > 0) {
                for (GridTab tab : tabs) {
                    GridTabContinuation tabContinuation = mMediaGroupManagerReal.continueGridTab(tab.getReloadPageKey());

                    if (tabContinuation != null) {
                        ArrayList<MediaGroup> list = new ArrayList<>();
                        YouTubeMediaGroup mediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_USER_PLAYLISTS);
                        mediaGroup.setTitle(tab.getTitle()); // id calculated by title hashcode
                        list.add(YouTubeMediaGroup.from(tabContinuation, mediaGroup));
                        emitter.onNext(list);
                    }
                }

                emitter.onComplete();
            } else {
                RxUtils.onError(emitter, "getPlaylistsObserve: tab is null");
            }
        });
    }

    @Override
    public Observable<MediaGroup> getEmptyPlaylistsObserve() {
        return RxUtils.create(emitter -> {
            checkSigned();

            List<GridTab> tabs = mMediaGroupManagerReal.getPlaylists();

            if (tabs != null && tabs.size() > 0) {
                emitter.onNext(YouTubeMediaGroup.fromTabs(tabs, MediaGroup.TYPE_USER_PLAYLISTS));
                emitter.onComplete();
            } else {
                RxUtils.onError(emitter, "getEmptyPlaylistsObserve: tab is null");
            }
        });
    }

    @Override
    public void enableHistory(boolean enable) {
        if (enable) {
            mTrackingService.resumeWatchHistory();
        } else {
            mTrackingService.pauseWatchHistory();
        }
    }

    @Override
    public void clearHistory() {
        mTrackingService.clearWatchHistory();
    }
}
