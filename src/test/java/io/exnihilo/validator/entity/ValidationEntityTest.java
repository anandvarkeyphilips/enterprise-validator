package io.exnihilo.validator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Assert;
import org.junit.Test;

/**
 * Validation Response Entity has the response details for all configured editor methods.
 * Tests are based on:
 * https://www.artima.com/lejava/articles/equality.html
 */
public class ValidationEntityTest {

    @Test
    public void newValidationEntityWithBuilder_whenEqual_ThenTrue() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity z = x;

        //reflexive: for any non-null value x, the expression x.equals(x) should return true.
        Assert.assertTrue(x.equals(x));
        //symmetric: for any non-null values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
        Assert.assertTrue(x.equals(y) && y.equals(x));
        //transitive: for any non-null values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
        Assert.assertTrue(x.equals(z) && y.equals(z) && x.equals(y));
        //consistent: for any non-null values x and y, multiple invocations of x.equals(y) should consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.
        Assert.assertTrue(x.equals(y) && x.equals(y) && x.equals(y) && x.equals(y));
        //Null check: for any non-null value x, x.equals(null) should return false.
        Assert.assertFalse(x.equals(null));

        Assert.assertEquals(x, y);
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
        Assert.assertEquals(x.getInputMessage(), y.getInputMessage());
        Assert.assertEquals(x.getValidationMessage(), y.getValidationMessage());
        Assert.assertEquals(x.getLineNumber(), y.getLineNumber());
        Assert.assertEquals(x.getColumnNumber(), y.getColumnNumber());
        Assert.assertEquals(x.isValid(), y.isValid());
    }

    @Test
    public void newValidationEntityWithBuilder_whenNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("Custom Input Message").lineNumber(1).columnNumber(2).valid(false).validationMessage("Custom Validation Message").build();

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
        Assert.assertNotEquals(x.getInputMessage(), y.getInputMessage());
        Assert.assertNotEquals(x.getValidationMessage(), y.getValidationMessage());
        Assert.assertNotEquals(x.getLineNumber(), y.getLineNumber());
        Assert.assertNotEquals(x.getColumnNumber(), y.getColumnNumber());
        Assert.assertNotEquals(x.isValid(), y.isValid());
    }

    @Test
    public void newValidationEntityWithBuilder_whenBothAreEqualAndObjectInstances_ThenTrue() {
        Object x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        Object y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();

        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityStringWithBuilder_whenSameInstanceToStringsAreCompared_ThenTrue() {
        String x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").toString();
        String y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").toString();

        Assert.assertEquals("ValidationEntity.ValidationEntityBuilder(valid=true, lineNumber=11, columnNumber=22, inputMessage=inputMessage, validationMessage=Validation Successful)", x);
        Assert.assertEquals("ValidationEntity.ValidationEntityBuilder(valid=true, lineNumber=11, columnNumber=22, inputMessage=inputMessage, validationMessage=Validation Successful)", y);
    }

    @Test
    public void newValidationEntityWithBuilder_whenOneIsInstanceAndOtherString_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        String y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build().toString();

        Assert.assertFalse(x.equals(y));
        Assert.assertFalse(y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenOneIsInstanceAndOtherOneIsAChild_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();

        class ValidationEntityExtended extends ValidationEntity {
            String additionalDetail;

            public String getAdditionalDetail() {
                return this.additionalDetail;
            }

            public void setAdditionalDetail(final String additionalDetail) {
                this.additionalDetail = additionalDetail;
            }

            ValidationEntityExtended(@JsonProperty("valid") final boolean valid, @JsonProperty("lineNumber") final int lineNumber,
                                     @JsonProperty("columnNumber") final int columnNumber, @JsonProperty("inputMessage") final String inputMessage,
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
                return "ValidationEntityExtended(valid=" + this.isValid() + ", lineNumber=" + this.getLineNumber() + ", columnNumber=" + this.getColumnNumber() + ", inputMessage=" + this.getInputMessage() + ", validationMessage=" + this.getValidationMessage() + ", additionalDetail=" + this.getAdditionalDetail() + ")";
            }
        }
        ValidationEntityExtended validationEntityExtended = new ValidationEntityExtended(true, 11, 22, "inputMessage", "Validation Successful", "additionalDetail");
        Assert.assertFalse(x.equals(validationEntityExtended));
        Assert.assertFalse(validationEntityExtended.equals(x));
        Assert.assertNotEquals(x.hashCode(), validationEntityExtended.hashCode());
        Assert.assertNotEquals(x.toString(), validationEntityExtended.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenEachMethodIsEqual_ThenTrue() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();

        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
        Assert.assertEquals(x.getInputMessage(), y.getInputMessage());
        Assert.assertEquals(x.getValidationMessage(), y.getValidationMessage());
        Assert.assertEquals(x.getLineNumber(), y.getLineNumber());
        Assert.assertEquals(x.getColumnNumber(), y.getColumnNumber());
        Assert.assertEquals(x.isValid(), y.isValid());
    }

    @Test
    public void newValidationEntityWithBuilder_whenInputMessageNullForOne_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder(null).lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();

        Assert.assertNotEquals(x, y);
        Assert.assertNotEquals(y, x);
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenInputMessageNullForBoth_ThenTrue() {
        ValidationEntity x = ValidationEntity.builder(null).lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder(null).lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();

        Assert.assertEquals(x, y);
        Assert.assertEquals(y, x);
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenInputMessageNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Custom Input Message").build();

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenValidNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        y.setValid(false);

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenLineNumberNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        y.setLineNumber(1);

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenColumnNumberNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        y.setColumnNumber(2);

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenValidationMessageNullForOne_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage(null).build();

        Assert.assertNotEquals(x, y);
        Assert.assertNotEquals(y, x);
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenValidationMessageBothNull_ThenTrue() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage(null).build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage(null).build();

        Assert.assertEquals(x, y);
        Assert.assertEquals(y, x);
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithBuilder_whenValidationMessageNotEqual_ThenFalse() {
        ValidationEntity x = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Validation Successful").build();
        ValidationEntity y = ValidationEntity.builder("inputMessage").lineNumber(11).columnNumber(22).valid(true).validationMessage("Custom Validation Message").build();

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void newValidationEntityWithAllArgsConstructor_whenEqual_ThenTrue() {
        ValidationEntity x = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");
        ValidationEntity y = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");

        Assert.assertTrue(x.equals(y) && y.equals(x));
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());
        Assert.assertEquals(x.getInputMessage(), y.getInputMessage());
        Assert.assertEquals(x.getValidationMessage(), y.getValidationMessage());
        Assert.assertEquals(x.getLineNumber(), y.getLineNumber());
        Assert.assertEquals(x.getColumnNumber(), y.getColumnNumber());
        Assert.assertEquals(x.isValid(), y.isValid());
    }


    @Test
    public void newValidationEntityWithAllArgsConstructor_whenNotEqual_ThenFalse() {
        ValidationEntity x = new ValidationEntity(true, 11, 22, "inputMessage", "Validation Successful");
        ValidationEntity y = new ValidationEntity(false, 1, 1, "Custom inputMessage", "Custom Validation Successful");

        Assert.assertFalse(x.equals(y) && y.equals(x));
        Assert.assertNotEquals(x.hashCode(), y.hashCode());
        Assert.assertNotEquals(x.toString(), y.toString());
        Assert.assertNotEquals(x.getInputMessage(), y.getInputMessage());
        Assert.assertNotEquals(x.getValidationMessage(), y.getValidationMessage());
        Assert.assertNotEquals(x.getLineNumber(), y.getLineNumber());
        Assert.assertNotEquals(x.getColumnNumber(), y.getColumnNumber());
        Assert.assertNotEquals(x.isValid(), y.isValid());
    }
}