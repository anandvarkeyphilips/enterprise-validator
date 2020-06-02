package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
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

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputYamlData).valid(true).validationMessage("Valid YAML!!!").build();

        Assert.assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
    }

    @Test
    public void validateYamlService_whenInvalidYaml_returnFalse() throws Exception {
        File file = ResourceUtils.getFile("classpath:invalid-yaml.yml");
        String inputYamlData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputYamlData).valid(false).validationMessage("mapping values are not allowed here\n" +
                " in 'string', line 7, column 27:\n" +
                "       pid.fail-on-write-error: true\n" +
                "                              ^\n")
                .lineNumber(7)
                .columnNumber(27)
                .build();

        Assert.assertEquals(outputValidationEntity, validatorService.validateYamlService(inputValidationEntity));
    }

    @Test
    public void validateJsonService_whenValidJson_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-json.json");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputJsonData).valid(true).validationMessage("Valid JSON!!!").build();

        Assert.assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
    }

    @Test
    public void validateJsonService_whenInvalidJson_returnFalse() throws Exception {
        File file = ResourceUtils.getFile("classpath:invalid-json.json");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputJsonData).valid(false).validationMessage("Expected a ',' or '}' at 135 [character 21 line 7]")
                .lineNumber(7)
                .columnNumber(21)
                .build();

        Assert.assertEquals(outputValidationEntity, validatorService.validateJsonService(inputValidationEntity));
    }

    @Test
    public void formatJsonService_whenValidJson_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:minified-json.json");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder("{\n" +
                "    \"glossary\": {\n" +
                "        \"title\": \"example glossary\",\n" +
                "        \"GlossDiv\": {\n" +
                "            \"title\": \"S\",\n" +
                "            \"GlossList\": {\n" +
                "                \"GlossEntry\": {\n" +
                "                    \"ID\": \"SGML\",\n" +
                "                    \"SortAs\": \"SGML\",\n" +
                "                    \"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
                "                    \"Acronym\": \"SGML\",\n" +
                "                    \"Abbrev\": \"ISO 8879:1986\",\n" +
                "                    \"GlossDef\": {\n" +
                "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                "                        \"GlossSeeAlso\": [\n" +
                "                            \"GML\",\n" +
                "                            \"XML\"\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    \"GlossSee\": \"markup\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}").valid(true).validationMessage("Valid JSON!!!").build();

        Assert.assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
    }

    @Test
    public void formatJsonService_whenInvalidJson_returnFalse() throws Exception {
        File file = ResourceUtils.getFile("classpath:invalid-json.json");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputJsonData).valid(false).validationMessage("Expected a ',' or '}' at 135 [character 21 line 7]")
                .lineNumber(7)
                .columnNumber(21)
                .build();

        Assert.assertEquals(outputValidationEntity, validatorService.formatJsonService(inputValidationEntity));
    }

    @Test
    public void formatXmlService_whenValidXml_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-xml.xml");
        String inputXmlData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputXmlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
                "<book>\n" +
                "    <title>java book</title>\n" +
                "    <author>nick bore</author>\n" +
                "    <pages>1020</pages>\n" +
                "    <example>xml - valid xml file</example>\n" +
                "</book>\n").valid(true).validationMessage("Formatted XML!!!").build();

        Assert.assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
    }

    @Test
    public void formatXmlService_whenInvalidXml_returnFalse() throws Exception {
        File file = ResourceUtils.getFile("classpath:invalid-xml.xml");
        String inputXmlData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputXmlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputXmlData).valid(false).validationMessage(
                "Element type \"pages1020\" must be followed by either attribute specifications, \">\" or \"/>\".")
                .build();

        Assert.assertEquals(outputValidationEntity, validatorService.formatXmlService(inputValidationEntity));
    }

    /**
     * Encodes data in Base64 format and returns error details in case of failure.
     *
     * @param validationEntity
     * @return validationEntity
     */
    public ValidationEntity encodeBase64(ValidationEntity validationEntity) {
        try {
            String encodedBytes = Base64.getEncoder().encodeToString(validationEntity.getInputMessage().getBytes(StandardCharsets.UTF_8));
            log.debug("encodeBase64 completed successfully: {}", encodedBytes);
            validationEntity.setValid(true);
            validationEntity.setInputMessage(encodedBytes);
            validationEntity.setValidationMessage("Encode Successful!!!");
        } catch (Exception e) {
            validationEntity.setValidationMessage(e.getMessage());
            log.error("Exception occurred in Encoding: ", e);
        } finally {
            return validationEntity;
        }
    }

    /**
     * Decodes data from Base64 format and returns error details in case of failure.
     *
     * @param validationEntity
     * @return validationEntity
     */
    public ValidationEntity decodeBase64(ValidationEntity validationEntity) {
        try {
            String decodedString = new String(Base64.getDecoder().decode(validationEntity.getInputMessage()));
            log.debug("decodeBase64 completed successfully: {}", decodedString);
            validationEntity.setValid(true);
            validationEntity.setInputMessage(decodedString);
            validationEntity.setValidationMessage("Decode Successful!!!");
        } catch (Exception e) {
            validationEntity.setValidationMessage(e.getMessage());
            log.error("Exception occurred in Decoding: ", e);
        } finally {
            return validationEntity;
        }
    }
}