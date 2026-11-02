package io.exnihilo.validator.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.IValidatorService;
import java.io.File;
import java.nio.file.Files;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

@ExtendWith(MockitoExtension.class)
public class EditorControllerUnitTest {

  private MockMvc mockMvc;
  @Mock
  private IValidatorService validatorServiceMock;
  @InjectMocks
  private EditorController editorController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(editorController).build();
  }

  @Test
  public void index_whenRootUrl_ThenRedirectToEditor() throws Exception {
    mockMvc.perform(get("/")).andDo(MockMvcResultHandlers.print()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/editor"));
  }

  @Test
  public void validateYamlController_whenValidYaml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-yaml.yml");
    String inputYamlData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputYamlData).valid(true).validationMessage("Valid YAML").build();
    doReturn(outputValidationEntity).when(validatorServiceMock).validateYamlService(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/yaml").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Valid YAML")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).validateYamlService(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }

  @Test
  public void validateYamlController_whenInvalidYaml_returnFalse() throws Exception {
    String inputYamlData = "Wrong : Data :";
    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputYamlData).valid(false).build();
    doReturn(outputValidationEntity).when(validatorServiceMock).validateYamlService(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);

    mockMvc.perform(post("/yaml").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.not("Valid YAML")))
        .andExpect(jsonPath("$.valid", Matchers.is(false))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));
  }

  @Test
  public void validateJsonController_whenValidYaml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-json.json");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage(inputJsonData).valid(true).validationMessage("Valid JSON!!!").build();
    doReturn(outputValidationEntity).when(validatorServiceMock).validateJsonService(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/json").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Valid JSON!!!")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).validateJsonService(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }

  @Test
  public void formatJsonController_whenValidJson_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-json.json");
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
    doReturn(outputValidationEntity).when(validatorServiceMock).formatJsonService(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/formatJson").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Formatted JSON!!!")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).formatJsonService(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }

  @Test
  public void formatXmlController_whenValidXml_returnTrue() throws Exception {
    File file = ResourceUtils.getFile("classpath:valid-xml.xml");
    String inputJsonData = new String(Files.readAllBytes(file.toPath()));

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("""
        <?xml version="1.0" encoding="UTF-16"?><book>
            <title>java book</title>
            <author>nick bore</author>
            <pages>1020</pages>
            <example>xml - valid xml file</example>
        </book>
        """).valid(true).validationMessage("Formatted XML!!!").build();
    doReturn(outputValidationEntity).when(validatorServiceMock).formatXmlService(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/formatXml").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Formatted XML!!!")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).formatXmlService(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }

  @Test
  public void base64EncodeController_whenValidData_returnTrue() throws Exception {
    String inputJsonData = "Anand";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity = ValidationEntity.builder().inputMessage("QW5hbmQ=").valid(true)
        .validationMessage("Encode Successful!!!").build();
    doReturn(outputValidationEntity).when(validatorServiceMock).encodeBase64(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/base64Encode").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Encode Successful!!!")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).encodeBase64(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }

  @Test
  public void base64DecodeController_whenValidData_returnTrue() throws Exception {
    String inputJsonData = "QW5hbmQ=";

    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputJsonData).build();
    ValidationEntity outputValidationEntity =
        ValidationEntity.builder().inputMessage("Anand").valid(true).validationMessage("Decode Successful!!!").build();
    doReturn(outputValidationEntity).when(validatorServiceMock).decodeBase64(inputValidationEntity);

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
    mockMvc.perform(post("/base64Decode").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Decode Successful!!!")))
        .andExpect(jsonPath("$.valid", Matchers.is(true))).andExpect(jsonPath("$.*", Matchers.hasSize(5)));

    ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
    verify(validatorServiceMock, times(1)).decodeBase64(validatorServiceArgumentCaptor.capture());
    Mockito.verifyNoMoreInteractions(validatorServiceMock);

    ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
    MatcherAssert.assertThat(captorValue, is(inputValidationEntity));
  }
}
