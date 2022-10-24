package com.roomescape.server.config;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

public class ChromeDriverContext {
    public static final String CHROME_DRIVER_ID = "webdriver.chrome.driver";
    private static final Logger logger = LoggerFactory.getLogger(ChromeDriverContext.class);
    private static final String CHROME_DRIVER_PATH = "C:/Users/Sewon/IdeaProjects/escape-server/chromedriver.exe";
    private WebDriver driver;

    @Bean
    public WebDriver getDriver() {
        return driver;
    }

    @Bean
    public WebDriver setupChromeDriver() {
        System.setProperty(CHROME_DRIVER_ID, CHROME_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.setPageLoadStrategy(PageLoadStrategy.NONE);

        try {
            driver = new ChromeDriver(options);
        } catch (Exception e) {
            logger.error("[chrome driver error] msg: {}, cause: {}", e.getMessage(), e.getCause());
        }

        return driver;
    }
}
