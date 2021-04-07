package cn.rockst.ddns.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONUtil {

    public static void printJsonTree(Object object) {
        printJsonTree(null, object);
    }

    public static void printJsonTree(String name, Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("{}\r\n{}", name, gson.toJson(object));
    }

}
