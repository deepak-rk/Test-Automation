package com.drauto.locators;

import org.openqa.selenium.By;

/**
 * Wrapper Class over {@link By}
 * 
 * @author Deepak R
 *
 */
public class Element {

    public static final long DEFAULT_TIMEOUT_IN_MILLISECONDS = 5000;
    private final By by;
    private final String name;
    private long timeout;

    /**
     * @param by
     *            locator value
     * @param name
     *            identifier for element
     * @param timeout
     *            timeout interval in milliseconds
     */
    public Element(By by, String name, long timeout) {
        this.by = by;
        this.name = name;
        this.timeout = timeout;
    }

    /**
     * @param by
     *            locator value
     * @param name
     *            identifier for element
     */
    public Element(By by, String name) {
        this(by, name, DEFAULT_TIMEOUT_IN_MILLISECONDS);
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
