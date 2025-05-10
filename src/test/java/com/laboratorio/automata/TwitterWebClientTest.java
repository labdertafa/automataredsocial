package com.laboratorio.automata;

import com.laboratorio.implementacion.TwitterWebClient;
import com.laboratorio.util.ConfigReader;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * author Rafael
 * version 1.0
 * created 10/05/2025
 * updated 10/05/2025
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TwitterWebClientTest {
    private static RedSocialWebClient redSocialWebClient;
    private static String followedUser;

    @BeforeAll
    public static void initTwitterClient() {
        ConfigReader config = new ConfigReader("config//twitter_conf.properties");
        String testUser = config.getProperty("test_user");
        String password = config.getProperty("test_password");
        String url = config.getProperty("twitter_url");
        redSocialWebClient = new TwitterWebClient(url, testUser, password);
        if (!redSocialWebClient.login()) {
            redSocialWebClient = null;
        }
    }
    @AfterAll
    public static void releaseTwitterClient() {
        if (redSocialWebClient != null) {
            redSocialWebClient.closeBrowser();
        }
    }

    @Test @Order(1)
    public void postearImagen() {
        ConfigReader config = new ConfigReader("config//twitter_conf.properties");
        String rutaImagen = config.getProperty("imagen_test");
        boolean result = redSocialWebClient.postImage("Tweet de prueba con imagen enviado desde Playwright", rutaImagen);
        assertTrue(result);
    }

    @Test @Order(2)
    public void eliminarPrimerPost() {
        boolean result = redSocialWebClient.deleteFirstPost();
        assertTrue(result);
    }

    @Test @Order(3)
    public void seguirPrimeraSugerencia() {
        boolean result = redSocialWebClient.goToFirstSuggested();
        assertTrue(result);
        followedUser = redSocialWebClient.getUsername();
        assertFalse(followedUser.isBlank());
        result = redSocialWebClient.follow();
        assertTrue(result);
    }

    @Test @Order(4)
    public void dejarDeSeguirSugerencia() {
        boolean result = redSocialWebClient.navigateToUserPage(followedUser);
        assertTrue(result);
        result = redSocialWebClient.unfollow();
        assertTrue(result);
    }
}