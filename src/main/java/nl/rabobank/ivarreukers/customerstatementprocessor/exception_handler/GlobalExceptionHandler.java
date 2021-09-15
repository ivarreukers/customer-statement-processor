package nl.rabobank.ivarreukers.customerstatementprocessor.exception_handler;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.CustomerStatementProcessorResponse;
import nl.rabobank.ivarreukers.customerstatementprocessor.domain.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<CustomerStatementProcessorResponse> processBadRequest(final Exception exception,
      HttpServletRequest request) {
    log.error(exception.getMessage());

    CustomerStatementProcessorResponse response = new CustomerStatementProcessorResponse();
    response.setStatus(ResultStatus.BAD_REQUEST);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomerStatementProcessorResponse> processException(final Exception exception,
      HttpServletRequest request) {
    log.error(exception.toString());
    log.error(exception.getMessage());

    CustomerStatementProcessorResponse response = new CustomerStatementProcessorResponse();
    response.setStatus(ResultStatus.INTERNAL_SERVER_ERROR);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }
}
