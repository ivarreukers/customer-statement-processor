package nl.rabobank.ivarreukers.customerstatementprocessor.web;

import java.util.List;
import javax.validation.Valid;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.StatementProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class StatementProcessorController {

  @Autowired
  private StatementProcessorService statementProcessorService;

  @PostMapping("/validate-transactions")
  public ResponseEntity<CustomerStatementProcessorResponse> validateTransactions(
      @RequestBody List<@Valid Transaction> transactions) {
    return ResponseEntity.ok(statementProcessorService.validateTransactions(transactions));
  }
}
