package io.exnihilo.validator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.exnihilo.validator.entity.DiffEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidatorServiceDiffTest {

  private IValidatorService validatorService;

  @BeforeEach
  void setUp() {
    validatorService = new ValidatorService();
  }

  private DiffEntity diff(String left, String right) {
    return validatorService.diffJsonService(DiffEntity.builder().inputMessage(left).compareMessage(right).build());
  }

  @Test
  void diffJsonService_whenKeysDiffer_reportsChangedRemovedAdded() {
    DiffEntity result = diff("{\"a\":1,\"b\":{\"c\":2}}", "{\"a\":2,\"b\":{\"d\":3}}");
    assertTrue(result.isValid());
    assertFalse(result.isIdentical());
    assertEquals(3, result.getDifferences().size());
    assertEquals("~ $.a: expected 1, found 2", result.getDifferences().get(0));
    assertEquals("- $.b.c: removed 2", result.getDifferences().get(1));
    assertEquals("+ $.b.d: added 3", result.getDifferences().get(2));
    assertEquals("3 key difference(s) found!!!", result.getValidationMessage());
  }

  @Test
  void diffJsonService_whenIdentical_reportsKeyIdentical() {
    DiffEntity result = diff("{\"a\":1,\"list\":[1,2]}", "{\"list\":[1,2],\"a\":1}");
    assertTrue(result.isValid());
    assertTrue(result.isIdentical());
    assertTrue(result.getDifferences().isEmpty());
    assertEquals("Documents are key-identical!!!", result.getValidationMessage());
  }

  @Test
  void diffJsonService_whenInvalidJson_reportsParserError() {
    DiffEntity result = diff("{\"a\":1", "{\"a\":1}");
    assertFalse(result.isValid());
    assertFalse(result.isIdentical());
    assertTrue(result.getDifferences() == null || result.getDifferences().isEmpty());
    assertTrue(result.getValidationMessage().length() > 0);
  }

  @Test
  void diffJsonService_whenListsDiffer_reportsIndexEntries() {
    DiffEntity result = diff("[1,2,3]", "[1,9]");
    assertTrue(result.isValid());
    assertEquals(2, result.getDifferences().size());
    assertEquals("~ $[1]: expected 2, found 9", result.getDifferences().get(0));
    assertEquals("- $[2]: removed 3", result.getDifferences().get(1));
  }

  @Test
  void diffJsonService_whenListGrows_reportsAddedIndexEntries() {
    DiffEntity result = diff("[1]", "[1,2,3]");
    assertEquals(2, result.getDifferences().size());
    assertEquals("+ $[1]: added 2", result.getDifferences().get(0));
    assertEquals("+ $[2]: added 3", result.getDifferences().get(1));
  }

  @Test
  void diffJsonService_whenStructureTypeChanges_reportsTypeDifference() {
    DiffEntity result = diff("{\"a\":1}", "{\"a\":{\"b\":2}}");
    assertTrue(result.isValid());
    assertEquals(1, result.getDifferences().size());
    assertEquals("~ $.a: expected 1, found {b=2}", result.getDifferences().get(0));
  }

  @Test
  void diffJsonService_whenScalarVsList_reportsTypeDifference() {
    DiffEntity result = diff("[1]", "{\"a\":1}");
    assertEquals(1, result.getDifferences().size());
    assertEquals("~ $: expected [1], found {a=1}", result.getDifferences().get(0));
  }

  @Test
  void diffJsonService_whenValuesAreStrings_theyAreQuotedAndTruncated() {
    String longValue = "0123456789012345678901234567890123456789012345";
    DiffEntity result = diff("{\"a\":\"" + longValue + "\"}", "{\"a\":\"short\"}");
    assertEquals(1, result.getDifferences().size());
    assertEquals("~ $.a: expected \"012345678901234567890123456789012345678..., found \"short\"",
        result.getDifferences().get(0));
  }

  @Test
  void diffYamlService_whenKeysDiffer_reportsChanges() {
    DiffEntity result = validatorService
        .diffYamlService(DiffEntity.builder().inputMessage("server:\n  port: 8090\nlogging:\n  level: info\n")
            .compareMessage("server:\n  port: 80\n").build());
    assertTrue(result.isValid());
    assertFalse(result.isIdentical());
    assertEquals(2, result.getDifferences().size());
    assertEquals("~ $.server.port: expected 8090, found 80", result.getDifferences().get(0));
    assertEquals("- $.logging: removed {level=info}", result.getDifferences().get(1));
  }

  @Test
  void diffYamlService_whenIdentical_reportsKeyIdentical() {
    DiffEntity result = validatorService.diffYamlService(DiffEntity.builder().inputMessage("a: 1\nb:\n  - x\n  - y\n")
        .compareMessage("b:\n  - x\n  - y\na: 1\n").build());
    assertTrue(result.isValid());
    assertTrue(result.isIdentical());
    assertEquals("Documents are key-identical!!!", result.getValidationMessage());
  }

  @Test
  void diffYamlService_whenInvalidYaml_reportsParserError() {
    DiffEntity result = validatorService
        .diffYamlService(DiffEntity.builder().inputMessage("Wrong : Data :").compareMessage("a: 1\n").build());
    assertFalse(result.isValid());
    assertTrue(result.getValidationMessage().length() > 0);
  }
}
