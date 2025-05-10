package pl.fundraising.charity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fundraising.charity.client.CantorClient;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.model.response.GeneralServerResponse;
import pl.fundraising.charity.service.EventService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CantorClient client;

    @PostMapping("/event")
    public ResponseEntity<GeneralServerResponse> createNewEvent(@RequestBody @Valid EventRequest eventRequest) {
        eventService.createEvent(eventRequest);
        return ResponseEntity.ok(new GeneralServerResponse("Event: " + eventRequest.getCharityName()
                + " has been created. Event account base currency: " + eventRequest.getCurrencySymbol()));
    }
}
