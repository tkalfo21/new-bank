package ch.bbw.web;

import ch.bbw.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test void sparkontoKannPerHttpErstelltWerden() throws Exception {
        mvc.perform(post("/accounts/savings"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test void leeresKontoLiefertSaldoNull() throws Exception {
        mvc.perform(get("/bank/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test void unbekanntesKontoLiefert404() throws Exception {
        mvc.perform(get("/accounts/S-unbekannt"))
                .andExpect(status().isNotFound());
    }

    @Test void beideRanglistenLiefernLeereArraysBeiLeeremBestand() throws Exception {
        mvc.perform(get("/bank/top5/highest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
        mvc.perform(get("/bank/top5/lowest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

private String kontoEroeffnen(String pfad, String eingabe) throws Exception {
        var anfrage = post(pfad);
        if (eingabe != null) {
            anfrage.contentType(APPLICATION_JSON).content(eingabe);
        }
        String json = mvc.perform(anfrage)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        return json.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
    }

@Test @DisplayName("UC-01: Konto eröffnen und Anfangssaldo über HTTP prüfen")
    void kontoEroeffnen() throws Exception {
        String id = kontoEroeffnen("/accounts/savings", null);
        mvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value("SavingsAccount"))
                .andExpect(jsonPath("$.balance").value(0));
        mvc.perform(get("/accounts/{id}/balance", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test @DisplayName("UC-02: Einzahlung über HTTP erstellt eine Buchung")
    void einzahlung() throws Exception {
        String id = kontoEroeffnen("/accounts/savings", null);
        mvc.perform(post("/accounts/{id}/deposit", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":1250,\"text\":\"Lohn\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1250));
        mvc.perform(get("/accounts/{id}/bookings", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1250))
                .andExpect(jsonPath("$[0].text").value("Lohn"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test @DisplayName("UC-03: Lohnkonto erlaubt Kreditlimite und verbietet Überschreitung")
    void lohnkontoAuszahlung() throws Exception {
        String id = kontoEroeffnen("/accounts/salary", "{\"creditLimit\":-500}");
        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(-500));
        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
        mvc.perform(get("/accounts/{id}/bookings", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(-500))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test @DisplayName("UC-03: Jugendsparkonto hält die Monatslimite auch über HTTP ein")
    void jugendkontoAuszahlung() throws Exception {
        String id = kontoEroeffnen("/accounts/promo-youth", null);
        mvc.perform(post("/accounts/{id}/deposit", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":30000000}"))
                .andExpect(status().isOk());
        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":20000000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(10000000));
        mvc.perform(post("/accounts/{id}/withdraw", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":1}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/accounts/{id}/balance", id))
                .andExpect(jsonPath("$.balance").value(10000000));
    }

    @Test @DisplayName("UC-04: Unbekanntes Konto liefert HTTP 404")
    void kontoNichtGefunden() throws Exception {
        mvc.perform(get("/accounts/S-unbekannt"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test @DisplayName("Ungültiger Betrag und Datum liefern HTTP 400 ohne Buchung")
    void ungueltigeAnfragen() throws Exception {
        String id = kontoEroeffnen("/accounts/savings", null);
        mvc.perform(post("/accounts/{id}/deposit", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":0}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/accounts/{id}/deposit", id)
                .contentType(APPLICATION_JSON)
                .content("{\"amount\":100,\"date\":\"kein-datum\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/accounts/{id}/bookings", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
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

    @Test void kreditlimiteNullIstErlaubt() throws Exception {
        mvc.perform(post("/accounts/salary").contentType(APPLICATION_JSON)
                .content("{\"creditLimit\":0}"))
                .andExpect(status().isCreated());
    }

    @Test void einzahlungMitBetragNullWirdAbgelehnt() throws Exception {
        String json = mvc.perform(post("/accounts/savings"))
                .andReturn().getResponse().getContentAsString();
        String id = json.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
        mvc.perform(post("/accounts/{id}/deposit", id).contentType(APPLICATION_JSON)
                .content("{\"amount\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test void ungueltigesDatumWirdAbgelehnt() throws Exception {
        String json = mvc.perform(post("/accounts/savings"))
                .andReturn().getResponse().getContentAsString();
        String id = json.replaceAll(".*\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
        mvc.perform(post("/accounts/{id}/deposit", id).contentType(APPLICATION_JSON)
                .content("{\"amount\":100,\"date\":\"kein-datum\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void ungueltigesJsonWirdAbgelehnt() throws Exception {
        mvc.perform(post("/accounts/salary").contentType(APPLICATION_JSON)
                .content("{kaputt"))
                .andExpect(status().isBadRequest());
    }
}
