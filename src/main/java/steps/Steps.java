package steps;

import configs.SpringConfiguration;
import context.ScenarioContext;
import enums.Operation;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static context.Key.*;
import static org.junit.Assert.*;

@ContextConfiguration(classes = SpringConfiguration.class)
public class Steps {
    @Autowired
    ScenarioContext scenarioContext;

    @Given("user is on home page edge")
    public void accessHomePageEdge() throws MalformedURLException {
        //Setting system properties of EdgeDriver
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");

        EdgeOptions options = new EdgeOptions();

//Creating an object of EdgeDriver
        WebDriver driver = new EdgeDriver(options);
        driver.manage().window().maximize();

//Deleting all the cookies
        driver.manage().deleteAllCookies();

//Specifiying pageLoadTimeout and Implicit wait
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

//launching the specified URL
        driver.get("https://www.google.com/");

//Locating the elements using name locator for the text box
        driver.findElement(By.name("q")).sendKeys("BrowserStack Guide");
        driver.close();
    }

    @Given("the user opens the system calculator")
    public void theUserOpensTheSystemCalculator() {
        WindowsDriver windowsDriver = null;
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");

            windowsDriver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            windowsDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            scenarioContext.save(WINDOWS_DRIVER, windowsDriver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("the user enters number {}")
    public void theUserEntersNumber(Double number) {
        ArrayList<Double> numbers = (ArrayList<Double>) scenarioContext.get(NUMBERS);
        if (numbers==null) {
            numbers = new ArrayList<>();
        }
        numbers.add(number);
        scenarioContext.save(NUMBERS, numbers);

    }

    @When("the user presses on the {} sign")
    public void theUserPressesOnTheSign(String operation) {
        scenarioContext.save(OPERATION, Operation.getBySign(operation));
    }

    @Then("the result is as expected")
    public void theResultIsAsExpected() {
        WindowsDriver windowsDriver = (WindowsDriver) scenarioContext.get(WINDOWS_DRIVER);
        ArrayList<Double> numbers = (ArrayList<Double>) scenarioContext.get(NUMBERS);
        Operation operation = (Operation) scenarioContext.get(OPERATION);

        Double firstNumber = numbers.get(0);
        Double secondNumber = numbers.get(1);
        Double expectedResult;
        switch (operation.sign()) {
            case "+":
                expectedResult = firstNumber + secondNumber;
                break;
            case "-":
                expectedResult = firstNumber - secondNumber;
                break;
            case "*":
                expectedResult = firstNumber * secondNumber;
                break;
            case "/":
                expectedResult = firstNumber / secondNumber;
                break;
            default:
                throw new IllegalArgumentException("Invalid operator.");
        }

        windowsDriver.findElementByAccessibilityId("CalculatorResults").sendKeys(String.valueOf(numbers.get(0)));
        windowsDriver.findElement(By.name(operation.legacyName())).click();
        windowsDriver.findElementByAccessibilityId("CalculatorResults").sendKeys(String.valueOf(numbers.get(1)));
        windowsDriver.findElement(By.name("Equals")).click();

        WebElement calculatorResult= windowsDriver.findElementByAccessibilityId("CalculatorResults");
        assertNotNull(calculatorResult);
        Double actualResult = Double.valueOf(calculatorResult.getText().replace("Display is", "").trim());

        assertEquals(String.format("%.2f", expectedResult), String.format("%.2f", actualResult));
        windowsDriver.quit();
    }

    @Given("user is on home page")
    public void accessHomePage() throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://www.bkb.ch/");
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        scenarioContext.save(WEB_DRIVER, driver);
    }

    @When("user press on login button")
    public void userPressOnLoginButton() {
        WebDriver driver = (WebDriver) scenarioContext.get(WEB_DRIVER);
        driver.findElement(By.linkText("Login")).click();
    }

    @And("enter {string} as Identifikationsnummer")
    public void enterAAsIdentifikationsnummer(String id) {
        WebDriver driver = (WebDriver) scenarioContext.get(WEB_DRIVER);

        Object[] windowHandles=driver.getWindowHandles().toArray();
        driver.switchTo().window((String) windowHandles[1]);

        WebElement usernameBox = driver.findElement(By.id("username"));
        usernameBox.sendKeys(id);
    }

    @And("enter {string} as Password")
    public void enterBAsPassword(String pass) {
        WebDriver driver = (WebDriver) scenarioContext.get(WEB_DRIVER);
        WebElement passBox = driver.findElement(By.id("password"));
        passBox.sendKeys(pass);
    }

    @When("press Login button")
    public void pressLoginButton() {
        WebDriver driver = (WebDriver) scenarioContext.get(WEB_DRIVER);
        driver.findElement(By.id("loginButton")).click();
    }

    @Then("pop-up contains correct message")
    public void popUpContainsCorrectMessage() {
        WebDriver driver = (WebDriver) scenarioContext.get(WEB_DRIVER);
        String warningMessage = driver.findElement(By.xpath("//*[@id='alertText']")).getText();
        String expectedMessage = "The identification number and/or password you entered are not correct.\n" +
                "Please check your entries and try again.";
        assertEquals(expectedMessage, warningMessage);
        driver.close();
        driver.quit();
    }
}