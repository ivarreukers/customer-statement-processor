package nl.rabobank.ivarreukers.customerstatementprocessor.service.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceValidatorServiceTest {

    private TransactionValidator balanceValidator;
    private List<Transaction> transactions;

    private Transaction correctNormal, correctSmallNegative, correctSmallPositive, correctBigPositive, correctBigNegative;
    private Transaction invalidNormal, invalidSmallNegative, invalidSmallPositive, invalidBigPositive, invalidBigNegative;

    @BeforeEach
    void init() {
      balanceValidator = new BalanceValidatorService();

      // all startBalances are different so we don't have to specify reference as an id
      correctNormal = createTransaction(100, 50, 150);
      correctSmallNegative = createTransaction(100, -0.01, 99.99);
      correctSmallPositive = createTransaction(99.98, 0.01, 99.99);
      correctBigPositive = createTransaction(80992392, 21382413, 102374805);
      correctBigNegative = createTransaction(21382413, -80992392, -59609979);

      invalidNormal = createTransaction(100.01, 50, 150);
      invalidSmallNegative = createTransaction(99.76, -0.01, 99.77);
      invalidSmallPositive = createTransaction(99.54, 0.01, 99.54);
      invalidBigNegative = createTransaction(800000000.05, -1000000000.03, -20000000.01);
      invalidBigPositive = createTransaction(780000000.01, 830000000.36, 161000000.36);
    }

    @Test
    void shouldReturnEmptyWhenCorrect() {
      transactions = Arrays.asList(correctNormal, correctBigPositive, correctSmallNegative);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertNotNull(output);
      assertEquals(0, output.size());
    }

    @Test
    void shouldRecognizeFalseNormalMutation() {
      transactions = Arrays.asList(correctBigNegative, correctSmallPositive, invalidNormal);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertEquals(1, output.size());
      assertEquals(invalidNormal.getStartBalance(), output.get(0).getStartBalance());
    }

    @Test
    void shouldRecognizeFalseSmallNegativeMutation() {
      transactions = Arrays.asList(correctBigNegative, invalidSmallNegative, correctSmallPositive);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertEquals(1, output.size());
      assertEquals(invalidSmallNegative.getStartBalance(), output.get(0).getStartBalance());
    }

    @Test
    void shouldRecognizeFalseSmallPositiveMutation() {
      transactions = Arrays.asList(correctBigNegative, correctSmallPositive, invalidSmallPositive);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertEquals(1, output.size());
      assertEquals(invalidSmallPositive.getStartBalance(), output.get(0).getStartBalance());
    }

    @Test
    void shouldRecognizeFalseBigNegativeMutation() {
      transactions = Arrays.asList(invalidBigNegative, correctBigNegative, correctSmallPositive);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertEquals(1, output.size());
      assertEquals(invalidBigNegative.getStartBalance(), output.get(0).getStartBalance());
    }

    @Test
    void shouldRecognizeFalseBigPositiveMutation() {
      transactions = Arrays.asList(correctBigNegative, correctSmallPositive, invalidBigPositive);

      List<Transaction> output = balanceValidator.validate(transactions);
      assertEquals(1, output.size());
      assertEquals(invalidBigPositive.getStartBalance(), output.get(0).getStartBalance());
    }

    @Test
    void shouldRecognizeMultiple() {
      transactions = Arrays.asList(correctBigNegative, correctSmallPositive, invalidBigPositive, invalidNormal,
          invalidSmallPositive);

      List<Transaction> output = balanceValidator.validate(transactions);

      assertEquals(3, output.size());
      assertEquals(invalidBigPositive.getStartBalance(), output.get(0).getStartBalance());
    }

    private Transaction createTransaction(double start, double mutation, double end) {
      Transaction transaction = new Transaction();
      transaction.setStartBalance(BigDecimal.valueOf(start));
      transaction.setMutation(BigDecimal.valueOf(mutation));
      transaction.setEndBalance(BigDecimal.valueOf(end));
      return transaction;
    }
  }
