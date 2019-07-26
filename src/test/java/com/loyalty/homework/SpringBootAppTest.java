package com.loyalty.homework;

import com.loyalty.homework.controllers.RestApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Integration")
public class SpringBootAppTest {

    @Autowired
    private RestApiController restApi;


    @Test
    public void contextLoads() {
        assertThat(restApi).isNotNull();
    }

}