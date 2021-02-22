package com.danb.discord.stocks.watcher.sec;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SecGovCrawler {
    private static final String APIA_URL = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent";

    private WebDriver driver;

    private void prepareDriver() {
        System.setProperty(
                "webdriver.chrome.driver",
                "webdriver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    private void searchLatestEntries() {
        driver.get(APIA_URL);
    }
}
