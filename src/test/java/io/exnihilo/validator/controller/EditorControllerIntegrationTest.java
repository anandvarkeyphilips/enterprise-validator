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
public class EditorControllerIntegrationTest {

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
}