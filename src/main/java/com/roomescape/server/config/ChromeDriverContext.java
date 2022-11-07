package com.roomescape.server.config;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromeDriverContext {
    private static final Logger logger = LoggerFactory.getLogger(ChromeDriverContext.class);
    private static final String CHROME_DRIVER_ID = "webdriver.chrome.driver";
//    private static final String CHROME_DRIVER_PATH = "C:/Users/Sewon/IdeaProjects/escape-server/chromedriver.exe";
    private static final String CHROME_DRIVER_PATH = "/Users/sewon/IdeaProjects/escape-server/chromedriver";

    @Bean
    public WebDriver chromeDriver() {
        logger.info("@ChromeDriverContext: chromeDriver start");
        System.setProperty(CHROME_DRIVER_ID, CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        return new ChromeDriver(options);
    }
}
