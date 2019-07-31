package com.drauto.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drauto.locators.Element;

/**
 * This class will parse the json file (Object repository), and return the
 * element(i.e the wrappper class over WebElement)
 * 
 * 
 * @author Deepak R
 *
 */
public class JSONElementReader {
    private static final Logger log = LoggerFactory
            .getLogger(JSONElementReader.class);

    private JSONObject jsonObject;
    private JSONParser jsonParser;

    public JSONElementReader(String jsonPath) {
        jsonParser = new JSONParser();
        jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser
                    .parse(new FileReader(jsonPath));

        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException occured", e);
        } catch (IOException e) {
            log.error("IOException occured", e);
        } catch (ParseException e) {
            log.error("ParseException occured", e);
        }
    }

    /**
     * @param elementName
     *            name of the element in the Json repository
     * @return the element from <i>elementName</i>
     */
    public Element getElementFromJson(String elementName) {
        Object[] obj = null;
        return getElementFromJson(elementName, obj);
    }

    /**
     * This method will return the element from JSON Object repository
     * 
     */
    public Element getElementFromJson(String elementName, Object... param) {
        Object jsonObjTemp = jsonObject.get(elementName);
        if (jsonObjTemp == null) {
            log.error("Object {} not found", elementName);
            return null;
        }
        String temp = null;
        String name = elementName;
        temp = ((JSONObject) jsonObjTemp).get("selectorType")
                + Constants.EMPTY_STRING;
        if (temp == null || (temp != null && temp.isEmpty())) {
            log.error("Selector type not found for {}", elementName);
            return null;
        }
        String selectorType = temp;

        temp = ((JSONObject) jsonObjTemp).get("selectorValue")
                + Constants.EMPTY_STRING;
        if (temp == null || (temp != null && temp.isEmpty())) {
            log.error("Selector value not found for {}", elementName);
            return null;
        }
        String selectorValue = temp;

        temp = ((JSONObject) jsonObjTemp).get("timeout")
                + Constants.EMPTY_STRING;

        long timeout = (temp == null || (temp != null && "null"
                .equalsIgnoreCase(temp))) ? Element.DEFAULT_TIMEOUT_IN_MILLISECONDS
                : Long.valueOf(temp + Constants.EMPTY_STRING);
        if (param != null) {
            selectorValue = String.format(selectorValue, param);
        }
        By by = getSelector(selectorType, selectorValue);
        return new Element(by, name, timeout);
    }

    public By getSelector(String selectorType, String selectorValue) {
        switch (selectorType.toLowerCase()) {
        case Constants.XPATH:
            return By.xpath(selectorValue);
        case Constants.CSS_SELECTOR:
            return By.cssSelector(selectorValue);
        case Constants.NAME:
            return By.name(selectorValue);
        case Constants.ID:
            return By.id(selectorValue);
        case Constants.LINK_TEXT:
            return By.linkText(selectorValue);
        case Constants.PARTIAL_LINK_TEXT:
            return By.partialLinkText(selectorValue);
        case Constants.CLASS:
            return By.className(selectorValue);
        default:
            log.error("no options found for type " + selectorType);
            return null;
        }
    }
}
