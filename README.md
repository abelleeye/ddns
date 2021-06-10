# [DDNS](https://github.com/AbelLee-LiYe/ddns)
aliyun ddns

基于openjdk11开发

## Feature
* [x] 多主域名解析
* [x] 多主机记录解析
* [x] 任务轮询间隔可配置
* [x] 当解析主机记录未设置过，自动创建
* [x] 异步线程同时解析
* [x] jsonip.com低延迟获取公网IP
* [x] docker-compose一键启动


## 安装

部署目录结构:
```
- ddns 部署目录
-- config.json
-- docker-compose.yml 
```

1. 下载config.json, `wget https://raw.githubusercontent.com/abelleeye/ddns/main/config.json`
2. 下载docker-compose.yml, `wget https://raw.githubusercontent.com/abelleeye/ddns/main/docker-compose.yml`
3. 修改config.json配置文件
3. 执行 `docker-compose up -d`

## 配置

accessKeyId 和 secret 获取方式： 阿里云 -> 控制台 -> 右上角头像 -> AccessKey 管理

[可用区域](https://help.aliyun.com/document_detail/40654.html)

```json5
{
  "regionId": "cn-hangzhou",  // 可用区域 
  "accessKeyId": "accessKeyId", 
  "secret": "secret",  
  "interval": 10000, // 毫秒
  "domains": [
    {
      "domain": "example.com", // 主域名
      "rrKeyWords": [
        {
          "rrKeyWord": "pre", // 主机记录 pre.example.com
          "type": "A" // 解析记录类型
        },
        {
          "rrKeyWord": "example",
          "type": "A"
        }
      ]
    }
  ]
}
```


