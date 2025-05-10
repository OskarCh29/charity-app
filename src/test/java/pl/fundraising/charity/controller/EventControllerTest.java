package pl.fundraising.charity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.fundraising.charity.entity.CharityAccount;
import pl.fundraising.charity.entity.Currency;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.exception.EventAlreadyExistsException;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.repository.CurrencyRepository;
import pl.fundraising.charity.service.EventService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void currencySetup(){
        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("PLN")));
    }

    @Test
    void shouldReturn201withProperResponse() throws Exception {
        FundraisingEvent testEvent = initTestEvent();

        EventRequest request = new EventRequest("TestCharity", "PLN");

        when(eventService.createEvent(any())).thenReturn(testEvent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Event: " + testEvent.getName())));

        verify(eventService).createEvent(any());
    }

    @Test
    void shouldReturnStatus409eventAlreadyExists() throws Exception {
        String errorMessage = "Event with this name already exists";
        EventRequest request = new EventRequest("TestCharity", "PLN");

        when(eventService.createEvent(any())).thenThrow(
                new EventAlreadyExistsException(errorMessage));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));

        verify(eventService).createEvent(any());

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testCases/eventRequestValidator_case.csv")
    void shouldReturn400causedByValidation(String charityName, String currencySymbol,
                                                 String errorMessage) throws Exception {
        EventRequest request = new EventRequest(charityName, currencySymbol);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));

    }

    private FundraisingEvent initTestEvent() {
        FundraisingEvent event = new FundraisingEvent();
        event.setName("TestCharity");
        event.setAccount(new CharityAccount());
        event.getAccount().setCurrency(new Currency("PLN"));

        return event;
    }
}
