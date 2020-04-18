package com.morbis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MorbisApplication {

    public static Logger logger = LoggerFactory.getLogger(MorbisApplication.class);

    static {
        System.setProperty("spring.mail.username", "test");
        System.setProperty("spring.mail.password", "test");
        System.setProperty("spring.mail.host.address", "test");
    }

    public static void main(String[] args) {
        SpringApplication.run(MorbisApplication.class, args);
        logger.error("fefedefefe");
    }
}
