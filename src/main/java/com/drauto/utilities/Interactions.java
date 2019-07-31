package com.drauto.utilities;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drauto.locators.Element;
import com.google.common.base.Function;

/**
 * 
 * Utility class which deals with all the interactions with the
 * {@link WebElement}
 * 
 * @author Deepak R
 *
 */
/**
 * @author Home
 *
 */
public class Interactions {

    private WebDriver driver;
    private Actions actionDriver;

    private static final Logger log = LoggerFactory
            .getLogger(Interactions.class);

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
        WebElement result = null;
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofMillis(by.getTimeout()))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class);

            result = wait.until(new Function<WebDriver, WebElement>() {

                public WebElement apply(WebDriver driver) {

                    return driver.findElement(by.getBy());

                }
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
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

        if (by == null) {
            log.warn("by is null");

            return new ArrayList<WebElement>();
        }

        List<WebElement> result = new ArrayList<WebElement>();
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofMillis(by.getTimeout()))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class);

            result = (List<WebElement>) wait
                    .until(new Function<WebDriver, List<WebElement>>() {

                        public List<WebElement> apply(WebDriver driver) {

                            return driver.findElements(by.getBy());

                        }
                    });
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return result;

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
     * @param by
     *            locator value
     * @param text
     *            text to be entered
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
     * @param by
     *            element
     * @param text
     *            text to be entered
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
     * @param source
     *            Source element
     * @param destination
     *            Destination Element
     *
     */
    public boolean moveToElement(Element source, Element destination) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        actionDriver.clickAndHold(elementSource)
                .moveToElement(elementDestination).release().build().perform();
        return true;
    }
}
