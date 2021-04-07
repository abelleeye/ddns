package cn.rockst.ddns.util;

import java.util.Objects;

public class IOUtil {

    public static void streamClose(AutoCloseable... closeables){
        for (AutoCloseable closeable : closeables) {
            if (Objects.nonNull(closeable)) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
