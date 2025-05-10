package com.laboratorio.automata;

import com.laboratorio.model.NombreRedSocial;
import com.laboratorio.util.AutomataSessionManager;
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
    private static AutomataSessionManager sessionManager;
    private static String testUser;
    private static String password;
    private static String followedUser;

    @BeforeAll
    public static void initTwitterClient() {
        ConfigReader config = new ConfigReader("config//twitter_conf.properties");
        testUser = config.getProperty("test_user");
        password = config.getProperty("test_password");
        sessionManager = new AutomataSessionManager();

    }
    @AfterAll
    public static void releaseTwitterClient() {
        sessionManager.releaseSessions();
    }

    @Test @Order(1)
    public void postearImagen() {
        ConfigReader config = new ConfigReader("config//twitter_conf.properties");
        String rutaImagen = config.getProperty("imagen_test");
        RedSocialWebClient redSocialWebClient = sessionManager.getSession(NombreRedSocial.TWITTER, testUser, password);
        boolean result = redSocialWebClient.postImage("Tweet de prueba con imagen enviado desde Playwright", rutaImagen);
        assertTrue(result);
    }

    @Test @Order(2)
    public void eliminarPrimerPost() {
        RedSocialWebClient redSocialWebClient = sessionManager.getSession(NombreRedSocial.TWITTER, testUser, password);
        boolean result = redSocialWebClient.deleteFirstPost();
        assertTrue(result);
    }

    @Test @Order(3)
    public void seguirPrimeraSugerencia() {
        RedSocialWebClient redSocialWebClient = sessionManager.getSession(NombreRedSocial.TWITTER, testUser, password);
        boolean result = redSocialWebClient.goToFirstSuggested();
        assertTrue(result);
        followedUser = redSocialWebClient.getUsername();
        assertFalse(followedUser.isBlank());
        result = redSocialWebClient.follow();
        assertTrue(result);
    }

    @Test @Order(4)
    public void dejarDeSeguirSugerencia() {
        RedSocialWebClient redSocialWebClient = sessionManager.getSession(NombreRedSocial.TWITTER, testUser, password);
        boolean result = redSocialWebClient.navigateToUserPage(followedUser);
        assertTrue(result);
        result = redSocialWebClient.unfollow();
        assertTrue(result);
    }
}