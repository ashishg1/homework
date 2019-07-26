package com.loyalty.homework.controllers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.db.DBSupplier;
import com.loyalty.homework.db.DynamoDBLocalFixture;
import com.loyalty.homework.db.DynamoDBOperations;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(RestApiController.class)
@ActiveProfiles("Integration")
public class RestApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @BeforeClass
    public static void setUp() throws Exception {
        final AmazonDynamoDB dynamoDB = new DynamoDBLocalFixture().startDynamoDB();
        DBSupplier.setDynamoDB(dynamoDB, "testEnv");
    }


    @Test
    public void testPostIsSaved() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);
        final MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());


        mvc.perform(get("/api/v1/users/ashish/posts/" + jsonObject.get("messageId")).content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].userName", is("ashish")))
                .andExpect(jsonPath("$[0].messageId", is(jsonObject.get("messageId"))))
                .andExpect(jsonPath("$[0].message", is("Test Post")));

    }

    @Test
    public void testReplyToPostIsSaved() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);

        MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject post = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/reply").content("Test Post Reply"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Post Reply")))
                .andExpect(jsonPath("messageDepth", is(1)))
                .andReturn();

        final JSONObject reply = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvc.perform(get("/api/v1/users/ashish/posts/" + post.get("messageId")).content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].userName", is("ashish")))
                .andExpect(jsonPath("$[0].messageId", is(post.get("messageId"))))
                .andExpect(jsonPath("$[0].message", is("Test Post")))
                .andExpect(jsonPath("$[1].rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("$[1].messageId", is(reply.get("messageId"))))
                .andExpect(jsonPath("$[1].message", is("Test Post Reply")))
                .andExpect(jsonPath("$[1].messageDepth", is(1)))
                .andReturn();

    }

    @Test
    public void testReplyToReplyIsSaved() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);

        MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject post = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/reply").content("Test Post Reply"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Post Reply")))
                .andExpect(jsonPath("messageDepth", is(1)))
                .andReturn();

        final JSONObject reply = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/" + reply.get("messageId") + "/reply").content("Test Reply Reply"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Reply Reply")))
                .andExpect(jsonPath("messageDepth", is(2)))
                .andReturn();

        final JSONObject reply2 = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvc.perform(get("/api/v1/users/ashish/posts/" + post.get("messageId")).content("Test Post"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].userName", is("ashish")))
                .andExpect(jsonPath("$[0].messageId", is(post.get("messageId"))))
                .andExpect(jsonPath("$[0].message", is("Test Post")))
                .andExpect(jsonPath("$[1].rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("$[1].messageId", is(reply.get("messageId"))))
                .andExpect(jsonPath("$[1].message", is("Test Post Reply")))
                .andExpect(jsonPath("$[1].messageDepth", is(1)))
                .andExpect(jsonPath("$[2].rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("$[2].messageId", is(reply2.get("messageId"))))
                .andExpect(jsonPath("$[2].message", is("Test Reply Reply")))
                .andExpect(jsonPath("$[2].messageDepth", is(2)))
                .andReturn();

    }

}