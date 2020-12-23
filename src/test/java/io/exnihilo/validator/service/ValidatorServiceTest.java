package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

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

  @Before
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

    Assert.assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
  }

  @Test
  public void validateYamlService_whenInvalidYaml_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-yaml.yml");
    String inputYamlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).valid(false)
        .validationMessage("mapping values are not allowed here\n" + " in 'string', line 7, column 27:\n"
            + "       pid.fail-on-write-error: true\n" + "                              ^\n")
        .lineNumber(7).columnNumber(27).build();

    Assert.assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
  }

  @Test
  public void validateJsonService_whenValidJson_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputJsonData).valid(true).validationMessage("Valid JSON!!!").build();

    Assert.assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
  }

  @Test
  public void validateJsonService_whenInvalidJson_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).valid(false)
        .validationMessage("Expected a ',' or '}' at 129 [character 21 line 7]").lineNumber(7).columnNumber(21).build();

    Assert.assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
  }

  @Test
  public void formatJsonService_whenValidJson_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:minified-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("{\n" + "    \"glossary\": {\n"
        + "        \"title\": \"example glossary\",\n" + "        \"GlossDiv\": {\n" + "            \"title\": \"S\",\n"
        + "            \"GlossList\": {\n" + "                \"GlossEntry\": {\n"
        + "                    \"ID\": \"SGML\",\n" + "                    \"SortAs\": \"SGML\",\n"
        + "                    \"GlossTerm\": \"Standard Generalized Markup Language\",\n"
        + "                    \"Acronym\": \"SGML\",\n" + "                    \"Abbrev\": \"ISO 8879:1986\",\n"
        + "                    \"GlossDef\": {\n"
        + "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
        + "                        \"GlossSeeAlso\": [\n" + "                            \"GML\",\n"
        + "                            \"XML\"\n" + "                        ]\n" + "                    },\n"
        + "                    \"GlossSee\": \"markup\"\n" + "                }\n" + "            }\n" + "        }\n"
        + "    }\n" + "}").valid(true).validationMessage("Formatted JSON!!!").build();

    Assert.assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
  }

  @Test
  public void formatJsonService_whenInvalidJson_returnFalse() throws Exception {
    File file = ResourceUtils.getFile("classpath:invalid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).valid(false)
        .validationMessage("Expected a ',' or '}' at 129 [character 21 line 7]").lineNumber(7).columnNumber(21).build();

    Assert.assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
  }

  @Test
  public void formatXmlService_whenValidXml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-xml.xml");
    String inputXmlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputXmlData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder()
        .inputMessage("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" + "<book>\n" + "    <title>java book</title>\n"
            + "    <author>nick bore</author>\n" + "    <pages>1020</pages>\n"
            + "    <example>xml - valid xml file</example>\n" + "</book>\n")
        .valid(true).validationMessage("Formatted XML!!!").build();

    Assert.assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
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

    Assert.assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
  }

  @Test
  public void encodeBase64_whenValidContent_returnTrue() throws Exception {
    String data = "Anand";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(data).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("QW5hbmQ=").valid(true)
        .validationMessage("Encode Successful!!!").build();

    Assert.assertEquals(outputValidationEntity, validatorService.encodeBase64(inputValidationEntity));
  }

  @Test
  public void decodeBase64_whenValidContent_returnTrue() throws Exception {
    String data = "QW5hbmQ=";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(data).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage("Anand").valid(true).validationMessage("Decode Successful!!!").build();

    Assert.assertEquals(outputValidationEntity, validatorService.decodeBase64(inputValidationEntity));
  }
}
