package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.next.v2.gen.*

data class NextMediaItemImpl(var nextVideoItem: NextVideoItem): BaseMediaItemImpl() {
    override val videoIdItem by lazy { nextVideoItem.getVideoId() }
    override val channelIdItem: String? = null
    override val titleItem by lazy { nextVideoItem.getTitle() }
    override val infoItem by lazy { YouTubeHelper.createInfo(nextVideoItem.getAuthor()) ?: null }
    override val cardThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val playlistIdItem by lazy { nextVideoItem.getPlaylistId() }
    override val playlistIndexItem by lazy { nextVideoItem.getPlaylistIndex() }
    override val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(videoIdItem) ?: null }
    override val playlistParamsItem by lazy { nextVideoItem.getParams() }
}
