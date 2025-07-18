package com.laboratorio.implementacion;

import com.laboratorio.util.ConfigReader;
import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 *
 * author Rafael
 * version 1.0
 * created 03/05/2025
 * updated 06/07/2025
 */
public class BaseWebClient {
    protected final static Logger log = LogManager.getLogger(BaseWebClient.class);

    private final ConfigReader configReader;
    private final Playwright playwright;
    private final Browser browser;
    protected final Page page;
    protected final String url;
    protected final String username;
    protected final String password;

    public BaseWebClient(String url, String username, String password) {
        this.url = url;

        this.configReader = new ConfigReader("config//twitter_conf.properties");

        this.playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setArgs(List.of("--lang=es-ES"))
                .setHeadless(true);
        this.browser = playwright.chromium().launch(launchOptions);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setLocale("es-ES");
        BrowserContext context = browser.newContext(contextOptions);
        this.page = context.newPage();
        this.page.setDefaultTimeout(20000);
        this.page.navigate(url);

        this.username = username;
        this.password = password;
    }

    public void logError(String message, Exception e) {
        log.error("Mensaje: {}", message);
        log.error("Error: {}", e.getMessage());
        if (e.getCause() !=  null) {
            log.error("Causa: {}", e.getCause().getMessage());
        }
    }

    public Locator getXPathLocator(String lokatorKey) {
        String locator = this.configReader.getProperty(lokatorKey);
        String xpathLocator = "xpath=" + locator;
        return this.page.locator(xpathLocator);
    }

    // Hacer las acciones más lentas y en tiempos variables
    public void waifForAMoment() {
        try {
            int seconds = (int)(Math.random() * 15) + 1;
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            log.error("Error haciendo una pausa: {}", e.getMessage());
        }
    }

    public String getPageLink() {
        return this.page.url();
    }

    public String getUsername() {
        String url = this.page.url();
        String[] partes = url.split("/");

        return partes[partes.length - 1];
    }

    public void pauseInteraction() {
        this.page.pause();
    }

    public void closeBrowser() {
        if (this.browser != null) {
            this.browser.close();
        }
        if (this.playwright != null) {
            this.playwright.close();
        }
    }
}