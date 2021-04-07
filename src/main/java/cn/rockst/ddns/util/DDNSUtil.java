package cn.rockst.ddns.util;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DDNSUtil {

    /**
     * 获取主域名的所有解析记录列表
     */
    public static DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request, IAcsClient client){
        return request(request, client);
    }

    /**
     * 添加解析记录
     */
    public static AddDomainRecordResponse addDomainRecord(AddDomainRecordRequest request, IAcsClient client) {
        return request(request, client);
    }

    /**
     * 修改解析记录
     */
    public static UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request, IAcsClient client){
        return request(request, client);
    }

    public static <T extends AcsResponse> T request(AcsRequest<T> request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("发生调用错误，抛出运行时异常", e);
            // 发生调用错误，抛出运行时异常
            throw new RuntimeException();
        }
    }

    public static void logPrint(String functionName, Object result) {
        Gson gson = new Gson();
        log.info("-------------------------------{}-------------------------------", functionName);
        log.info(gson.toJson(result));
    }

}
