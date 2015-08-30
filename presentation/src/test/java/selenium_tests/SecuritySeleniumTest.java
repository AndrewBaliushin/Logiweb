package selenium_tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium_tests.ext.AuthUtils;

/**
 * Test security features: login & logout.
 *
 * @author Andrey Baliushin
 */
public class SecuritySeleniumTest extends AbstractSeleniumTest {

    private AuthUtils authUtils = new AuthUtils(getDriver());


    @Test
    public void loginTest() {
        authUtils.loginAsManager();
        String url = getDriver().getCurrentUrl();
        Assert.assertFalse(url.contains("login")); //wasn't redirected to login page
    }

    @Test
    public void logoutTest() {
        authUtils.loginAsManager();
        authUtils.logout();
        getDriver().get(getBaseUrl());
        Assert.assertTrue(getDriver().getCurrentUrl().contains("login")); //redirected to login page
    }
}
