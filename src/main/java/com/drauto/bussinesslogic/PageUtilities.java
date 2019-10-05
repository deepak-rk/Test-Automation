package com.drauto.bussinesslogic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.drauto.locators.Element;
import com.drauto.utilities.Interactions;
import com.drauto.utilities.JSONElementReader;

/**
 * @author Deepak R
 * 
 *         Base class for all pages and modules. Json Object repository will
 *         have the same name as the className
 *
 */
public class PageUtilities {

    protected final JSONElementReader jsonElementReader;
    protected final Interactions interactions;

    public PageUtilities(WebDriver driver, String name) {
        this.interactions = new Interactions(driver);
        this.jsonElementReader = new JSONElementReader(
                System.getProperty("user.dir") + "/src/test/resources/assignment/locators/" + name + ".json");
    }

    public PageUtilities(WebDriver driver, String name, By by) {
        this(driver, name);
        interactions.setLoading(by);
    }

    /**
     * @param elementName reference name in the json
     * 
     * @return element from the Object repository
     */
    protected Element readElement(String elementName) {
        return jsonElementReader.getElementFromJson(elementName);

    }

    /**
     * @param elementName reference name in the json
     * @param param       parameters within the expression
     * @return parameterized element from the Object repository
     */
    protected Element readElement(String elementName, Object... param) {
        return jsonElementReader.getElementFromJson(elementName, param);

    }

}
