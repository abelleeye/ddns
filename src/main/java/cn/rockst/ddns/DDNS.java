package cn.rockst.ddns;

import cn.rockst.ddns.config.AppConfig;
import cn.rockst.ddns.constant.Constants;
import cn.rockst.ddns.holder.AppConfigHolder;
import cn.rockst.ddns.task.DDNSTask;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Timer;

@Slf4j
public class DDNS {

    public static void main(String[] args) {
        // 设置配置文件路径
        System.setProperty(Constants.APP_CONFIG_PATH_KEY, args.length != 0 ? args[0] : Constants.DEFAULT_APP_CONFIG_PATH);
        // 获取配置对象
        AppConfig appConfig = AppConfigHolder.getInstance();
        List<AppConfig.Domain> domains = appConfig.getDomains();

        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                appConfig.getRegionId(), // 地域ID
                appConfig.getAccessKeyId(), // 您的AccessKey ID
                appConfig.getSecret()); // 您的AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);

        // 开始任务
        Timer timer = new Timer();
        timer.schedule(new DDNSTask(client, domains), 0, appConfig.getInterval());
    }

}
