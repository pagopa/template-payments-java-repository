package it.gov.pagopa.template.config.rest;

import it.gov.pagopa.template.dto.generated.ErrorFieldDTO;

import java.util.List;

public record PuErrorDTO(
  String category,
  String code,
  String message,
  List<ErrorFieldDTO> fields
) {
}
