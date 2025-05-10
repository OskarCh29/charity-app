package pl.fundraising.charity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fundraising.charity.client.CantorClient;
import pl.fundraising.charity.entity.Cantor;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.model.response.GeneralServerResponse;
import pl.fundraising.charity.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CantorClient client;

    @PostMapping("/event")
    public ResponseEntity<GeneralServerResponse> createNewEvent(@RequestBody @Valid EventRequest eventRequest){
        eventService.createEvent(eventRequest);
        return ResponseEntity.ok(new GeneralServerResponse("Event: " + eventRequest.getCharityName()
        + " has been created. Event account base currency: " + eventRequest.getCurrencySymbol()));
    }
}
