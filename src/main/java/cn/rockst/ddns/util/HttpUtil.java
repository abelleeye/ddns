package cn.rockst.ddns.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpUtil {

    public static String get(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .setConnectTimeout(60000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.streamClose(response, httpclient);
        }
        throw new RuntimeException("http request error!");
    }
}
