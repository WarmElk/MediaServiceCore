package com.liskovsoft.youtubeapi.app.models.clientdata;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

/**
 * Data contained withing m=base js file<br/>
 * NOTE: Same pattern could be encountered 3 times or more<br/>
 * We're using the first one.
 */
public class ModernClientData implements ClientData {
    // We need first occurrence (Android TV)
    @RegExp({
            "var [$\\w]+=\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\n?[$\\w]+=\"\\w+\"",
            "\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[$\\w]+=\"\\w+\"",
            "clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",mi:\"\\w+\""
    })
    private String mClientId;

    // We need first occurrence (Android TV)
    @RegExp({
            "var [$\\w]+=\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\n?[$\\w]+=\"(\\w+)\"",
            "\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[$\\w]+=\"(\\w+)\"",
            "clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",mi:\"(\\w+)\""
    })
    private String mClientSecret;

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }
}
