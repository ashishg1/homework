package com.loyalty.homework.controllers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.loyalty.homework.db.DBSupplier;
import com.loyalty.homework.db.DynamoDBLocalFixture;
import com.loyalty.homework.db.DynamoDBOperations;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
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


@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@WebMvcTest(RestApiController.class)
@ActiveProfiles("Integration")
public class RestApiControllerTest {

    private static final String TEST_POST = "{\"message\":\"Test Post\",\"city\":\"Toronto,CA\",\"userName\":\"ashish\"}";
    private static final String TEST_REPLY1 = "{\"message\":\"Test Post Reply\",\"userName\":\"shubha\",\"city\":\"Toronto,CA\"}";
    private static final String TEST_REPLY2 = "{\"message\":\"Test Reply Reply\",\"userName\":\"shubha\",\"city\":\"Toronto,CA\"}";
    private static final String TEST_POST2 = "{\"message\":\"Test Post 2\",\"city\":\"Toronto,CA\",\"userName\":\"ashish\"}";

    @Autowired
    private MockMvc mvc;

    @BeforeClass
    public static void setUpBeforeClass() {
        final AmazonDynamoDB dynamoDB = new DynamoDBLocalFixture().startDynamoDB();
        DBSupplier.setDynamoDB(dynamoDB, "testEnv");
    }

    @Before
    public void setUp() throws Exception {
        new DynamoDBOperations(new DBSupplier()).createTables(true);
    }


    @Test
    public void testPostIsSaved() throws Exception {
        final MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").header("Content-Type", "application/json").content(TEST_POST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvc.perform(get("/api/v1/users/ashish/posts/" + jsonObject.get("messageId")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].userName", is("ashish")))
                .andExpect(jsonPath("$[0].messageId", is(jsonObject.get("messageId"))))
                .andExpect(jsonPath("$[0].message", is("Test Post")));

    }

    @Test
    public void testNextPostIsRetrieved() throws Exception {
        mvc.perform(post("/api/v1/users/ashish/post/next").header("Content-Type", "application/json").content(TEST_POST));
        //TODO: This method should return the next posts. It is not implemented right now.
    }

    @Test
    public void testSecondPostIsAboveFirst() throws Exception {
        mvc.perform(post("/api/v1/users/ashish/post").header("Content-Type", "application/json").content(TEST_POST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        mvc.perform(post("/api/v1/users/ashish/post").header("Content-Type", "application/json").content(TEST_POST2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post 2")))
                .andReturn();

        mvc.perform(get("/api/v1/users/ashish/posts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].message", is("Test Post 2")))
                .andExpect(jsonPath("$[1].message", is("Test Post")));

    }

    @Test
    public void testReplyToPostIsSaved() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").header("Content-Type", "application/json").content(TEST_POST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject post = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/reply").header("Content-Type", "application/json").content(TEST_REPLY1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Post Reply")))
                .andExpect(jsonPath("messageDepth", is(1)))
                .andReturn();

        final JSONObject reply = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvc.perform(get("/api/v1/users/ashish/posts/" + post.get("messageId")))
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
        MvcResult mvcResult = mvc.perform(post("/api/v1/users/ashish/post").header("Content-Type", "application/json").content(TEST_POST))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("userName", is("ashish")))
                .andExpect(jsonPath("message", is("Test Post")))
                .andReturn();

        final JSONObject post = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/reply").header("Content-Type", "application/json").content(TEST_REPLY1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Post Reply")))
                .andExpect(jsonPath("messageDepth", is(1)))
                .andReturn();

        final JSONObject reply = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvcResult = mvc.perform(post("/api/v1/posts/" + post.get("messageId") + "/" + reply.get("messageId") + "/reply").header("Content-Type", "application/json").content(TEST_REPLY2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("rootMessageId", is(post.get("messageId"))))
                .andExpect(jsonPath("message", is("Test Reply Reply")))
                .andExpect(jsonPath("messageDepth", is(2)))
                .andReturn();

        final JSONObject reply2 = (JSONObject) new JSONParser().parse(mvcResult.getResponse().getContentAsString());

        mvc.perform(get("/api/v1/users/ashish/posts/" + post.get("messageId")))
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