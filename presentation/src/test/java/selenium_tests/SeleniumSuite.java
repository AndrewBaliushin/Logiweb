package selenium_tests;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Suite of selenium test cases that are covering all UI features of Logiweb application.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SecuritySeleniumTest.class,
        DriverSeleniumTest.class
})
public class SeleniumSuite {

    public static final String APP_URL = "localhost:8080/logiweb";

    public static final String MANAGER_NAME = "manager@logiweb.com";
    public static final String MANAGER_PASS = "12345";

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
