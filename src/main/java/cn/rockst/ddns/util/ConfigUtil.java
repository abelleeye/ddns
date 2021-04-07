package cn.rockst.ddns.util;

import cn.rockst.ddns.config.AppConfig;
import cn.rockst.ddns.constant.Constants;
import cn.rockst.ddns.exception.AppConfigException;
import cn.rockst.ddns.holder.AppConfigHolder;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

@Slf4j
public class ConfigUtil {

    public static String getAppConfigFilePath() {
        return System.getProperty(Constants.APP_CONFIG_PATH_KEY, Constants.DEFAULT_APP_CONFIG_PATH);
    }

    public static AppConfig loadAppConfig() {
        File file = new File(getAppConfigFilePath());
        System.out.println(file.getAbsolutePath());
        try (
                FileInputStream fileInputStream = new FileInputStream(getAppConfigFilePath());
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                JsonReader jsonReader = new JsonReader(inputStreamReader);
            ) {
            Gson gson = new Gson();
            return gson.fromJson(jsonReader, AppConfig.class);
        } catch (IOException e) {
            log.error("loading config file error!", e);
        }
        throw new AppConfigException("loading config file error!");
    }

    public static void validAppConfig() {
        AppConfig appConfig = AppConfigHolder.getInstance();

        List<AppConfig.Domain> domains = appConfig.getDomains();

        if (domains == null || domains.isEmpty()) {
            throw new AppConfigException("【配置文件】domains不可为空!");
        }

        if (domains.stream().anyMatch(domain -> domain.getRrKeyWords() == null || domain.getRrKeyWords().isEmpty())) {
            throw new AppConfigException("【配置文件】rrKeyWords集合不可为空!");
        }
    }

    public static void printConfig() {
        JSONUtil.printJsonTree("app config:", AppConfigHolder.getInstance());
    }
}
