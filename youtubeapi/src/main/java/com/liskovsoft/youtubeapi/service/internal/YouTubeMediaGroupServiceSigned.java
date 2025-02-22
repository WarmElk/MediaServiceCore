package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.search.SearchServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;

import java.util.List;

public class YouTubeMediaGroupServiceSigned implements MediaGroupServiceInt {
    private static final String TAG = YouTubeMediaGroupServiceSigned.class.getSimpleName();
    private final SearchServiceSigned mSearchServiceSigned;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInService mSignInManager;
    private static YouTubeMediaGroupServiceSigned sInstance;

    private YouTubeMediaGroupServiceSigned() {
        mSearchServiceSigned = SearchServiceSigned.instance();
        mBrowseServiceSigned = BrowseServiceSigned.instance();
        mSignInManager = YouTubeSignInService.instance();
    }

    public static YouTubeMediaGroupServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupServiceSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        BrowseServiceSigned.unhold();
        SearchServiceUnsigned.unhold();
    }

    @Override
    public SearchResult getSearch(String searchText) {
        return mSearchServiceSigned.getSearch(searchText, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SearchResult getSearch(String searchText, int options) {
        return mSearchServiceSigned.getSearch(searchText, options, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        return mSearchServiceSigned.getSearchTags(searchText, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public GridTab getSubscriptions() {
        return mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public List<GridTab> getSubscribedChannelsUpdate() {
        return mBrowseServiceSigned.getSubscribedChannelsUpdate(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public List<GridTab> getSubscribedChannelsAZ() {
        return mBrowseServiceSigned.getSubscribedChannelsAZ(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public List<GridTab> getSubscribedChannelsLastViewed() {
        return mBrowseServiceSigned.getSubscribedChannelsLastViewed(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public GridTab getHistory() {
        return mBrowseServiceSigned.getHistory(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SearchResultContinuation continueSearch(String nextKey) {
        Log.d(TAG, "Continue search group...");

        return mSearchServiceSigned.continueSearch(nextKey, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionTab getHomeTab() {
        Log.d(TAG, "Emitting home group...");
        SectionTab home = mBrowseServiceSigned.getHome(mSignInManager.getAuthorizationHeader());
        return home;
    }

    @Override
    public SectionTab getMusicTab() {
        Log.d(TAG, "Emitting music group...");
        return mBrowseServiceSigned.getMusic(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionTab getNewsTab() {
        Log.d(TAG, "Emitting news group...");
        return mBrowseServiceSigned.getNews(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionTab getGamingTab() {
        Log.d(TAG, "Emitting gaming group...");
        return mBrowseServiceSigned.getGaming(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionList getChannel(String channelId, String params) {
        Log.d(TAG, "Emitting channel sections...");
        return mBrowseServiceSigned.getChannel(channelId, params, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public GridTab getGridChannel(String channelId) {
        return mBrowseServiceSigned.getGridChannel(channelId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public List<GridTab> getPlaylists() {
        Log.d(TAG, "Start loading playlists...");

        return mBrowseServiceSigned.getPlaylists(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionContinuation continueSection(String nextKey) {
        Log.d(TAG, "Continue section...");

        return mBrowseServiceSigned.continueSection(nextKey, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public GridTabContinuation continueGridTab(String nextKey) {
        Log.d(TAG, "Continue grid tab...");

        return mBrowseServiceSigned.continueGridTab(nextKey, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SectionTabContinuation continueSectionTab(String nextPageKey) {
        Log.d(TAG, "Continue tab...");
        SectionTabContinuation continuation = mBrowseServiceSigned.continueSectionTab(nextPageKey, mSignInManager.getAuthorizationHeader());
        return continuation;
    }
}
