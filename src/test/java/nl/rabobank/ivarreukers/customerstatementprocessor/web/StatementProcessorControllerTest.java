package nl.rabobank.ivarreukers.customerstatementprocessor.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Collections;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.StatementProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class StatementProcessorControllerTest {

  @Mock
  private StatementProcessorService statementProcessorService;

  @InjectMocks
  private StatementProcessorController statementProcessorController;

  @Test
  void validateTransactions_shouldCallStatementProcessorService() {
    statementProcessorController.validateTransactions(Collections.emptyList());
    Mockito.verify(statementProcessorService, times(1)).validateTransactions(anyList());
  }

  @Test
  void validateTransactions_shouldReturnStatus200WithBody() {
    CustomerStatementProcessorResponse expectedResponse = new CustomerStatementProcessorResponse();
    expectedResponse.setStatus(ResultStatus.DUPLICATE_REFERENCE);
    when(statementProcessorService.validateTransactions(anyList()))
        .thenReturn(expectedResponse);

    ResponseEntity<CustomerStatementProcessorResponse> response =
        statementProcessorController.validateTransactions(Collections.emptyList());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
  }
}