package com.morbis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MorbisApplicationTests {

    static {
        System.setProperty("spring.mail.username", "test");
        System.setProperty("spring.mail.password", "test");
        System.setProperty("spring.mail.host.address", "test");
    }

    @Test
    void contextLoads() {

    }

}
