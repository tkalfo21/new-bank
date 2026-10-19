package ch.bbw.web;

import ch.bbw.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
class BankHttpIntegrationTests {
    @Autowired private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach void setup() {
        Bank.getInstance().resetForTests();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test void createDepositWithdrawAndErrorResponses() throws Exception {
        String json = mvc.perform(post("/accounts/savings"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        String id = json.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");

        mvc.perform(post("/accounts/{id}/deposit", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":5000,\"text\":\"Lohn\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(5000));

        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":2000,\"text\":\"Einkauf\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(3000));

        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":4000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        mvc.perform(get("/accounts/{id}/bookings", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Lohn"))
                .andExpect(jsonPath("$[1].text").value("Einkauf"));
        mvc.perform(get("/accounts/non-existing"))
                .andExpect(status().isNotFound());
    }
}
