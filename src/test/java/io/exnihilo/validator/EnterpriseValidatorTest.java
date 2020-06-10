package io.exnihilo.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class tests the bean context configurations
 *
 * @author Anand Varkey Philips
 * @since 2.0.6.RELEASE
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EnterpriseValidatorTest {

  @Test
  public void contextLoads() {
    Assert.assertTrue(true);
  }

  @Test
  public void applicationStarts() {
    EnterpriseValidator.main(new String[] {});
  }
}
