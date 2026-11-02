package io.exnihilo.validator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.validator.entity.ValidationEntity;
import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditorControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;
  @LocalServerPort
  private int port;
  @Autowired
  private ServletContext servletContext;

  private URL url;

  @BeforeEach
  void setUp() throws IOException {
    url = URI.create("http://localhost:" + port + "/" + servletContext.getContextPath()).toURL();
  }

  @Test
  void whenUserTrieHomePage_ThenSuccess() {
    ResponseEntity<String> response = restTemplate.getForEntity(url.toString() + "/editor", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Anand Varkey Philips"));
  }

  @Test
  void whenUserTriesInvalidPage_ThenFail() {
    ResponseEntity<String> response = restTemplate.getForEntity(url.toString() + "/invalidPage", String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertTrue(response.getBody().contains("We couldn't find what you were looking for."));
  }

  @Test
  void whenError_ThenRouteToErrorPage() {
    ResponseEntity<String> response = restTemplate.getForEntity(url.toString() + "/error", String.class);
    assertTrue(response.getBody().contains("This doesn't happen usually"));
  }

  @Test
  void whenUserPostValidXML_ThenSuccess() throws IllegalStateException, IOException {
    File file = ResourceUtils.getFile("classpath:valid-yaml.yml");
    String inputYamlData = new String(Files.readAllBytes(file.toPath()));
    ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();

    ObjectMapper mapper = new ObjectMapper();
    String objectJsonString = mapper.writeValueAsString(inputValidationEntity);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Content-Type", "application/json");
    HttpEntity<String> httpEntity = new HttpEntity<>(objectJsonString, httpHeaders);

    ResponseEntity<ValidationEntity> response =
        restTemplate.postForEntity(url.toString() + "/yaml", httpEntity, ValidationEntity.class);
    assertTrue(response.getBody().isValid());
  }
}
