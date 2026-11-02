package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.DiffEntity;
import io.exnihilo.validator.entity.ValidationEntity;
import org.json.JSONException;
import org.json.JSONTokener;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.exnihilo.validator.constants.EnterpriseValidatorConstants.*;
import static io.exnihilo.validator.util.Utils.getNumberFromRegexPattern;

/**
 * Validator Service Class handles the functional aspects of all configured validators.
 *
 * @author Anand Varkey Philips
 * @since 2.0.0.RELEASE
 */
@Service
public class ValidatorService implements IValidatorService {

  /**
   * Splits yaml data in case of multiple documents "---" and validates each part, and then returns
   * error message, line and column numbers in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  public ValidationEntity validateYamlService(ValidationEntity validationEntity) {
    try {
      Yaml yaml = new Yaml();
      Map<String, Object> obj = yaml.load(validationEntity.getInputMessage().replace("---", ""));
      validationEntity.setValid(true);
      validationEntity.setValidationMessage("Valid YAML!!!");
    } catch (Exception e) {
      validationEntity.setValidationMessage(e.getMessage());
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
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  public ValidationEntity validateJsonService(ValidationEntity validationEntity) {
    try {
      new JSONObject(validationEntity.getInputMessage());
      validationEntity.setValid(true);
      validationEntity.setValidationMessage("Valid JSON!!!");
    } catch (JSONException e) {
      validationEntity.setValidationMessage(e.getMessage());
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
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  public ValidationEntity formatJsonService(ValidationEntity validationEntity) {
    try {
      String indentedJson = (new JSONObject(validationEntity.getInputMessage())).toString(4);
      validationEntity.setValid(true);
      validationEntity.setInputMessage(indentedJson);
      validationEntity.setValidationMessage("Formatted JSON!!!");
    } catch (JSONException e) {
      validationEntity.setValidationMessage(e.getMessage());
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
   * @param validationEntity Model entity for all api requests and responses
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

      validationEntity.setValid(true);
      validationEntity.setInputMessage(writer.writeToString(document));
      validationEntity.setValidationMessage("Formatted XML!!!");
    } catch (Exception e) {
      validationEntity.setValidationMessage(e.getMessage());
    }
    return validationEntity;
  }

  /**
   * Encodes data in Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  public ValidationEntity encodeBase64(ValidationEntity validationEntity) {
    String encodedBytes =
        Base64.getEncoder().encodeToString(validationEntity.getInputMessage().getBytes(StandardCharsets.UTF_8));
    validationEntity.setValid(true);
    validationEntity.setInputMessage(encodedBytes);
    validationEntity.setValidationMessage("Encode Successful!!!");
    return validationEntity;
  }

  /**
   * Decodes data from Base64 format and returns error details in case of failure.
   *
   * @param validationEntity Model entity for all api requests and responses
   * @return validationEntity
   */
  public ValidationEntity decodeBase64(ValidationEntity validationEntity) {
    String decodedString = new String(Base64.getDecoder().decode(validationEntity.getInputMessage()));
    validationEntity.setValid(true);
    validationEntity.setInputMessage(decodedString);
    validationEntity.setValidationMessage("Decode Successful!!!");
    return validationEntity;
  }

  /**
   * Compares two json documents by keys and reports added, removed and changed key paths.
   *
   * @param diffEntity holds the base document (inputMessage) and the comparison document
   *        (compareMessage)
   * @return diffEntity with the list of differences
   */
  public DiffEntity diffJsonService(DiffEntity diffEntity) {
    try {
      Object left = jsonToJava(new JSONTokener(diffEntity.getInputMessage()).nextValue());
      Object right = jsonToJava(new JSONTokener(diffEntity.getCompareMessage()).nextValue());
      return diffStructures(diffEntity, left, right);
    } catch (JSONException e) {
      diffEntity.setValid(false);
      diffEntity.setValidationMessage(e.getMessage());
      return diffEntity;
    }
  }

