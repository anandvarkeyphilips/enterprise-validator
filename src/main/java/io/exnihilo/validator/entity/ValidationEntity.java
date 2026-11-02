package io.exnihilo.validator.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Validation Response Entity has the response details for all configured editor methods.
 *
 * @author Anand Varkey Philips
 * @since 2.0.0.RELEASE
 */
@Getter
@Setter
@Builder
@ToString
@Component
@EqualsAndHashCode
@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
public class ValidationEntity {
  @Schema(accessMode = Schema.AccessMode.READ_ONLY) // NOSONAR
  private boolean valid;
  @Schema(accessMode = Schema.AccessMode.READ_ONLY) // NOSONAR
  private int lineNumber;
  @Schema(accessMode = Schema.AccessMode.READ_ONLY) // NOSONAR
  private int columnNumber;
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "exampleData", description = "inputData")
  private String inputMessage;
  @Schema(accessMode = Schema.AccessMode.READ_ONLY) // NOSONAR
  private String validationMessage;

  @JsonCreator
  ValidationEntity(@JsonProperty("valid") final boolean valid, @JsonProperty("lineNumber") final int lineNumber,
      @JsonProperty("columnNumber") final int columnNumber, @JsonProperty("inputMessage") final String inputMessage,
      @JsonProperty("validationMessage") final String validationMessage) {
    this.valid = valid;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.inputMessage = inputMessage;
    this.validationMessage = validationMessage;
  }
}
