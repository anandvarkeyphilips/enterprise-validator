package io.exnihilo.validator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validation Response Entity has the response details for all configured editor methods. Tests are
 * based on: https://www.artima.com/lejava/articles/equality.html
 */
public class ValidationEntityTest {

  @Test
  void newValidationEntityWithBuilder_whenEqual_ThenTrue() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity z = x;

    // reflexive: for any non-null value x, the expression x.equals(x) should return true.
    assertTrue(x.equals(x));
    // symmetric: for any non-null values x and y, x.equals(y) should return true if and only if
    // y.equals(x) returns true.
    assertEquals(x, y);
    assertEquals(y, x);
    // transitive: for any non-null values x, y, and z, if x.equals(y) returns true and y.equals(z)
    // returns true, then x.equals(z) should return true.
    assertTrue(x.equals(z) && y.equals(z) && x.equals(y));
    // consistent: for any non-null values x and y, multiple invocations of x.equals(y) should
    // consistently return true or consistently return false, provided no information used in equals
    // comparisons on the objects is modified.
    assertTrue(x.equals(y) && x.equals(y) && x.equals(y) && x.equals(y));
    // Null check: for any non-null value x, x.equals(null) should return false.
    assertFalse(x.equals(null));

    assertEquals(x, y);
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
    assertEquals(x.getInputMessage(), y.getInputMessage());
    assertEquals(x.getValidationMessage(), y.getValidationMessage());
    assertEquals(x.getLineNumber(), y.getLineNumber());
    assertEquals(x.getColumnNumber(), y.getColumnNumber());
    assertEquals(x.isValid(), y.isValid());
  }

  @Test
  void newValidationEntityWithBuilder_whenNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("Custom Input Message").lineNumber(1).columnNumber(2)
        .valid(false).validationMessage("Custom Validation Message").build();

    assertNotEquals(x, y);
    assertNotEquals(y, x);
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
    assertNotEquals(x.getInputMessage(), y.getInputMessage());
    assertNotEquals(x.getValidationMessage(), y.getValidationMessage());
    assertNotEquals(x.getLineNumber(), y.getLineNumber());
    assertNotEquals(x.getColumnNumber(), y.getColumnNumber());
    assertNotEquals(x.isValid(), y.isValid());
  }

  @Test
  void newValidationEntityWithBuilder_whenBothAreEqualAndObjectInstances_ThenTrue() {
    Object x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build();
    Object y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build();

    assertTrue(x.equals(y) && y.equals(x));
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityStringWithBuilder_whenSameInstanceToStringsAreCompared_ThenTrue() {
    String x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").toString();
    String y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").toString();

    assertEquals(
        "ValidationEntity.ValidationEntityBuilder(valid=true, lineNumber=11, columnNumber=22, inputMessage=inputMessage, validationMessage=Validation Successful)",
        x);
    assertEquals(
        "ValidationEntity.ValidationEntityBuilder(valid=true, lineNumber=11, columnNumber=22, inputMessage=inputMessage, validationMessage=Validation Successful)",
        y);
  }

  @Test
  void newValidationEntityWithBuilder_whenOneIsInstanceAndOtherString_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    String y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build().toString();

    assertNotEquals(x, y);
    assertNotEquals(y, x);
    assertNotEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y);
  }

  @Test
  void newValidationEntityWithBuilder_whenOneIsInstanceAndOtherOneIsAChild_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();

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
        super(valid, lineNumber, columnNumber, inputMessage, validationMessage);
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
    ValidationEntityExtended validationEntityExtended =
        new ValidationEntityExtended(true, 11, 22, "inputMessage", "Validation Successful", "additionalDetail");
    assertNotEquals(x, validationEntityExtended);
    assertNotEquals(validationEntityExtended, x);
    assertNotEquals(x.hashCode(), validationEntityExtended.hashCode());
    assertNotEquals(x.toString(), validationEntityExtended.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenEachMethodIsEqual_ThenTrue() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();

    assertTrue(x.equals(y) && y.equals(x));
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
    assertEquals(x.getInputMessage(), y.getInputMessage());
    assertEquals(x.getValidationMessage(), y.getValidationMessage());
    assertEquals(x.getLineNumber(), y.getLineNumber());
    assertEquals(x.getColumnNumber(), y.getColumnNumber());
    assertEquals(x.isValid(), y.isValid());
  }

  @Test
  void newValidationEntityWithBuilder_whenInputMessageNullForOne_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage(null).lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build();

    assertNotEquals(x, y);
    assertNotEquals(y, x);
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenInputMessageNullForBoth_ThenTrue() {
    ValidationEntity x = ValidationEntity.builder().inputMessage(null).lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage(null).lineNumber(11).columnNumber(22).valid(true)
        .validationMessage("Validation Successful").build();

    assertEquals(x, y);
    assertEquals(y, x);
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenInputMessageNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Custom Input Message").build();

    assertFalse(x.equals(y) && y.equals(x));
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenValidNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    y.setValid(false);

    assertFalse(x.equals(y) && y.equals(x));
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenLineNumberNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    y.setLineNumber(1);

    assertFalse(x.equals(y) && y.equals(x));
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenColumnNumberNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    y.setColumnNumber(2);

    assertFalse(x.equals(y) && y.equals(x));
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenValidationMessageNullForOne_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage(null).build();

    assertNotEquals(x, y);
    assertNotEquals(y, x);
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenValidationMessageBothNull_ThenTrue() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage(null).build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage(null).build();

    assertEquals(x, y);
    assertEquals(y, x);
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithBuilder_whenValidationMessageNotEqual_ThenFalse() {
    ValidationEntity x = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Validation Successful").build();
    ValidationEntity y = ValidationEntity.builder().inputMessage("inputMessage").lineNumber(11).columnNumber(22)
        .valid(true).validationMessage("Custom Validation Message").build();

    assertFalse(x.equals(y) && y.equals(x));
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
  }

  @Test
  void newValidationEntityWithAllArgsConstructor_whenEqual_ThenTrue() {
    ValidationEntity x = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");
    ValidationEntity y = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");

    assertTrue(x.equals(y) && y.equals(x));
    assertEquals(x.hashCode(), y.hashCode());
    assertEquals(x.toString(), y.toString());
    assertEquals(x.getInputMessage(), y.getInputMessage());
    assertEquals(x.getValidationMessage(), y.getValidationMessage());
    assertEquals(x.getLineNumber(), y.getLineNumber());
    assertEquals(x.getColumnNumber(), y.getColumnNumber());
    assertEquals(x.isValid(), y.isValid());
  }

  @Test
  void newValidationEntityWithAllArgsConstructor_whenNotEqual_ThenFalse() {
    ValidationEntity x = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");
    ValidationEntity y = new ValidationEntity(false, 1, 1, "Custom inputMessage", "Custom Validation Successful");

    assertNotEquals(x, y);
    assertNotEquals(y, x);
    assertNotEquals(x.hashCode(), y.hashCode());
    assertNotEquals(x.toString(), y.toString());
    assertNotEquals(x.getInputMessage(), y.getInputMessage());
    assertNotEquals(x.getValidationMessage(), y.getValidationMessage());
    assertNotEquals(x.getLineNumber(), y.getLineNumber());
    assertNotEquals(x.getColumnNumber(), y.getColumnNumber());
    assertNotEquals(x.isValid(), y.isValid());
  }
}
