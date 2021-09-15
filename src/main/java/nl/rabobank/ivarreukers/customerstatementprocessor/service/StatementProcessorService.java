package nl.rabobank.ivarreukers.customerstatementprocessor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.Transaction;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.validator.BalanceValidatorService;
import nl.rabobank.ivarreukers.customerstatementprocessor.service.validator.ReferenceValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatementProcessorService {

  @Autowired private BalanceValidatorService balanceValidatorService;
  @Autowired private ReferenceValidatorService referenceValidatorService;

  /**
   *  Validates provided transactions by checking for incorrect mutations and duplicate references
   *
   * @param transactions The transactions to validate
   * @return CustomerStatementProcessorResponse containing the status and invalid transactions
   */
  public CustomerStatementProcessorResponse validateTransactions(List<Transaction> transactions) {

    List<Transaction> invalidBalanceTransactions = balanceValidatorService.validate(transactions);
    List<Transaction> invalidReferenceTransactions = referenceValidatorService.validate(transactions);

    boolean containsInvalidBalanceRecords = invalidBalanceTransactions.size() > 0;
    boolean containsDuplicateReferenceRecords = invalidReferenceTransactions.size() > 0;

    CustomerStatementProcessorResponse response = new CustomerStatementProcessorResponse();
    response.setStatus(ResultStatus.SUCCESSFUL);

    if(containsDuplicateReferenceRecords && containsInvalidBalanceRecords) {
      response.setStatus(ResultStatus.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
      response.setErrorRecords(combineTransactions(invalidBalanceTransactions, invalidReferenceTransactions));
    } else if(containsInvalidBalanceRecords) {
      response.setStatus(ResultStatus.INCORRECT_END_BALANCE);
      response.setErrorRecords(invalidBalanceTransactions);
    } else if(containsDuplicateReferenceRecords) {
      response.setStatus(ResultStatus.DUPLICATE_REFERENCE);
      response.setErrorRecords(invalidReferenceTransactions);
    }

    return response;
  }

  private List<Transaction> combineTransactions(List<Transaction> listOne, List<Transaction> listTwo) {
    Set<Transaction> combinedLists = new HashSet<>(listOne);
    combinedLists.addAll(listTwo);
    return new ArrayList<>(combinedLists);
  }



}
