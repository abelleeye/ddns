package cn.rockst.ddns.holder;

import cn.rockst.ddns.config.AppConfig;
import cn.rockst.ddns.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppConfigHolder {

    private volatile static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfigHolder.class) {
                if (instance == null) {
                    instance = ConfigUtil.loadAppConfig();
                    ConfigUtil.printConfig();
                    ConfigUtil.validAppConfig();
                }
            }
        }
        return instance;
    }
}
