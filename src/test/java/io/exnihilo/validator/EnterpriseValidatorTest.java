package io.exnihilo.validator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.exnihilo.validator.config.MemoryAppender;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class tests the bean context configurations
 *
 * @author Anand Varkey Philips
 * @since 2.0.6.RELEASE
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class EnterpriseValidatorTest {

  private MemoryAppender memoryAppender;
  private static final String LOGGER_NAME = "io.exnihilo.validator";

  @Before
  public void setup() {
    Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
    memoryAppender = new MemoryAppender();
    memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    logger.setLevel(Level.DEBUG);
    logger.addAppender(memoryAppender);
    memoryAppender.start();
  }

  @Test
  public void contextLoads() {
    Assert.assertTrue(true);
  }

  @Test
  public void testApplicationStartAndExitLogs() {
    EnterpriseValidator enterpriseValidator = new EnterpriseValidator();
    enterpriseValidator.logApplicationStart();
    enterpriseValidator.logApplicationExit();

    Assertions.assertThat(memoryAppender.countEventsForLogger(LOGGER_NAME)).isEqualTo(2);
    Assertions.assertThat(memoryAppender.search("Application is started at Indian Standard Time", Level.INFO).size())
        .isEqualTo(1);
    Assertions
        .assertThat(
            memoryAppender.search("Application is being gracefully stopped at Indian Standard Time", Level.INFO).size())
        .isEqualTo(1);
    Assertions
        .assertThat(
            memoryAppender.contains("Application is being gracefully stopped at Indian Standard Time", Level.TRACE))
        .isFalse();
  }
}
