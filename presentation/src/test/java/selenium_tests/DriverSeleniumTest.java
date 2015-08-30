package selenium_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import selenium_tests.ext.AuthUtils;

/**
 * Selenium tests for cases:
 * Create new driver.
 * Login as driver.
 * Remove driver.
 *
 * @author Andrey Baliushin
 */
public class DriverSeleniumTest extends AbstractSeleniumTest {

    private AuthUtils authUtils = new AuthUtils(getDriver());

    @Test
    public void createEditRemoverDriverTest() {
        authUtils.loginAsManager();
        createNewDriver("9999", "CreateTest", "CreateTest");
        editDriver("9999", "6666", "EditTest", "EditTest");
        removeDriver("6666");
    }

    @Test
    public void loginAsDriverTest() {
        authUtils.loginAsManager();
        createNewDriver("9999", "LoginTest", "LoginTest");
        authUtils.logout();

        authUtils.login("driver-9999@logiweb.com", "12345");
        getDriver().findElement(By.xpath("//h3[contains(.,'Driver info')]"));
        authUtils.logout();

        authUtils.loginAsManager();
        removeDriver("9999");
    }

    private void goToNewDriverPage() {
        getDriver().get(getBaseUrl());
        getDriver().findElement(By.xpath("//a[contains(.,'Drivers')]")).click();
        getDriver().findElement(By.xpath("//a[contains(.,'Add')]")).click();
    }

    private void goToEditDriverPage(String driverEmplId) {
        getDriver().get(getBaseUrl());
        getDriver().findElement(By.xpath("//a[contains(.,'Drivers')]")).click();
        getDriver().findElement(By.
                xpath("//tr[contains(.,'" + driverEmplId + "')]//span[contains(@class, 'pencil')]")).
                click();
    }

    private void createNewDriver(String emplID, String name, String surname) {
        goToNewDriverPage();
        getDriver().findElement(By.id("employeeId")).sendKeys(emplID);
        getDriver().findElement(By.id("name")).sendKeys(name);
        getDriver().findElement(By.id("surname")).sendKeys(surname);
        getDriver().findElement(By.xpath("//button[contains(.,'Create')]")).click();
    }

    private void editDriver(String currentEmplId, String newEmplId, String name, String surname) {
        goToEditDriverPage(currentEmplId);
        getDriver().findElement(By.id("name")).clear();
        getDriver().findElement(By.id("employeeId")).clear();
        getDriver().findElement(By.id("surname")).clear();

        getDriver().findElement(By.id("employeeId")).sendKeys(newEmplId);
        getDriver().findElement(By.id("name")).sendKeys(name);
        getDriver().findElement(By.id("surname")).sendKeys(surname);
        getDriver().findElement(By.xpath("//button[contains(.,'Edit')]")).click();
    }

    private void removeDriver(String driverEmplId) {
        getDriver().get(getBaseUrl());
        getDriver().findElement(By.xpath("//a[contains(.,'Drivers')]")).click();
        getDriver().findElement(By.
                xpath("//tr[contains(.,'" + driverEmplId + "')]//span[contains(@class, 'remove')]")).click();
        getDriver().findElement(By.xpath("//div[contains(@class, 'modal')]//button[contains(., 'OK')]")).click();
    }


}
