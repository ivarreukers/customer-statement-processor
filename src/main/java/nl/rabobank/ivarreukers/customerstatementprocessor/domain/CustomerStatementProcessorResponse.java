package nl.rabobank.ivarreukers.customerstatementprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerStatementProcessorResponse {
  private ResultStatus status;
  private List<Transaction> errorRecords;

  public CustomerStatementProcessorResponse() {
    this.errorRecords = new ArrayList<>();
  }


}
