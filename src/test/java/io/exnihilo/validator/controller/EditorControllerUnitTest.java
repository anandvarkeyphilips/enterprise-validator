package io.exnihilo.validator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.IValidatorService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class EditorControllerUnitTest {

    private MockMvc mockMvc;
    @Mock
    private IValidatorService validatorServiceMock;
    @InjectMocks
    private EditorController editorController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(editorController).build();
    }

    @Test
    public void validateYamlController_whenValidYaml_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-yaml.yml");
        String inputYamlData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputYamlData).valid(true).validationMessage("Valid YAML").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).validateYamlService(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/yaml")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Valid YAML")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).validateYamlService(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }

    @Test
    public void validateYamlController_whenInvalidYaml_returnFalse() throws Exception {
        String inputYamlData = "Wrong : Data :";
        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputYamlData).valid(false).build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).validateYamlService(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);

        mockMvc.perform(
                post("/yaml")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.not("Valid YAML")))
                .andExpect(jsonPath("$.valid", Matchers.is(false)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));
    }

    @Test
    public void validateJsonController_whenValidYaml_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-json.json");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder(inputJsonData).valid(true).validationMessage("Valid JSON!!!").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).validateJsonService(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Valid JSON!!!")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).validateJsonService(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }

    @Test
    public void formatJsonController_whenValidJson_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-json.json");
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
                "}").valid(true).validationMessage("Formatted JSON!!!").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).formatJsonService(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/formatJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Formatted JSON!!!")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).formatJsonService(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }

    @Test
    public void formatXmlController_whenValidXml_returnTrue() throws Exception {
        File file = ResourceUtils.getFile("classpath:valid-xml.xml");
        String inputJsonData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
                "<book>\n" +
                "    <title>java book</title>\n" +
                "    <author>nick bore</author>\n" +
                "    <pages>1020</pages>\n" +
                "    <example>xml - valid xml file</example>\n" +
                "</book>\n").valid(true).validationMessage("Formatted XML!!!").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).formatXmlService(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/formatXml")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Formatted XML!!!")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).formatXmlService(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }

    @Test
    public void base64EncodeController_whenValidData_returnTrue() throws Exception {
        String inputJsonData = "Anand";

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder("QW5hbmQ=").valid(true).validationMessage("Encode Successful!!!").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).encodeBase64(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/base64Encode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Encode Successful!!!")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).encodeBase64(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }

    @Test
    public void base64DecodeController_whenValidData_returnTrue() throws Exception {
        String inputJsonData = "QW5hbmQ=";

        ValidationEntity inputValidationEntity = ValidationEntity.builder(inputJsonData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder("Anand").valid(true).validationMessage("Decode Successful!!!").build();
        Mockito.doReturn(outputValidationEntity).when(validatorServiceMock).decodeBase64(inputValidationEntity);

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);
        mockMvc.perform(
                post("/base64Decode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectJsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validationMessage").value(Matchers.is("Decode Successful!!!")))
                .andExpect(jsonPath("$.valid", Matchers.is(true)))
                .andExpect(jsonPath("$.*", Matchers.hasSize(5)));

        ArgumentCaptor<ValidationEntity> validatorServiceArgumentCaptor = ArgumentCaptor.forClass(ValidationEntity.class);
        verify(validatorServiceMock, times(1)).decodeBase64(validatorServiceArgumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(validatorServiceMock);

        ValidationEntity captorValue = validatorServiceArgumentCaptor.getValue();
        Assert.assertThat(captorValue, is(inputValidationEntity));
    }
}