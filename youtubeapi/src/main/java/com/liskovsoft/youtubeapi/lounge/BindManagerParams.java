package com.liskovsoft.youtubeapi.lounge;

public class BindManagerParams {
    public static final String SCREEN_UID = "2a026ce9-4429-4c5e-8ef5-0101eddf5671";
    public static final String ZX = "xxxxxxxxxxxx";
    public static final String RID = "1337";
    public static final String AID = "42";
    public static final String APP = "lb-v4";
    public static final String ACCESS_TYPE = "permanent";
    private static final String BASE_BIND_URL = "https://www.youtube.com/api/lounge/bc/bind?" +
            "device=LOUNGE_SCREEN&theme=cl&capabilities=dsp%2Cmic%2Cdpa&mdxVersion=2&VER=8&v=2&t=1" +
            "&app=" + BindManagerParams.APP +
            "&id=" + BindManagerParams.SCREEN_UID +
            "&AID=" + BindManagerParams.AID +
            "&zx=" + BindManagerParams.ZX;
    public static final String BIND_DATA_URL = BASE_BIND_URL + "&RID=" + BindManagerParams.RID;
    private static final String BIND_RPC_URL = BASE_BIND_URL + "&RID=rpc&CI=0";

    public static String createBindRpcUrl(String screenName,
                                          String loungeToken,
                                          String sessionId,
                                          String gSessionId) {
        return String.format("%s&name=%s&loungeIdToken=%s&SID=%s&gsessionid=%s",
                BIND_RPC_URL,
                screenName,
                loungeToken,
                sessionId,
                gSessionId);
    }
}
