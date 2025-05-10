package pl.fundraising.charity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fundraising.charity.model.response.FinancialReportResponse;
import pl.fundraising.charity.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FinancialReportController {

    private final AccountService accountService;

    @GetMapping("/financial-report")
    public ResponseEntity<List<FinancialReportResponse>> getFinancialReport() {
        return ResponseEntity.ok(accountService.getAccountsFinancialReport());
    }
}
