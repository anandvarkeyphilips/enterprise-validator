package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Validator Service Class handles the functional aspects of all configured validators.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Slf4j
public class ValidatorServiceTest {

  private IValidatorService validatorService;

  @BeforeEach
  public void setUp() {
    validatorService = new ValidatorService();
  }

  @Test
  public void validateYamlService_whenValidYaml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-yaml.yml");
    String inputYamlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputYamlData).valid(true).validationMessage("Valid YAML!!!").build();

    assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
  }

  @Test
  public void validateYamlService_whenInvalidYaml_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-yaml.yml");
    String inputYamlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputYamlData).valid(false).validationMessage("""
            mapping values are not allowed here
             in 'string', line 7, column 27:
                   pid.fail-on-write-error: true
                                          ^
            """).lineNumber(7).columnNumber(27).build();

    assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
  }

  @Test
  public void validateJsonService_whenValidJson_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputJsonData).valid(true).validationMessage("Valid JSON!!!").build();

    assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
  }

  @Test
  public void validateJsonService_whenInvalidJson_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).valid(false)
        .validationMessage("Expected a ',' or '}' at 129 [character 21 line 7]").lineNumber(7).columnNumber(21).build();

    assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
  }

  @Test
  public void formatJsonService_whenValidJson_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:minified-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("""
        {
            "glossary": {
                "title": "example glossary",
                "GlossDiv": {
                    "title": "S",
                    "GlossList": {
                        "GlossEntry": {
                            "ID": "SGML",
                            "SortAs": "SGML",
                            "GlossTerm": "Standard Generalized Markup Language",
                            "Acronym": "SGML",
                            "Abbrev": "ISO 8879:1986",
                            "GlossDef": {
                                "para": "A meta-markup language, used to create markup languages such as DocBook.",
                                "GlossSeeAlso": [
                                    "GML",
                                    "XML"
                                ]
                            },
                            "GlossSee": "markup"
                        }
                    }
                }
            }
        }""").valid(true).validationMessage("Formatted JSON!!!").build();

    assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
  }

  @Test
  public void formatJsonService_whenInvalidJson_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).valid(false)
        .validationMessage("Expected a ',' or '}' at 129 [character 21 line 7]").lineNumber(7).columnNumber(21).build();

    assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
  }

  @Test
  public void formatXmlService_whenValidXml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-xml.xml");
    String inputXmlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputXmlData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("""
        <?xml version="1.0" encoding="UTF-16"?><book>
            <title>java book</title>
            <author>nick bore</author>
            <pages>1020</pages>
            <example>xml - valid xml file</example>
        </book>
        """).valid(true).validationMessage("Formatted XML!!!").build();

    assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
  }

  @Test
  public void formatXmlService_whenInvalidXml_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-xml.xml");
    String inputXmlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputXmlData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputXmlData).valid(false)
            .validationMessage(
                "Element type \"pages1020\" must be followed by either attribute specifications, \">\" or \"/>\".")
            .build();

    assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
  }

  @Test
  public void encodeBase64_whenValidContent_returnTrue() throws Exception {
    String data = "Anand";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(data).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("QW5hbmQ=").valid(true)
        .validationMessage("Encode Successful!!!").build();

    assertEquals(outputValidationEntity, validatorService.encodeBase64(inputValidationEntity));
  }

  @Test
  public void decodeBase64_whenValidContent_returnTrue() throws Exception {
    String data = "QW5hbmQ=";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(data).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage("Anand").valid(true).validationMessage("Decode Successful!!!").build();

    assertEquals(outputValidationEntity, validatorService.decodeBase64(inputValidationEntity));
  }
}
