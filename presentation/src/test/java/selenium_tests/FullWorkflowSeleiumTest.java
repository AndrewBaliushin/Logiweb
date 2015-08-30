package selenium_tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import selenium_tests.ext.AuthUtils;

/**
 * Selenium test for complete Logiweb workflow:
 * Create 2 drivers in Arroyo.
 * Create Truck in Arroyo.
 * Create order.
 * Add cargo to order.
 * Add truck and drivers to order.
 * Try to delete drivers and truck. (fail)
 *
 * @author Andrey Baliushin
 */
public class FullWorkflowSeleiumTest extends AbstractSeleniumTest {

    private AuthUtils authUtils = new AuthUtils(getDriver());

    @Test
    public void workflowTest() {
        authUtils.loginAsManager();
//        createNewDriver("9999", "DriverTest1", "DriverTest1");
//        createNewDriver("6666", "DriverTest2", "DriverTest2");

        createNewTruck("tt99999", "10", 2, "Den");




    }

    private void createNewDriver(String emplID, String name, String surname) {
        goToNewDriverPage();
        getDriver().findElement(By.id("employeeId")).sendKeys(emplID);
        getDriver().findElement(By.id("name")).sendKeys(name);
        getDriver().findElement(By.id("surname")).sendKeys(surname);
        getDriver().findElement(By.xpath("//button[contains(.,'Create')]")).click();
    }

    private void goToNewDriverPage() {
        getDriver().get(getBaseUrl());
        getDriver().findElement(By.xpath("//a[contains(.,'Drivers')]")).click();
        getDriver().findElement(By.xpath("//a[contains(.,'Add')]")).click();
    }

    private void createNewTruck(String licensePlate, String cargoCapacity, int crewSize, String city) {
        goToNewTruckPage();
        getDriver().findElement(By.id("licencePlate")).sendKeys(licensePlate);
        getDriver().findElement(By.id("cargoCapacity")).sendKeys(cargoCapacity);
        getDriver().findElement(By.xpath("//input[@value='" + crewSize + "']")).click();
        new Select(getDriver().findElement(By.id("currentCityId"))).selectByVisibleText(city);

        getDriver().findElement(By.xpath("//button[contains(.,'Create')]")).click();
    }

    private void goToNewTruckPage() {
        getDriver().get(getBaseUrl());
        getDriver().findElement(By.xpath("//a[contains(.,'Trucks')]")).click();
        getDriver().findElement(By.xpath("//a[contains(.,'Add')]")).click();
    }



}
