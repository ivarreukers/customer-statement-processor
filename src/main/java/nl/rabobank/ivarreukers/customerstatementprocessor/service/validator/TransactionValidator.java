package nl.rabobank.ivarreukers.customerstatementprocessor.service.validator;

import java.util.List;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;

public interface TransactionValidator {

  /**
   * Validates the transaction and returns invalid transactions
   *
   * @param transactions the transactions the validate
   * @return all invalid transactions
   */
  List<Transaction> validate(List<Transaction> transactions);

}
