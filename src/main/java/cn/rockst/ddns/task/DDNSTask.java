package cn.rockst.ddns.task;

import cn.rockst.ddns.config.AppConfig;
import cn.rockst.ddns.util.DDNSUtil;
import cn.rockst.ddns.util.IPUtil;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DDNSTask extends TimerTask {

    private final IAcsClient client;

    private final List<AppConfig.Domain> domains;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public DDNSTask(IAcsClient client, List<AppConfig.Domain> domains) {
        this.client = client;
        this.domains = domains;
    }

    @Override
    public void run() {
        domains.forEach(d -> {
            String domain = d.getDomain();
            Future<Object> domainTask = executor.submit(() -> {
                List<AppConfig.Domain.RRKeyWord> rrKeyWords = d.getRrKeyWords();
                Map<String, AppConfig.Domain.RRKeyWord> rrKeyWordMap = rrKeyWords.stream().collect(
                        Collectors.toConcurrentMap(AppConfig.Domain.RRKeyWord::getRrKeyWord, r -> r));

                // 查询指定主域名的最新解析记录
                DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
                // 主域名
                describeDomainRecordsRequest.setDomainName(domain);
                DescribeDomainRecordsResponse describeDomainRecordsResponse = DDNSUtil.describeDomainRecords(describeDomainRecordsRequest, client);
                DDNSUtil.logPrint("describeDomainRecords", describeDomainRecordsResponse);

                List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
                List<Future<Object>> rrTasks = new ArrayList<>();
                Set<String> rrSet = domainRecords.stream().map(DescribeDomainRecordsResponse.Record::getRR).collect(Collectors.toSet());

                // 如果返回列表不包含配置文件中【主机记录】的就添加
                rrKeyWordMap.forEach((rrKeyWord, rr) -> {
                    if (!rrSet.contains(rrKeyWord)) {
                        // 解析类型
                        String type = rr.getType();
                        // 当前主机公网IP
                        String currentHostIP = IPUtil.getCurrentHostIP();
                        AddDomainRecordRequest addDomainRecordRequest = new AddDomainRecordRequest();
                        addDomainRecordRequest.setDomainName(domain);
                        addDomainRecordRequest.setRR(rrKeyWord);
                        addDomainRecordRequest.setType(type);
                        addDomainRecordRequest.setValue(currentHostIP);
                        AddDomainRecordResponse addDomainRecordResponse = DDNSUtil.addDomainRecord(addDomainRecordRequest, client);
                        DDNSUtil.logPrint("addDomainRecord", addDomainRecordResponse);
                    }
                });

                // 如果返回列表包含配置文件中【主机记录】的就通过判断是否进行更新解析
                domainRecords.forEach(record -> {
                    if (rrKeyWordMap.containsKey(record.getRR())) {
                        AppConfig.Domain.RRKeyWord rrKeyWord = rrKeyWordMap.get(record.getRR());
                        // 记录ID
                        String recordId = record.getRecordId();
                        // 记录值
                        String recordsValue = record.getValue();
                        // 解析类型
                        String type = record.getType();
                        // 当前主机公网IP
                        String currentHostIP = IPUtil.getCurrentHostIP();

                        // 如果ip变动或者解析记录类型变动
                        if(!currentHostIP.equals(recordsValue) || !type.equals(rrKeyWord.getType())) {
                            Future<Object> rf = executor.submit(() -> {
                                UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
                                // 主机记录
                                updateDomainRecordRequest.setRR(rrKeyWord.getRrKeyWord());
                                // 记录ID
                                updateDomainRecordRequest.setRecordId(recordId);
                                // 将主机记录值改为当前主机IP
                                updateDomainRecordRequest.setValue(currentHostIP);
                                // 解析记录类型
                                updateDomainRecordRequest.setType(rrKeyWord.getType());
                                UpdateDomainRecordResponse updateDomainRecordResponse = DDNSUtil.updateDomainRecord(updateDomainRecordRequest, client);
                                DDNSUtil.logPrint("updateDomainRecord", updateDomainRecordResponse);
                                return true;
                            });
                            rrTasks.add(rf);
                        }
                    }
                });
                sync(rrTasks);
                return true;
            });
            sync(domainTask);
        });
    }

    private void sync(List<Future<Object>> futures) {
        futures.forEach(this::sync);
    }

    private void sync(Future<Object> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
