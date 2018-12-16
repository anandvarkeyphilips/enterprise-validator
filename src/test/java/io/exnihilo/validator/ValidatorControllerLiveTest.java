package io.exnihilo.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.validator.entity.ValidationEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidatorControllerLiveTest {

    private TestRestTemplate restTemplate;
    private URL url;
    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate();
        url = new URL("http://localhost:" + port);
    }

    @Test
    public void whenUserTrieHomePage_ThenSuccess() {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url.toString() + "/editor", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody()
                .contains("Anand Varkey Philips"));
    }

    @Test
    public void whenUserTriesInvalidPage_ThenFail() {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url.toString() + "/invalidPage", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody()
                .contains("We couldn't find what you were looking for."));
    }


    @Test
    public void whenUserPostValidXML_ThenSuccess() throws IllegalStateException, IOException {
        File file = ResourceUtils.getFile("classpath:application.yml");
        String inputYamlData = new String(Files.readAllBytes(file.toPath()));

        ValidationEntity inputValidationEntity = ValidationEntity.builder().inputMessage(inputYamlData).build();

        ObjectMapper mapper = new ObjectMapper();
        String objectJsonString = mapper.writeValueAsString(inputValidationEntity);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(objectJsonString, httpHeaders);

        ResponseEntity<ValidationEntity> response = restTemplate.postForEntity(url.toString() + "/yaml", httpEntity, ValidationEntity.class);
        assertTrue(response
                .getBody()
                .isValid());
    }
}