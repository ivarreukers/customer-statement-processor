package nl.rabobank.ivarreukers.customerstatementprocessor.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class StatementProcessorControllerIT {

  private static String resourceDir = "src/test/resources/validate_transactions/";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void validateTransactions_emptyListShouldReturnOK() throws Exception {
    mockMvc.perform(post("/validate-transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content("[]"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultStatus.SUCCESSFUL.toString()))
        .andExpect(jsonPath("$.errorRecords").isEmpty());
  }

  @Test
  void validateTransactions_validTransactions() throws Exception {
    String validTransactionsFile = resourceDir + "valid_transactions.json";
    mockMvc.perform(post("/validate-transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(validTransactionsFile)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultStatus.SUCCESSFUL.toString()))
        .andExpect(jsonPath("$.errorRecords").isEmpty());
  }

  @Test
  void validateTransactions_invalidBalanceTransactions() throws Exception {
    String invalidBalanceTransactions = resourceDir + "invalid_balance_transactions.json";
    mockMvc.perform(post("/validate-transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(invalidBalanceTransactions)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultStatus.INCORRECT_END_BALANCE.toString()))
        .andExpect(jsonPath("$.errorRecords").isNotEmpty())
        .andExpect(jsonPath("$.errorRecords[0].accountNumber").value("invalid balance"));
  }

  @Test
  void validateTransactions_duplicateReferenceTransactions() throws Exception {
    String duplicateReferenceTransactions = resourceDir + "duplicate_reference_transactions.json";
    mockMvc.perform(post("/validate-transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(duplicateReferenceTransactions)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultStatus.DUPLICATE_REFERENCE.toString()))
        .andExpect(jsonPath("$.errorRecords").isNotEmpty())
        .andExpect(jsonPath("$.errorRecords[0].reference").value(-1000))
        .andExpect(jsonPath("$.errorRecords[1].reference").value(-1000));
  }

  @Test
  void validateTransactions_invalidBalanceAndDuplicateReferenceTransactions() throws Exception {
    String invalidBalanceAndReferences = resourceDir + "invalid_balance_duplicate_references.json";
    mockMvc.perform(post("/validate-transactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(invalidBalanceAndReferences)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(ResultStatus.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE.toString()))
        .andExpect(jsonPath("$.errorRecords").isNotEmpty())
        .andExpect(jsonPath("$.errorRecords[?(@.accountNumber == \"invalid balance\")]").exists())
        .andExpect(jsonPath("$.errorRecords[?(@.reference == -1000)]").exists());
  }

  @Test
  void validateTransactions_invalidJson() throws Exception {
    String invalidJson = resourceDir + "invalid_json.json";
    mockMvc.perform(post("/validate-transactions")   .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(invalidJson)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(ResultStatus.BAD_REQUEST.toString()))
        .andExpect(jsonPath("$.errorRecords").isEmpty());
  }

  @Test
  void validateTransactions_invalidTransactionValues() throws Exception {
    String invalidTransactionValues = resourceDir + "invalid_transaction_values.json";
    mockMvc.perform(post("/validate-transactions")   .contentType(MediaType.APPLICATION_JSON)
        .content(readFileAsString(invalidTransactionValues)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(ResultStatus.BAD_REQUEST.toString()))
        .andExpect(jsonPath("$.errorRecords").isEmpty());
  }

  private String readFileAsString(String file) throws IOException {
    return new String(Files.readAllBytes(Paths.get(file)));
  }
}