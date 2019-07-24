package com.loyalty.homework.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestApi.class)
public class RestApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCloneReturnsText() throws Exception {
        mvc.perform(post("/api/v1/clone").content("something"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("something")));

    }

    @Test
    public void testCloneEmptyReturns4xx() throws Exception {
        mvc.perform(post("/api/v1/clone").content(""))
                .andExpect(status().is4xxClientError());

    }

}