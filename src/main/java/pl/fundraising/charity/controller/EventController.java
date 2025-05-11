package pl.fundraising.charity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fundraising.charity.entity.FundraisingEvent;
import pl.fundraising.charity.model.request.EventRequest;
import pl.fundraising.charity.model.response.GeneralServerResponse;
import pl.fundraising.charity.service.EventService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<GeneralServerResponse> createNewEvent(@RequestBody @Valid EventRequest eventRequest) {
        FundraisingEvent event = eventService.createEvent(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralServerResponse("Event: " + event.getName()
                + " has been created. Event account base currency: " + event.getAccount().getCurrency().getSymbol()));
    }

}
