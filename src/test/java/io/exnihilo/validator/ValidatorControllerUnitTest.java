package io.exnihilo.validator;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.validator.controller.EditorController;
import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.ValidatorService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class ValidatorControllerUnitTest {

    private MockMvc mockMvc;
    @Mock
    private ValidatorService validatorService;
    @InjectMocks
    private EditorController editorController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(editorController).build();
    }

    @Test
    public void validateYamlController_whenValidYaml_returnTrue() throws Exception {
        String inputYamlData = "server:\n" +
                "    port: 9090\n" +
                "    servlet.context-path: /validator\n" +
                "logging:\n" +
                "  level.root: info\n" +
                "  file: /packages/logs/yaml-validator/yaml-validator.log\n" +
                "spring.pid.fail-on-write-error: true\n" +
                "spring.pid.file: /packages/config/yaml-validator/yaml-validator.pid";
        ValidationEntity inputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputYamlData).valid(true).validationMessage("Valid YAML").build();
        Mockito.doReturn(outputValidationEntity).when(validatorService).validateYamlService(inputValidationEntity.getInputMessage());

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
    }

    @Test
    public void validateYamlController_whenInvalidYaml_returnFalse() throws Exception {
        String inputYamlData = "server:\n" +
                "    port: 9090\n" +
                "    servlet.context-path: /validator\n" +
                "logging:\n" +
                "level.root: info\n" +
                "  file: /packages/logs/yaml-validator/yaml-validator.log\n" +
                "spring.pid.fail-on-write-error: true\n" +
                "spring.pid.file: /packages/config/yaml-validator/yaml-validator.pid";
        ValidationEntity inputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputYamlData).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputYamlData).valid(false).build();
        Mockito.doReturn(outputValidationEntity).when(validatorService).validateYamlService(inputValidationEntity.getInputMessage());

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