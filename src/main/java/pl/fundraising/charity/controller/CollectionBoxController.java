package pl.fundraising.charity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
