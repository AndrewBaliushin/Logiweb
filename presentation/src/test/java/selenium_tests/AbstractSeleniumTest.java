package selenium_tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 23.08.15.
 */
public abstract class AbstractSeleniumTest {

    private static final String MANAGER_NAME = "manager@logiweb.com";
    private static final String MANAGER_PASS = "12345";

    private static WebDriver driver;

    public WebDriver getDriver() {
        WebDriver commonDriver = SeleniumSuite.getDriver();
        if (commonDriver != null) {
            return commonDriver;
        } else {
            return getLocalWebDriver();
        }
    }

    public String getBaseUrl() {
        return SeleniumSuite.APP_URL;
    }

    private WebDriver getLocalWebDriver() {
        if (driver != null) {
            return driver;
        } else {
            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return driver;
        }
    }

}
