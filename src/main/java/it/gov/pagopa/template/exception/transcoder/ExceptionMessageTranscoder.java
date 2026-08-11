package it.gov.pagopa.template.exception.transcoder;

public interface ExceptionMessageTranscoder<T extends Exception> {
  ExceptionMessageTranscoded transcode(T exception);
}
