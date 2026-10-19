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
class BankValidationIntegrationTests {
    @Autowired private WebApplicationContext context;
    private MockMvc mvc;
    @BeforeEach void prepare() {
        Bank.getInstance().resetForTests();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test void negativeAmountAndPositiveCreditLimitFail() throws Exception {
        mvc.perform(post("/accounts/salary").contentType(APPLICATION_JSON)
                .content("{\"creditLimit\":500}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists());
        String json = mvc.perform(post("/accounts/savings")).andReturn().getResponse().getContentAsString();
        String id = json.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
        mvc.perform(post("/accounts/{id}/deposit", id).contentType(APPLICATION_JSON)
                .content("{\"amount\":-500}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").exists());
    }
}
