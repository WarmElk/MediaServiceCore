package com.liskovsoft.youtubeapi.videoinfo;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApiHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoFormat2;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;

import java.util.ArrayList;
import java.util.List;

public abstract class VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceBase.class.getSimpleName();
    private final DashInfoApi mDashInfoApi;
    protected final AppService mAppService;

    protected VideoInfoServiceBase() {
        mAppService = AppService.instance();
        mDashInfoApi = RetrofitHelper.withRegExp(DashInfoApi.class);
    }

    protected void decipherFormats(List<? extends VideoFormat> formats) {
        if (formats == null) {
            return;
        }

        List<String> ciphered = extractCipheredStrings(formats);
        List<String> deciphered = mAppService.decipher(ciphered);
        applyDecipheredStrings(deciphered, formats);

        List<String> throttled = extractThrottledStrings(formats);
        List<String> throttleFixed = mAppService.throttleFix(throttled);
        applyThrottleFixedStrings(throttleFixed, formats);

        applyAdditionalStrings(formats);
    }

    private static List<String> extractCipheredStrings(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getSignatureCipher());
        }

        return result;
    }

    private static void applyDecipheredStrings(List<String> deciphered, List<? extends VideoFormat> formats) {
        if (deciphered.size() != formats.size()) {
            throw new IllegalStateException("Sizes of formats and deciphered strings should match!");
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setSignature(deciphered.get(i));
        }
    }

    private static List<String> extractThrottledStrings(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getThrottleCipher());
            // All throttled strings has same values
            break;
        }

        return result;
    }

    private static void applyThrottleFixedStrings(List<String> throttleFixed, List<? extends VideoFormat> formats) {
        if (throttleFixed.isEmpty()) {
            return;
        }

        // All throttled strings has same values
        boolean sameSize = throttleFixed.size() == formats.size();

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setThrottleCipher(throttleFixed.get(sameSize ? i : 0));
        }
    }

    private static void applyAdditionalStrings(List<? extends VideoFormat> formats) {
        String cpn = AppService.instance().getClientPlaybackNonce();

        for (VideoFormat format : formats) {
            format.setCpn(cpn);
            format.setClientVersion(AppConstants.CLIENT_VERSION_WEB);
            //format.setParam("alr", "yes");
        }
    }

    protected DashInfo getDashInfo(String url) {
        if (url == null) {
            return null;
        }

        return RetrofitHelper.get(mDashInfoApi.getDashInfoUrl(url));
    }

    protected DashInfo getDashInfo2(VideoInfo videoInfo) {
        if (videoInfo == null || videoInfo.getAdaptiveFormats() == null || videoInfo.getAdaptiveFormats().isEmpty()) {
            return null;
        }

        //AdaptiveVideoFormat format = getSmallestAudio(videoInfo);
        //
        //DashInfo dashInfo;
        //
        //try {
        //    dashInfo = new DashInfoFormat2(mDashInfoApi.getDashInfoFormat2(format.getUrl()));
        //} catch (ArithmeticException | NumberFormatException ex) {
        //    // Empty results received. Url isn't available or something like that
        //    dashInfo = RetrofitHelper.get(mDashInfoApi.getDashInfoFormat(format.getUrl()));
        //}
        
        DashInfo dashInfo;

        try {
            AdaptiveVideoFormat format = getSmallestAudio(videoInfo);
            dashInfo = new DashInfoFormat2(mDashInfoApi.getDashInfoFormat2(format.getUrl()));
        } catch (ArithmeticException | NumberFormatException ex) {
            // Segment isn't available
            AdaptiveVideoFormat format = getSmallestVideo(videoInfo);
            dashInfo = new DashInfoFormat2(mDashInfoApi.getDashInfoFormat2(format.getUrl()));
        }

        return dashInfo;
    }

    @NonNull
    private AdaptiveVideoFormat getSmallestAudio(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isAudio(item.getMimeType())); // smallest format

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.throttleFix(format.getThrottleCipher()));
        return format;
    }

    @NonNull
    private AdaptiveVideoFormat getSmallestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findLast(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // smallest format

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.throttleFix(format.getThrottleCipher()));
        return format;
    }

    @NonNull
    private AdaptiveVideoFormat getLargestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // first is largest

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.throttleFix(format.getThrottleCipher()));
        return format;
    }
}
