package nl.rabobank.ivarreukers.customerstatementprocessor.exception_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.servlet.http.HttpServletRequest;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

  private HttpServletRequest request;
  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
    request = new MockHttpServletRequest();
  }

  @Test
  void processBadRequest_handlesValidationExceptions() {
    HttpMessageNotReadableException exception = new HttpMessageNotReadableException("", new MockHttpInputMessage(new byte[0]));

    ResponseEntity<CustomerStatementProcessorResponse> responseEntity = handler
        .processBadRequest(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(ResultStatus.BAD_REQUEST, responseEntity.getBody().getStatus());
    assertNotNull(responseEntity.getBody().getErrorRecords());
    assertEquals(0, responseEntity.getBody().getErrorRecords().size());
  }

  @Test
  void processException_handlesAnyOtherException() {
    Exception exception = new Exception();

    ResponseEntity<CustomerStatementProcessorResponse> responseEntity = handler
        .processException(exception, request);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(ResultStatus.INTERNAL_SERVER_ERROR, responseEntity.getBody().getStatus());
    assertNotNull(responseEntity.getBody().getErrorRecords());
    assertEquals(0, responseEntity.getBody().getErrorRecords().size());
  }

}