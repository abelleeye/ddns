# DDNS
aliyun ddns

# [DDNS](https://github.com/AbelLee-LiYe/ddns)


## Feature

- 解析方式
* [x] 多主域名解析
* [x] 多主机记录解析
* [x] 任务轮询间隔可配置
* [x] 当解析主机记录未设置过，自动创建
* [x] 异步线程同时解析
* [x] jsonip.com低延迟获取公网IP
* [x] docker-compose一键启动


## 安装


## 配置

accessKeyId 和 secret 获取方式： 阿里云 -> 控制台 -> 右上角头像 -> AccessKey 管理

```json5
{
  "regionId": "cn-hangzhou",  //可用区域
  "accessKeyId": "accessKeyId", 
  "secret": "secret",  
  "interval": 10000, // 毫秒
  "domains": [
    {
      "domain": "example.com", // 主域名
      "rrKeyWords": [
        {
          "rrKeyWord": "pre", // 主机记录 pre.example.com
          "type": "A"
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


