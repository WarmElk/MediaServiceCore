package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchManagerUnsignedTest extends SearchManagerTestBase {
    private static final String SEARCH_TEXT = "thrones season 8 trailer";
    private static final String SEARCH_TEXT_2 = "miley cyrus";
    private static final String SEARCH_TEXT_SPECIAL_CHAR = "What's Trending";
    private SearchManagerUnsigned mSearchManagerUnsigned;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mSearchManagerUnsigned = RetrofitHelper.withJsonPath(SearchManagerUnsigned.class);
    }

    @Test
    public void testThatSearchResultIsValid() {
        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT));
        SearchResult searchResult = RetrofitHelper.get(wrapper);

        checkSearchResult(searchResult);

        wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_SPECIAL_CHAR));
        searchResult = RetrofitHelper.get(wrapper);

        checkSearchResult(searchResult);
    }

    @Test
    public void testThatContinuationResultIsValid() {
        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT));
        SearchResult result = RetrofitHelper.get(wrapper);
        checkSearchResult(result);

        String nextPageKey = result.getNextPageKey();
        Call<SearchResultContinuation> wrapper2 = mSearchManagerUnsigned.continueSearchResult(SearchManagerParams.getContinuationQuery(nextPageKey));
        SearchResultContinuation result2 = RetrofitHelper.get(wrapper2);
        checkSearchResultContinuation(result2);
    }

    @Test
    public void testThatResultContainsMultipleItems() {
        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_2));
        SearchResult searchResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains multiple items", searchResult.getItemWrappers().size() > 5);

        checkSearchResultTileItem(searchResult.getItemWrappers().get(0).getTileItem());
    }

    @Test
    public void testThatResultContainsMultipleRows() {
        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_2));
        SearchResult searchResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains multiple rows", searchResult.getSections().size() > 1);

        checkSearchResultTileItem(searchResult.getSections().get(0).getItemWrappers().get(0).getTileItem());
    }

    @Test
    public void testThatSearchResultIsProperlyLocalized() {
        LocaleManager.instance().setLanguage("en");

        Call<SearchResult> wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_2));
        SearchResult searchResult = RetrofitHelper.get(wrapper);

        TileItem item = searchResult.getItemWrappers().get(0).getTileItem();

        assertTrue("Contains english localization", item.getViewCountText().contains("views"));

        LocaleManager.instance().setLanguage("ru");

        wrapper = mSearchManagerUnsigned.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_2));
        searchResult = RetrofitHelper.get(wrapper);

        item = searchResult.getItemWrappers().get(0).getTileItem();

        assertTrue("Contains russian localization", item.getViewCountText().contains("просмотров"));
    }
}