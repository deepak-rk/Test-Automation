package com.drauto.utilities;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drauto.locators.Element;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * 
 * Utility class which deals with all the interactions with the
 * {@link WebElement}
 * 
 * @author Deepak R
 *
 */
/**
 * @author Deepak
 *
 */
public class Interactions {

    private static final Logger log = LoggerFactory.getLogger(Interactions.class);
    private static Properties driverConfig;

    private static final int EXPECTED_CONDITION_TIMEOUT;
    private static final int POLLING_EVERY;
    public static final int DEFAULT_TIMEOUT_IN_MILLISECONDS;
    public static final int PAGE_LOAD_TIMEOUT;

    private WebDriver driver;
    private Actions actionDriver;
    private By loading;

    static {
        driverConfig = Utilities
                .readProperties(System.getProperty("user.dir") + "/src/test/resources/driverConfig.properties");
        EXPECTED_CONDITION_TIMEOUT = Integer.valueOf(driverConfig.getProperty("expectedConditionWait"));
        POLLING_EVERY = Integer.valueOf(driverConfig.getProperty("pollingEvery"));
        DEFAULT_TIMEOUT_IN_MILLISECONDS = Integer.valueOf(driverConfig.getProperty("defaultElementTimeout"));
        PAGE_LOAD_TIMEOUT = Integer.valueOf(driverConfig.getProperty("pageLoadTimeout"));

    }

    public Interactions(WebDriver driver) {
        this.driver = driver;
        this.actionDriver = new Actions(driver);

    }

    /**
     * 
     * 
     * @param by
     * @return element after handling waits
     */
    public WebElement getElementWithWait(final Element by) {
        if (by == null) {
            log.warn("by is null");
            return null;
        }
        if (loading != null) {
            WebDriverWait waitLoading = new WebDriverWait(driver, EXPECTED_CONDITION_TIMEOUT);
            waitLoading.until((ExpectedCondition<Boolean>) wd -> (driver.findElements(loading).size() == 0));
        }

        WebElement result = null;
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMillis(by.getTimeout()))
                    .pollingEvery(Duration.ofMillis(POLLING_EVERY)).ignoring(NoSuchElementException.class);

            result = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(by.getBy());

                }
            });
        } catch (TimeoutException e) {
            log.warn("Element '{}' not found", by);
        }

        return result;

    }

    /**
     * 
     * 
     * @param by
     * @return elements after handling waits
     */
    public List<WebElement> getElementsWithWait(final Element by) {

        WebElement result = getElementWithWait(by);
        if (result == null) {
            return new ArrayList<WebElement>();
        } else {
            return driver.findElements(by.getBy());
        }

    }

    /**
     * @param by
     * @return true if click operation is successfull
     */
    public boolean clickOnElement(Element by) {

        WebElement element = getElementWithWait(by);
        if (element == null) {
            log.error("element {} not found", by.getName());
            return false;
        }
        log.info("Clicking on {}", by.getName());
        element.click();
        return true;
    }

    /**
     * 
     * This method will type text to the locator
     * 
     * @param by   locator value
     * @param text text to be entered
     * 
     * 
     */
    public boolean typeText(Element by, String text) {

        WebElement element = getElementWithWait(by);
        if (text == null) {
            log.warn("Text is null");
        }

        if (element == null) {
            log.error("element {} not found", by);
            return false;
        }
        log.info("Typing on {}", by.getName());
        element.sendKeys(text);
        return true;
    }

    /**
     * this method will select value from drpdown using visible text
     * 
     * @param by
     * @param text
     * @return
     */
    public boolean selectByVisibleText(Element by, String text) {

        WebElement element = getElementWithWait(by);
        if (text == null) {
            log.warn("Text is null");
        }

        if (element == null) {
            log.error("element {} not found", by);
            return false;
        }
        log.info("Selecting on {}", by.getName());

        Select select = new Select(element);

        select.selectByVisibleText(text);
        return true;
    }

    /**
     * @param by   element
     * @param text text to be entered
     * @return true if the given text is entered in the textbox
     */
    public boolean typeTextInTextArea(Element by, String text) {

        WebElement element = getElementWithWait(by);
        if (text == null) {
            log.warn("Text is null");
        }

        if (element == null) {
            log.error("element {} not found", by);
            return false;
        }
        log.info("Typing on {}", by.getName());
        actionDriver.click(element);
        actionDriver.sendKeys(text);
        return true;
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * This method will move element from source to destination
     * 
     * @param source      Source element
     * @param destination Destination Element
     *
     */
    public boolean moveToElement(Element source, Element destination) {

        WebElement elementSource = getElementWithWait(source);
        WebElement elementDestination = getElementWithWait(destination);

        if (elementSource == null) {
            log.error("element {} not found", source.getName());
            return false;
        }

        if (elementDestination == null) {
            log.error("element {} not found", destination.getName());
            return false;
        }
        log.info("Moving '{}' to '{}'", source.getName(), destination.getName());
        actionDriver.clickAndHold(elementSource).moveToElement(elementDestination).release().build().perform();
        return true;
    }

    public final Predicate<WebElement> loadingPredicate() {
        return new Predicate<WebElement>() {
            @Override
            public boolean apply(WebElement element) {
                if (element != null) {
                    log.info("Page is loading");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        log.error("Exception occured", e);
                    }
                    return false;
                }
                return true;

            }
        };
    }

    public By getLoading() {
        return loading;
    }

    public void setLoading(By loading) {
        this.loading = loading;
    }

}
