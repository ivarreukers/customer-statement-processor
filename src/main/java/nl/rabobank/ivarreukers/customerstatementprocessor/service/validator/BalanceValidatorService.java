package nl.rabobank.ivarreukers.customerstatementprocessor.service.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import org.springframework.stereotype.Component;

/**
 * Class that can validate a balance and mutation mismatch of transactions
 *
 */
@Slf4j
@Component
public class BalanceValidatorService implements TransactionValidator {
  /**
   * Returns transactions where the start- & end balance do not
   *  match with the given mutation.
   *
   * @param transactions the transactions the validate
   * @return all invalid transactions
   */
  @Override
  public List<Transaction> validate(List<Transaction> transactions) {
    log.info("Starting validation of transactions based on correct balance for " + transactions.size() + " transactions");
    List<Transaction> invalidTransactions = transactions.stream()
        .filter(this::isTransactionInvalid)
        .collect(Collectors.toList());

    log.info("Found " + invalidTransactions.size() + " transactions with invalid mutation balance");
    return invalidTransactions;
  }

  private boolean isTransactionInvalid(Transaction transaction) {
    BigDecimal startBalance = transaction.getStartBalance();
    BigDecimal mutation = transaction.getMutation();
    BigDecimal endBalance = transaction.getEndBalance();

    // check if start balance + (-)mutation = end balance
    BigDecimal calculatedEndBalance = startBalance.add(mutation);

    return endBalance.compareTo(calculatedEndBalance) != 0;
  }


}