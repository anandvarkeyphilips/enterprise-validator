package io.exnihilo.validator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Validation Response Entity has the response details for all configured editor methods. Tests are
 * based on: https://jqno.nl/equalsverifier/ todo: verify contains method in HashSet use cases
 */
public class ValidationEntityTestFromEqualsverifier {

  @Test
  public void newValidationEntity_whenEqual_ThenTrue() {
    class ValidationEntityExtended extends ValidationEntity {
      String additionalDetail;

      public String getAdditionalDetail() {
        return this.additionalDetail;
      }

      public void setAdditionalDetail(final String additionalDetail) {
        this.additionalDetail = additionalDetail;
      }

      ValidationEntityExtended(@JsonProperty("valid") final boolean valid,
          @JsonProperty("lineNumber") final int lineNumber, @JsonProperty("columnNumber") final int columnNumber,
          @JsonProperty("inputMessage") final String inputMessage,
          @JsonProperty("validationMessage") final String validationMessage, String additionalDetail) {
        super(true, 11, 22, "inputMessage", "Validation Successful");
        this.additionalDetail = additionalDetail;
      }

      @Override
      public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ValidationEntityExtended) {
          ValidationEntityExtended that = (ValidationEntityExtended) other;
          result = (that.canEqual(this) && this.additionalDetail.equals(that.additionalDetail) && super.equals(that));
        }
        return result;
      }

      @Override
      public int hashCode() {
        return (41 * super.hashCode() + additionalDetail.hashCode());
      }

      @Override
      public boolean canEqual(Object other) {
        return (other instanceof ValidationEntityExtended);
      }

      @Override
      public String toString() {
        return "ValidationEntityExtended(valid=" + this.isValid() + ", lineNumber=" + this.getLineNumber()
            + ", columnNumber=" + this.getColumnNumber() + ", inputMessage=" + this.getInputMessage()
            + ", validationMessage=" + this.getValidationMessage() + ", additionalDetail=" + this.getAdditionalDetail()
            + ")";
      }
    }

    EqualsVerifier.forClass(ValidationEntity.class).withRedefinedSubclass(ValidationEntityExtended.class)
        .suppress(Warning.NONFINAL_FIELDS).verify();
  }
}
