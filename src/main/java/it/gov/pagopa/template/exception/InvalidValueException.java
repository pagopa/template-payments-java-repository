package it.gov.pagopa.template.exception;

public class InvalidValueException extends BaseBusinessException{
  public InvalidValueException(String code, String message) {
    super(code, message);
  }
}
