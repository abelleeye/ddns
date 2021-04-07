package cn.rockst.ddns.config;

import lombok.Data;

import java.util.List;

@Data
public class AppConfig {

    private String regionId;

    private String accessKeyId;

    private String secret;

    private Integer interval;

    private List<Domain> domains;


    @Data
    public static class Domain {

        private String domain;

        private List<RRKeyWord> rrKeyWords;


        @Data
        public static class RRKeyWord {

            private String rrKeyWord;

            private String type;
        }
    }

}
