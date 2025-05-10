package pl.fundraising.charity.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.fundraising.charity.exception.RecordNotFoundException;
import pl.fundraising.charity.model.response.FinancialReportResponse;
import pl.fundraising.charity.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinancialReportController.class)
public class FinancialReportControllerTest {

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFinancialReportShouldReturn200withProperResponse() throws Exception {
        List<FinancialReportResponse> response = List.of(
                new FinancialReportResponse("TestCharity",
                        BigDecimal.ZERO, "PLN")
        );

        when(accountService.getAccountsFinancialReport()).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/financial-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].charityName").value("TestCharity"))
                .andExpect(jsonPath("$[0].balance").value(0))
                .andExpect(jsonPath("$[0].currency").value("PLN"));

        verify(accountService).getAccountsFinancialReport();
    }

    @Test
    void getFinancialReportShouldReturn404withProperMessage() throws Exception {
        String errorMessage = "Cannot generate event while no events registered";
        when(accountService.getAccountsFinancialReport()).thenThrow(
                new RecordNotFoundException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/financial-report"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));

        verify(accountService).getAccountsFinancialReport();
    }

    @Test
    @DisplayName("PreventWhiteJson")
    void testNoResourceFoundToPreventWhiteJson() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/finance"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Resource not found"));
    }

    @Test
    @DisplayName("PreventNoMethodSupported")
    void testMethodNotSupported() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/api/financial-report"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Request method 'PUT' is not supported"));
    }
}
