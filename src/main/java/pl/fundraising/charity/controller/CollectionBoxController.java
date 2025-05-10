package pl.fundraising.charity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fundraising.charity.model.request.AssignRequest;
import pl.fundraising.charity.model.request.DonationRequest;
import pl.fundraising.charity.model.response.CollectionBoxResponse;
import pl.fundraising.charity.model.response.GeneralServerResponse;
import pl.fundraising.charity.service.CollectionBoxService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService boxService;

    @GetMapping("/box")
    public ResponseEntity<List<CollectionBoxResponse>> getBoxStatus() {
        return ResponseEntity.ok(boxService.getAllBoxesInfo());
    }

    @PostMapping("/box")
    public ResponseEntity<GeneralServerResponse> registerBox() {
        boxService.createNewBox();
        return ResponseEntity.ok(new GeneralServerResponse("New Empty box created"));
    }

    @DeleteMapping("/box/{id}")
    public ResponseEntity<GeneralServerResponse> removeBox(@PathVariable long id) {
        boxService.deleteBox(id);
        return ResponseEntity.ok(new GeneralServerResponse(
                "Box wih id:" + id + " has been unregistered"));
    }

    @PutMapping("/box/{boxId}/assign")
    public ResponseEntity<GeneralServerResponse> assignBoxToEvent(
            @PathVariable long boxId,
            @RequestBody AssignRequest request) {
        boxService.assignBoxToEvent(boxId, request.getEventId());
        return ResponseEntity.ok(new GeneralServerResponse(
                "Box with id:" + boxId + " has been assigned to event:"
        ));
    }

    @PutMapping("/box/{boxId}/unregister")
    public ResponseEntity<GeneralServerResponse> unregisterBox(
            @PathVariable long boxId) {
        boxService.unregisterBoxFromEvent(boxId);
        return ResponseEntity.ok(new GeneralServerResponse(
                "Box with id: " + boxId + " has been unregistered from fundraising event"));
    }

    // Change to object
    @PutMapping("/box/{boxId}/donate")
    public ResponseEntity<GeneralServerResponse> donateMoneyToBox(
            @PathVariable long boxId,
            @Valid @RequestBody DonationRequest request) {

        boxService.donateBox(boxId, request);
        return ResponseEntity.ok(new GeneralServerResponse(
                "Donated box " + boxId + " with: " + request.getAmount() + request.getCurrency()));
    }

    @PutMapping("/box/{boxId}/transfer")
    public ResponseEntity<GeneralServerResponse> transferBalanceToAccount(@PathVariable long boxId) {
        boxService.transferFundsToAccount(boxId);
        return ResponseEntity.ok().body(new GeneralServerResponse(
                "Balance from box: " + boxId + " has been transfer to assigned charity account"));
    }
}
