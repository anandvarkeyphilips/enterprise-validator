/*
package io.exnihilo.validator;


import io.exnihilo.validator.controller.EditorController;
import io.exnihilo.validator.entity.ValidationEntity;
import io.exnihilo.validator.service.ValidatorService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ValidatorControllerTest {


    private MockMvc mockMvc;

    @Mock
    private EditorController editorController;

    @InjectMocks
    private ValidatorService validatorService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(validatorService).build();
    }

    @Test
    public void testHelloWorld() throws Exception {
        String inputMessage = "server:\n" +
                "    port: 9090\n" +
                "    servlet.context-path: /validator\n" +
                "logging:\n" +
                "  level.root: info\n" +
                "  file: /packages/logs/yaml-validator/yaml-validator.log\n" +
                "spring.pid.fail-on-write-error: true\n" +
                "spring.pid.file: /packages/config/yaml-validator/yaml-validator.pid";
        ValidationEntity inputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputMessage).build();
        ValidationEntity outputValidationEntity = ValidationEntity.builder()
                .inputMessage(inputMessage).isValid(true).build();

        when(editorController.validateYamlController(inputValidationEntity))
                .thenReturn(
                        new ResponseEntity<Object>(validatorService.validateYamlService(inputValidationEntity.getValidationMessage()), HttpStatus.OK));

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));

        verify(editorController).hello();


        mockMvc.perform(post("/hello/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(outputValidationEntity))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Greetings")))
                .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)));

    }

    @Test
    public void testHelloWorldJson() throws Exception {
        mockMvc.perform(get("/hello/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Greetings")))
                .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
    }

    @Test
    public void testPost() throws Exception {
        String json = "{\n" +
                "  \"title\": \"Greetings\",\n" +
                "  \"value\": \"Hello World\"\n" +
                "}";
        mockMvc.perform(post("/hello/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Greetings")))
                .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
    }
}
*/
