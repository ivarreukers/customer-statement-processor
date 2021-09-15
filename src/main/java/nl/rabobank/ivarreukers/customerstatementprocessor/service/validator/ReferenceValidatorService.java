package nl.rabobank.ivarreukers.customerstatementprocessor.service.validator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReferenceValidatorService implements TransactionValidator {

  /**
   * Returns all transactions that contain a non-unique reference number
   *
   * @param transactions the transactions the validate
   * @return all transactions with duplicate references
   */
  @Override
  public List<Transaction> validate(List<Transaction> transactions) {
    return getNonUniqueReferences(transactions);
  }

  private List<Transaction> getNonUniqueReferences(List<Transaction> transactions) {
    log.info(
        "Starting validation of unique reference for " + transactions.size() + " transactions");

    List<Transaction> errorRecords = new ArrayList<>();
    Map<Long, Transaction> seenTransactions = new HashMap<>();
    Set<Long> seenInvalidReferences = new HashSet<>();

    for (Transaction transaction : transactions) {
      if (seenTransactions.containsKey(transaction.getReference())) {
        errorRecords.add(transaction);
        seenInvalidReferences.add(transaction.getReference());
      } else {
        seenTransactions.put(transaction.getReference(), transaction);
      }
    }

    for (Long invalidReference : seenInvalidReferences) {
      errorRecords.add(seenTransactions.get(invalidReference));
    }

    log.info("Found " + errorRecords.size() + " transactions with a non unique reference");

    return errorRecords;
  }
}