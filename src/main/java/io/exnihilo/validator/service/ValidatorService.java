package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.ValidationEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static io.exnihilo.validator.constants.EnterpriseValidatorConstants.*;
import static io.exnihilo.validator.util.Utils.getNumberFromRegexPattern;

/**
 * Validator Service Class handles the functional aspects of all configured validators.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Slf4j
@Service
public class ValidatorService implements IValidatorService {

  /**
   * Splits yaml data in case of multiple documents "---" and validates each part, and then returns
   * error message, line and column numbers in case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity validateYamlService(ValidationEntity validationEntity) {
    try {
      Yaml yaml = new Yaml();
      Map<String, Object> obj = yaml.load(validationEntity.getInputMessage().replace("---", ""));
      log.debug("YAML Value obtained successfully: {}", obj.toString());
      validationEntity.setValid(true);
      validationEntity.setValidationMessage("Valid YAML!!!");
    } catch (Exception e) {
      validationEntity.setValidationMessage(e.getMessage());
      log.error(EXCEPTION_IN_VALIDATION_MESSAGE, e);
      if (e.getMessage().contains(LINE_WITH_SPACE)) {
        validationEntity.setLineNumber(getNumberFromRegexPattern(LINE_WITH_SPACE, ",", e.getMessage()));
        validationEntity.setColumnNumber(getNumberFromRegexPattern(COLUMN_WITH_SPACE, ":", e.getMessage()));
      }
    }
    return validationEntity;
  }

  /**
   * Validates the format of json data string and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity validateJsonService(ValidationEntity validationEntity) {
    try {
      String indentedJson = (new JSONObject(validationEntity.getInputMessage())).toString(4);
      log.debug("JSON Value obtained successfully: {}", indentedJson);
      validationEntity.setValid(true);
      validationEntity.setValidationMessage("Valid JSON!!!");
    } catch (JSONException e) {
      validationEntity.setValidationMessage(e.getMessage());
      log.error(EXCEPTION_IN_VALIDATION_MESSAGE, e);
      if (e.getMessage().contains(LINE_WITH_SPACE)) {
        validationEntity.setLineNumber(getNumberFromRegexPattern(LINE_WITH_SPACE, "]", e.getMessage()));
        validationEntity.setColumnNumber(getNumberFromRegexPattern("[character ", " line", e.getMessage()));
      }
    }
    return validationEntity;
  }

  /**
   * Validates the format of json data string and returns formatted json along with error details in
   * case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity formatJsonService(ValidationEntity validationEntity) {
    try {
      String indentedJson = (new JSONObject(validationEntity.getInputMessage())).toString(4);
      log.debug("JSON Value formatted successfully: {}", indentedJson);
      validationEntity.setValid(true);
      validationEntity.setInputMessage(indentedJson);
      validationEntity.setValidationMessage("Formatted JSON!!!");
    } catch (JSONException e) {
      validationEntity.setValidationMessage(e.getMessage());
      log.error(EXCEPTION_IN_VALIDATION_MESSAGE, e);
      if (e.getMessage().contains(LINE_WITH_SPACE)) {
        validationEntity.setLineNumber(getNumberFromRegexPattern(LINE_WITH_SPACE, "]", e.getMessage()));
        validationEntity.setColumnNumber(getNumberFromRegexPattern("[character ", " line", e.getMessage()));
      }
    }
    return validationEntity;
  }

  /**
   * Validates the format of xml data string and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity formatXmlService(ValidationEntity validationEntity) {
    try {
      final InputSource src = new InputSource(new StringReader(validationEntity.getInputMessage()));
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      final Node document = documentBuilderFactory.newDocumentBuilder().parse(src).getDocumentElement();
      final Boolean keepDeclaration = Boolean.valueOf(validationEntity.getInputMessage().startsWith("<?xml"));
      final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
      final LSSerializer writer = impl.createLSSerializer();

      // Set this to true if the output needs to be beautified.
      writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
      // Set this to true if the declaration is needed to be outputted.
      writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

      log.debug("XML Value formatted successfully: {}", writer.writeToString(document));
      validationEntity.setValid(true);
      validationEntity.setInputMessage(writer.writeToString(document));
      validationEntity.setValidationMessage("Formatted XML!!!");
    } catch (Exception e) {
      validationEntity.setValidationMessage(e.getMessage());
      log.error(EXCEPTION_IN_VALIDATION_MESSAGE, e);
    }
    return validationEntity;
  }

  /**
   * Encodes data in Base64 format and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity encodeBase64(ValidationEntity validationEntity) {
    String encodedBytes =
        Base64.getEncoder().encodeToString(validationEntity.getInputMessage().getBytes(StandardCharsets.UTF_8));
    log.debug("encodeBase64 completed successfully: {}", encodedBytes);
    validationEntity.setValid(true);
    validationEntity.setInputMessage(encodedBytes);
    validationEntity.setValidationMessage("Encode Successful!!!");
    return validationEntity;
  }

  /**
   * Decodes data from Base64 format and returns error details in case of failure.
   *
   * @param validationEntity
   * @return validationEntity
   */
  public ValidationEntity decodeBase64(ValidationEntity validationEntity) {
    String decodedString = new String(Base64.getDecoder().decode(validationEntity.getInputMessage()));
    log.debug("decodeBase64 completed successfully: {}", decodedString);
    validationEntity.setValid(true);
    validationEntity.setInputMessage(decodedString);
    validationEntity.setValidationMessage("Decode Successful!!!");
    return validationEntity;
  }
}
