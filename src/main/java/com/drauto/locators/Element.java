package com.drauto.locators;

import org.openqa.selenium.By;

import com.drauto.utilities.Interactions;

/**
 * Wrapper Class over {@link By}
 * 
 * @author Deepak R
 *
 */
public class Element {

    private final By by;
    private final String name;
    private long timeout;

    /**
     * @param by      locator value
     * @param name    identifier for element
     * @param timeout timeout interval in milliseconds
     */
    public Element(By by, String name, long timeout) {
        this.by = by;
        this.name = name;
        this.timeout = timeout;
    }

    /**
     * @param by   locator value
     * @param name identifier for element
     */
    public Element(By by, String name) {
        this(by, name, Interactions.DEFAULT_TIMEOUT_IN_MILLISECONDS);
    }

    public By getBy() {
        return by;
    }

    public String getName() {
        return name;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
