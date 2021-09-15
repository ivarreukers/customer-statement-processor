package nl.rabobank.ivarreukers.customerstatementprocessor.domain;

public enum ResultStatus {
  SUCCESSFUL,
  DUPLICATE_REFERENCE,
  INCORRECT_END_BALANCE,
  DUPLICATE_REFERENCE_INCORRECT_END_BALANCE,
  BAD_REQUEST,
  INTERNAL_SERVER_ERROR
}
