package nl.rabobank.ivarreukers.customerstatementprocessor.service.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReferenceValidatorServiceTest {
    private TransactionValidator validator;

    private List<Transaction> transactions;

    @BeforeEach
    void init() {
      validator = new ReferenceValidatorService();
      transactions = new ArrayList<>();
    }

    @Test
    void shouldReturnEmptyWhenCorrect() {
      addCorrectTransactions(2);
      List<Transaction> output = validator.validate(transactions);

      assertNotNull(output);
      assertEquals(0, output.size());
    }

    @Test
    void shouldReturnDuplicateReferences() {
      int duplicateCount = 1;
      addDuplicateTransactionPairs(duplicateCount);
      addCorrectTransactions(10);
      List<Transaction> output = validator.validate(transactions);

      assertEquals(2, output.size());
      assertEquals(output.get(0).getReference(), output.get(1).getReference());

      assertNotEquals(output.get(0).getAccountNumber(), output.get(1).getAccountNumber());
    }

    @Test
    void shouldReturnMultipleDuplicateReferences() {
      int duplicateCount = 10;
      addDuplicateTransactionPairs(duplicateCount);

      List<Transaction> output = validator.validate(transactions);

      assertEquals(20, output.size());
    }

    @AfterEach
    void tearDown() {
      transactions = null;
    }

    private void addCorrectTransactions(int amount) {
      for(int i = 1; i <= amount; i++) {
        transactions.add(createTransaction(-i, "accNum"+i));
      }
    }

    private void addDuplicateTransactionPairs(int amountOfDuplicatePairs) {
      for(int i = 0; i < amountOfDuplicatePairs; i++) {
        transactions.add(createTransaction(i, UUID.randomUUID().toString()));
        transactions.add(createTransaction(i, UUID.randomUUID().toString()));
      }
    }

    private Transaction createTransaction(long reference, String accountNumber) {
      Transaction transaction = new Transaction();
      transaction.setReference(reference);
      transaction.setAccountNumber(accountNumber);
      return transaction;
    }

  }
