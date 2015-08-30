package selenium_tests.ext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium_tests.SeleniumSuite;

/**
 * Created by andrew on 23.08.15.
 */
public class AuthUtils {

    private WebDriver driver;

    public AuthUtils(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriver getDriver() {
        return driver;
    }

    public void login(String username, String pass) {
        getDriver().get(SeleniumSuite.APP_URL + "/login");
        getDriver().findElement(By.id("inputEmail")).sendKeys(username);
        getDriver().findElement(By.id("inputPassword")).sendKeys(pass);
        getDriver().findElement(By.xpath("//form[@class='form-signin']/button")).click();

        (new WebDriverWait(getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("navbar-header")));
    }

    public void loginAsManager() {
        login(SeleniumSuite.MANAGER_NAME, SeleniumSuite.MANAGER_PASS);
    }

    public void logout() {
        getDriver().get(SeleniumSuite.APP_URL + "/logout");
    }
}
