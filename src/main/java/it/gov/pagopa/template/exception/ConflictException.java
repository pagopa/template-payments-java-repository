package it.gov.pagopa.template.exception;

public class ConflictException extends BaseBusinessException {
  public ConflictException(String code, String message) {
    super(code, message);
  }
}
