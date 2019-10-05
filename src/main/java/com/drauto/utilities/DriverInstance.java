package com.drauto.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/*/
/**
 * @author Deepak
 *
 */
public class DriverInstance {

    public WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "\\drivers\\" + "chromedriver.exe");
        DesiredCapabilities caps = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.default_content_setting_values.notifications", 1);

        options.setExperimentalOption("prefs", prefs);
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Interactions.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        return driver;
    }

}
