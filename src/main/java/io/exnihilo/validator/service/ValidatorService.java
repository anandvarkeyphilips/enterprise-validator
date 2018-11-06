package io.exnihilo.validator.service;

import io.exnihilo.validator.entity.JSONOrderedObject;
import io.exnihilo.validator.entity.ValidationEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator Service Class handles the functional aspects of all configured validators.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Slf4j
@Service
public class ValidatorService {

    @Lookup
    public ValidationEntity getPrototypeBean() {
        return null;
    }

    /**
     * Splitting of yaml data in case of "---", then validating each part separately,
     * and then returning success data or line and column numbers in case of failure.
     *
     * @param yamlData
     * @return validation result
     */
    public ValidationEntity validateYamlService(String yamlData) {
        ValidationEntity validationEntity = getPrototypeBean();
        validationEntity.setInputMessage(yamlData);
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> obj = yaml.load(yamlData.replace("---", ""));
            log.debug("YAML Value obtained successfully: {}", obj.toString());
            validationEntity.setValidationMessage("Valid YAML!!!");
        } catch (Exception e) {
            validationEntity.setValidationMessage(e.getMessage());
            log.error("Exception occurred in validation: ", e);
            if (e.getMessage().contains("line ")) {
                String pattern1 = "line ";
                String pattern2 = ",";
                Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                Matcher m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setLineNumber(Integer.parseInt(m.group(1)));
                }
                pattern1 = "column ";
                pattern2 = ":";
                p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setColumnNumber(Integer.parseInt(m.group(1)));
                }
            }
        } finally {
            return validationEntity;
        }
    }

    public ValidationEntity validateJsonService(String json) {
        ValidationEntity validationEntity = getPrototypeBean();
        validationEntity.setInputMessage(json);
        try {
            String indentedJson = (new JSONOrderedObject(json)).toString(4);
            log.debug("JSON Value obtained successfully: {}", indentedJson);
            validationEntity.setValidationMessage("Valid JSON!!!");
        } catch (JSONException e) {
            validationEntity.setValidationMessage(e.getMessage());
            log.error("Exception occurred in validation: ", e);
            if (e.getMessage().contains("line ")) {
                String pattern1 = "line ";
                String pattern2 = "]";
                Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                Matcher m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setLineNumber(Integer.parseInt(m.group(1)));
                }
                pattern1 = "[character ";
                pattern2 = " line";
                p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setColumnNumber(Integer.parseInt(m.group(1)));
                }
            }
        } finally {
            return validationEntity;
        }
    }

    public ValidationEntity formatJsonService(String json) {
        ValidationEntity validationEntity = getPrototypeBean();
        validationEntity.setInputMessage(json);
        try {
            String indentedJson = (new JSONOrderedObject(json)).toString(4);
            log.debug("JSON Value obtained successfully: {}", indentedJson);
            validationEntity.setInputMessage(indentedJson);
            validationEntity.setValidationMessage("Valid JSON!!!");
        } catch (JSONException e) {
            validationEntity.setValidationMessage(e.getMessage());
            log.error("Exception occurred in validation: ", e);
            if (e.getMessage().contains("line ")) {
                String pattern1 = "line ";
                String pattern2 = "]";
                Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                Matcher m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setLineNumber(Integer.parseInt(m.group(1)));
                }
                pattern1 = "[character ";
                pattern2 = " line";
                p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                m = p.matcher(e.getMessage());
                while (m.find()) {
                    validationEntity.setColumnNumber(Integer.parseInt(m.group(1)));
                }
            }
        } finally {
            return validationEntity;
        }
    }
}