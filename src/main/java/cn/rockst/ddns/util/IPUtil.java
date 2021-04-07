package cn.rockst.ddns.util;

import com.google.gson.JsonParser;

public class IPUtil {

    /**
     * 获取当前主机公网IP
     */
    public static String getCurrentHostIP(){
        return new JsonParser().parse(
                HttpUtil.get("https://jsonip.com/")
        ).getAsJsonObject().get("ip").getAsString();
    }
}
