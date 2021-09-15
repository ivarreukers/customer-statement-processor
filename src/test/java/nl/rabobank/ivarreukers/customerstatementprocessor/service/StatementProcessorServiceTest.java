package nl.rabobank.ivarreukers.customerstatementprocessor.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.validator.BalanceValidatorService;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.validator.ReferenceValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatementProcessorServiceTest {
  @Mock
  private BalanceValidatorService balanceValidatorService;

  @Mock
  private ReferenceValidatorService referenceValidatorService;

  @InjectMocks
  StatementProcessorService service;

  @Test
  void validateTransactions_shouldReturnStatusOKOnNOInvalidTransactions() {
    mockReturnTransactionsWithInvalidBalance(Collections.emptyList());
    mockReturnTransactionsWithInvalidReference(Collections.emptyList());

    CustomerStatementProcessorResponse response = service.validateTransactions(Collections.emptyList());

    assertEquals(ResultStatus.SUCCESSFUL, response.getStatus());
    assertNotNull(response.getErrorRecords());
    assertEquals(0, response.getErrorRecords().size());
  }

  @Test
  void validateTransactions_shouldReturnInvalidBalanceOnInvalidTransactions() {
    List<Transaction> invalidBalanceTransactions = Collections.singletonList(new Transaction());
    mockReturnTransactionsWithInvalidBalance(invalidBalanceTransactions);
    mockReturnTransactionsWithInvalidReference(Collections.emptyList());

    CustomerStatementProcessorResponse response = service.validateTransactions(Collections.emptyList());

    assertEquals(ResultStatus.INCORRECT_END_BALANCE, response.getStatus());
    assertNotNull(response.getErrorRecords());
    assertEquals(1, response.getErrorRecords().size());
  }

  @Test
  void validateTransactions_shouldReturnInvalidReferenceOnInvalidTransactions() {
    List<Transaction> invalidReferenceTransactions = Collections.singletonList(new Transaction());
    mockReturnTransactionsWithInvalidBalance(Collections.emptyList());
    mockReturnTransactionsWithInvalidReference(invalidReferenceTransactions);

    CustomerStatementProcessorResponse response = service.validateTransactions(Collections.emptyList());

    assertEquals(ResultStatus.DUPLICATE_REFERENCE, response.getStatus());
    assertNotNull(response.getErrorRecords());
    assertEquals(1, response.getErrorRecords().size());
  }

  @Test
  void validateTransactions_shouldReturnInvalidReferenceAndBalanceOnInvalidTransactions() {
    Transaction transaction = new Transaction();
    transaction.setAccountNumber("1234");
    List<Transaction> invalidBalanceTransactions = Collections.singletonList(transaction);
    List<Transaction> invalidReferenceTransactions = Collections.singletonList(new Transaction());

    mockReturnTransactionsWithInvalidBalance(invalidBalanceTransactions);
    mockReturnTransactionsWithInvalidReference(invalidReferenceTransactions);

    CustomerStatementProcessorResponse response = service.validateTransactions(Collections.emptyList());

    assertEquals(ResultStatus.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, response.getStatus());
    assertNotNull(response.getErrorRecords());
    assertEquals(2, response.getErrorRecords().size());
  }

  @Test
  void validateTransaction_shouldNotReturnDuplicateTransactions() {
    Transaction transaction = new Transaction();
    List<Transaction> invalidBalanceTransactions = Collections.singletonList(transaction);
    List<Transaction> invalidReferenceTransactions = Collections.singletonList(transaction);

    mockReturnTransactionsWithInvalidBalance(invalidBalanceTransactions);
    mockReturnTransactionsWithInvalidReference(invalidReferenceTransactions);

    CustomerStatementProcessorResponse response = service.validateTransactions(Collections.emptyList());

    assertNotNull(response.getErrorRecords());
    assertEquals(1, response.getErrorRecords().size());
  }

  private void mockReturnTransactionsWithInvalidBalance(List<Transaction> transactions) {
    when(balanceValidatorService.validate(anyList())).thenReturn(transactions);
  }

  private void mockReturnTransactionsWithInvalidReference(List<Transaction> transactions) {
    when(referenceValidatorService.validate(anyList())).thenReturn(transactions);
  }
}