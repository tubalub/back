package tubalubback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.MvcResult;
import tubalubback.services.SyncService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER1 = "user1";

    @BeforeEach
    void setUp() {
        SyncService.userSet.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser() throws Exception {
        mockMvc.perform(put("/user").content(USER1));
        assertTrue(SyncService.userSet.contains(USER1));
    }

    @Test
    void deleteUser() throws Exception {
        SyncService.userSet.add(USER1);
        mockMvc.perform(delete("/user/" + USER1));
        assertEquals(0, SyncService.userSet.size());
    }

    @Test
    void getAllUsers() throws Exception {
        SyncService.userSet.add("test1");
        SyncService.userSet.add("test2");

        MvcResult result = mockMvc.perform(get("/users")).andReturn();
        MockHttpServletResponse resp = result.getResponse();
        assertEquals(resp.getStatus(), 200);
        assertEquals(objectMapper.writeValueAsString((SyncService.userSet)),resp.getContentAsString());
    }
}