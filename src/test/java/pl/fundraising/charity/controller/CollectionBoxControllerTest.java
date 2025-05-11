package pl.fundraising.charity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.fundraising.charity.entity.CollectionBox;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.exception.*;
import pl.fundraising.charity.model.request.AssignRequest;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.service.CollectionBoxService;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollectionBoxController.class)
public class CollectionBoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectionBoxService boxService;

    @MockitoBean
    private CurrencyRepository currencyRepository;

    @Test
    void getBoxStatusShouldReturn200withBoxesResponse() throws Exception {
        CollectionBoxResponse testBox = new CollectionBoxResponse(1L, true, false);

        when(boxService.getAllBoxesInfo()).thenReturn(List.of(testBox));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/box"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].boxId").value(1))
                .andExpect(jsonPath("$[0].assigned").value(true))
                .andExpect(jsonPath("$[0].empty").value(false));

        verify(boxService).getAllBoxesInfo();


    }

    @Test
    void getBoxStatusShouldReturn404withErrorResponse() throws Exception {
        when(boxService.getAllBoxesInfo()).thenThrow(new RecordNotFoundException("No boxes available"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/box"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("No boxes available"));

        verify(boxService).getAllBoxesInfo();
    }

    @Test
    void createNewEmptyBoxShouldReturn200withProperMessage() throws Exception {
        when(boxService.createNewBox()).thenReturn(new CollectionBox());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/box"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("New Empty box created"));
    }

    @Test
    void deleteDamagedBoxShouldReturn200withProperMessage() throws Exception {
        doNothing().when(boxService).deleteBox(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/box/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has been removed")));

        verify(boxService).deleteBox(anyLong());

    }

    @Test
    void deleteDamagedBoxShouldReturn404WhenNoBoxToBeDeleted() throws Exception {
        doThrow(new RecordNotFoundException("No box to be deleted")).when(boxService).deleteBox(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/box/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("No box to be deleted"));

        verify(boxService).deleteBox(anyLong());
    }

    @Test
    void assignBoxToFundraisingEventShouldReturn200withProperMessage() throws Exception {
        doNothing().when(boxService).assignBoxToEvent(anyLong(), anyLong());
        AssignRequest request = new AssignRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/assign", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has been assigned to event")));

        verify(boxService).assignBoxToEvent(anyLong(), anyLong());
    }

    @Test
    void assignBoxToFundraisingEventShouldReturn404boxNotFound() throws Exception {
        doThrow(new RecordNotFoundException("Box not found")).when(boxService).assignBoxToEvent(anyLong(), anyLong());
        AssignRequest request = new AssignRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/assign", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Box not found"));

        verify(boxService).assignBoxToEvent(anyLong(), anyLong());
    }

    @Test
    void assignBoxToFundraisingEventShouldReturn404eventNotFound() throws Exception {
        doThrow(new RecordNotFoundException("Event not found")).when(boxService).assignBoxToEvent(anyLong(), anyLong());
        AssignRequest request = new AssignRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/assign", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Event not found"));

        verify(boxService).assignBoxToEvent(anyLong(), anyLong());
    }

    @Test
    void assignBoxToFundraisingEventShouldReturn409withProperMessage() throws Exception {
        doThrow(new BoxAlreadyAssignedException("Box is already assigned"))
                .when(boxService).assignBoxToEvent(anyLong(), anyLong());
        AssignRequest request = new AssignRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/assign", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.errorMessage").value("Box is already assigned"));

        verify(boxService).assignBoxToEvent(anyLong(), anyLong());
    }

    @Test
    void unregisterBoxFromFundraisingEventShouldReturn200withProperMessage() throws Exception {
        doNothing().when(boxService).unregisterBoxFromEvent(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/unregister", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("has been unregistered")));

        verify(boxService).unregisterBoxFromEvent(anyLong());
    }

    @Test
    void unregisterBoxFromFundraisingEventShouldReturn404noBoxToBeUnregistered() throws Exception {
        doThrow(new RecordNotFoundException("Box not found")).when(boxService).unregisterBoxFromEvent(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{id}/unregister", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Box not found"));

        verify(boxService).unregisterBoxFromEvent(anyLong());
    }

    @Test
    void donateBoxShouldReturn200withDonationResponse() throws Exception {
        DonationRequest donationRequest = new DonationRequest(BigDecimal.valueOf(100), "PLN");

        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("PLN")));
        doNothing().when(boxService).donateBox(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/donate", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(donationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boxId").value(1))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.currency").value("PLN"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testCases/donateRequestValidator_case.csv")
    void donateBoxShouldReturn400causedByValidation(BigDecimal amount, String currency, String message) throws Exception {
        DonationRequest donationRequest = new DonationRequest(amount, currency);

        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("PLN")));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/donate", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(donationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value(message));
    }

    @Test
    void donateBoxShouldReturn403causedByBoxUnassign() throws Exception {
        DonationRequest donationRequest = new DonationRequest(BigDecimal.valueOf(100), "PLN");

        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("PLN")));
        doThrow(new DonationException("You cannot donate unassigned box")).when(boxService).donateBox(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/donate", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(donationRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.errorMessage").value("You cannot donate unassigned box"));
    }

    @Test
    void donateBoxShouldReturn404boxNotFoundErrorMessage() throws Exception {
        DonationRequest donationRequest = new DonationRequest(BigDecimal.valueOf(100), "PLN");

        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("PLN")));
        doThrow(new RecordNotFoundException("Box not found")).when(boxService).donateBox(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/donate", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(donationRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Box not found"));
    }

    @Test
    void transferBoxMoneyToAccountShouldReturn200withProperMessage() throws Exception {
        doNothing().when(boxService).transferFundsToAccount(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/transfer", 1))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("has been transferred to assigned charity account")));
    }

    @Test
    void transferBoxMoneyToAccountShouldReturn403causedByEmptyBox() throws Exception {
        doThrow(new TransferException("Cannot transfer funds because box is empty"))
                .when(boxService).transferFundsToAccount(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/transfer", 1))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Cannot transfer funds because box is empty"));

    }

    @Test
    void transferBoxMoneyToAccountShouldReturn404boxNotFoundErrorMessage() throws Exception {
        doThrow(new RecordNotFoundException("Box not found")).when(boxService).transferFundsToAccount(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/transfer", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Box not found"));
    }

    @Test
    void transferBoxMoneyToAccountShouldReturn400causedByExchangeError() throws Exception {
        doThrow(new MoneyExchangeClientException("Exchange error")).when(boxService).transferFundsToAccount(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/transfer", 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Exchange error"));
    }

    @Test
    @DisplayName("HandleInvalidInput")
    void testInvalidInputToHandleMessageNotReadable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/box/{boxId}/transfer", "ABC"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Invalid input type - check your request"));
    }


}