  /**
   * Compares two yaml documents by keys and reports added, removed and changed key paths.
   *
   * @param diffEntity holds the base document (inputMessage) and the comparison document
   *        (compareMessage)
   * @return diffEntity with the list of differences
   */
  public DiffEntity diffYamlService(DiffEntity diffEntity) {
    try {
      Yaml yaml = new Yaml();
      Object left = yaml.load(diffEntity.getInputMessage().replace("---", ""));
      Object right = yaml.load(diffEntity.getCompareMessage().replace("---", ""));
      return diffStructures(diffEntity, left, right);
    } catch (Exception e) {
      diffEntity.setValid(false);
      diffEntity.setValidationMessage(e.getMessage());
      return diffEntity;
    }
  }

  private static DiffEntity diffStructures(DiffEntity diffEntity, Object left, Object right) {
    List<String> differences = new ArrayList<>();
    diffAny("$", left, right, differences);
    diffEntity.setValid(true);
    diffEntity.setIdentical(differences.isEmpty());
    diffEntity.setDifferences(differences);
    diffEntity.setValidationMessage(
        differences.isEmpty() ? "Documents are key-identical!!!" : differences.size() + " key difference(s) found!!!");
    return diffEntity;
  }

  /**
   * Recursively compares normalized structures. Maps are compared by key (order-insensitive), lists
   * by index, scalars by value; differences are recorded as "+ path: added value" (present only in
   * the comparison document), "- path: removed value" (present only in the base document) and "~
   * path: expected x, found y".
   */
  private static void diffAny(String path, Object left, Object right, List<String> differences) {
    if (left instanceof Map && right instanceof Map) {
      diffMaps(path, (Map<?, ?>) left, (Map<?, ?>) right, differences);
    } else if (left instanceof List && right instanceof List) {
      diffLists(path, (List<?>) left, (List<?>) right, differences);
    } else if (left instanceof Map || left instanceof List || right instanceof Map || right instanceof List) {
      differences.add("~ " + path + ": expected " + describe(left) + ", found " + describe(right));
    } else if (!java.util.Objects.equals(left, right)) {
      differences.add("~ " + path + ": expected " + describe(left) + ", found " + describe(right));
    }
  }

  private static void diffMaps(String path, Map<?, ?> left, Map<?, ?> right, List<String> differences) {
    Map<Object, Object> keys = new LinkedHashMap<>();
    left.keySet().forEach(key -> keys.put(key, "left"));
    right.keySet().forEach(key -> keys.put(key, keys.containsKey(key) ? "both" : "right"));
    for (Map.Entry<Object, Object> entry : keys.entrySet()) {
      Object key = entry.getKey();
      String childPath = path + "." + key;
      if ("left".equals(entry.getValue())) {
        differences.add("- " + childPath + ": removed " + describe(left.get(key)));
      } else if ("right".equals(entry.getValue())) {
        differences.add("+ " + childPath + ": added " + describe(right.get(key)));
      } else {
        diffAny(childPath, left.get(key), right.get(key), differences);
      }
    }
  }

  private static void diffLists(String path, List<?> left, List<?> right, List<String> differences) {
    int common = Math.min(left.size(), right.size());
    for (int i = 0; i < common; i++) {
      diffAny(path + "[" + i + "]", left.get(i), right.get(i), differences);
    }
    for (int i = common; i < left.size(); i++) {
      differences.add("- " + path + "[" + i + "]: removed " + describe(left.get(i)));
    }
    for (int i = common; i < right.size(); i++) {
      differences.add("+ " + path + "[" + i + "]: added " + describe(right.get(i)));
    }
  }

  /**
   * Converts org.json values to plain Java structures while preserving the fork's insertion order via
   * LinkedHashMap.
   */
  private static Object jsonToJava(Object value) {
    if (value instanceof JSONObject) {
      Map<String, Object> map = new LinkedHashMap<>();
      for (String key : ((JSONObject) value).keySet()) {
        map.put(key, jsonToJava(((JSONObject) value).get(key)));
      }
      return map;
    }
    if (value instanceof org.json.JSONArray) {
      List<Object> list = new ArrayList<>();
      for (Object item : (org.json.JSONArray) value) {
        list.add(jsonToJava(item));
      }
      return list;
    }
    return value;
  }

  private static String describe(Object value) {
    String text = String.valueOf(value);
    if (value instanceof String) {
      text = "\"" + text + "\"";
    }
    return text.length() > 40 ? text.substring(0, 40) + "..." : text;
  }
}
