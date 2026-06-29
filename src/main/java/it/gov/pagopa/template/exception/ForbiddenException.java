package it.gov.pagopa.template.exception;

public class ForbiddenException extends BaseBusinessException {
  public ForbiddenException(String code, String message) {
    super(code, message);
  }
}
